package ws.exon.cm.market.model.orderflow;

import lombok.*;
import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.core.common.candle.Deal;
import ws.exon.cm.market.model.event.data.orderflow.NewDealEvent;
import ws.exon.cm.market.model.orderflow.aggregator.factory.AggregationFactory;
import ws.exon.cm.market.service.DroolsManagerService;
import ws.exon.cm.market.service.page.PageReader;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Getter
@Setter
@Component
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class OrderFlowTask extends OrderFlowBase implements Runnable {
    private static final Comparator<Deal> TIME_COMPARATOR = Comparator.comparing(o -> o.getPrice().getTs());

    private final DroolsManagerService dms;
    private final UUID id = UUID.randomUUID();
    @Getter(AccessLevel.NONE)
    private final Map<LocalDate, List<PageReader<NewDealEvent>>> pages = new TreeMap<>();
    private Duration duration;
    private PageReader.Factory<NewDealEvent> dealReaderFactory;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private int capacity;
    private boolean debug;

    private static String formatTaskKey(final LocalDate date, final Duration duration, final String symbol) {
        return String.format("aggregate:tasks:%s:%s:%s", date.toString(), duration.toString(), symbol);
    }

    public KieSession init(final Set<?> factories, final LocalDate date, final Duration duration,
                           final KieSession session, final PageReader.Factory<NewDealEvent> rf) {
        setFactories(factories);
        setDate(date);
        setDuration(duration);
        setSession(session);
        setDealReaderFactory(rf);

        this.init();

        return session;
    }

    public KieSession init(final Set<?> factories, final LocalDate date, final Duration duration, final String releaseId,
                           final PageReader.Factory<NewDealEvent> rf) {
        return init(factories, date, duration, dms.backTestSession(releaseId), rf);
    }

    @SneakyThrows
    protected void init() {
        if (debug) {
            session.addEventListener(new DebugAgendaEventListener());
            session.addEventListener(new DebugRuleRuntimeEventListener());
        }

        super.init();
        final Set<Symbol> symbols = new HashSet<>();
        factories.forEach(s -> {
            if (s instanceof AggregationFactory)
                symbols.addAll(((AggregationFactory) s).getIndex());
        });
        this.capacity = dealReaderFactory.getBatchSize() * symbols.size();
        for (long i = duration.toDays() - 1; i >= 0; i--) {
            final LocalDate dataDate = date.plus(i, ChronoUnit.DAYS);
            final List<PageReader<NewDealEvent>> readers = new ArrayList<>(symbols.size());
            for (final Symbol symbol : symbols) {
                final PageReader<NewDealEvent> reader = dealReaderFactory.create(symbol, dataDate);
                readers.add(reader);
            }
            pages.put(dataDate, readers);
        }
    }

    protected void process() throws InterruptedException {
        this.init();
        final List<Deal> trades = new ArrayList<>(capacity);
        for (final Map.Entry<LocalDate, List<PageReader<NewDealEvent>>> history : pages.entrySet()) {
            final LocalDate date = history.getKey();
            final List<PageReader<NewDealEvent>> readers = history.getValue();
            while (true) {
                trades.clear();
                for (final PageReader<NewDealEvent> pr : readers)
                    pr.getDeals(trades);

                if (!LocalDate.now().equals(date) && trades.isEmpty())
                    break;

                trades.sort(TIME_COMPARATOR);
                processDeals(trades);
            }
        }
        finaliseFlow();
    }

    @Override
    public void run() {
        try {
            process();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
