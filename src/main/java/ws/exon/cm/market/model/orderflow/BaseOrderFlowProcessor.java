package ws.exon.cm.market.model.orderflow;

import lombok.RequiredArgsConstructor;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieSession;
import org.kie.api.time.SessionClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.core.common.candle.Deal;
import ws.exon.cm.market.model.event.data.orderflow.NewDealEvent;
import ws.exon.cm.market.model.orderflow.aggregator.factory.AggregationFactory;
import ws.exon.cm.market.model.strategy.MarketDataProvider;
import ws.exon.cm.market.model.strategy.NoDataFeedAvailable;
import ws.exon.cm.market.service.page.PageReader;
import ws.exon.cm.market.service.page.file.csv.StringPageReader;
import ws.exon.cm.market.utlis.ListChannel;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public abstract class BaseOrderFlowProcessor implements OrderflowProcessor {
    private final Set<AggregationFactory> factories;
    private final Map<String, OrderflowListener> consumers = new HashMap<>();
    private final Map<Symbol, Iterator<Deal>> sources = new HashMap<>();
    @Autowired
    private ApplicationContext context;
    private SessionClock clock;

    public BaseOrderFlowProcessor(Set<AggregationFactory> factories, final MarketDataProvider provider) {
        this.factories = factories;

        final Set<Symbol> reqs = new HashSet<>();
        for (final AggregationFactory af : factories)
            reqs.addAll(af.getIndex());

        for (final Symbol s : reqs) {
            if (!provider.hasDealsSupply(s))
                throw new NoDataFeedAvailable(s);
            sources.put(s, provider.getDealIterator(s, null, null));
        }
    }

    @Override
    public Set<String> getOrderflowEvents() {
        return null;
    }

    public void initialize() {
        final ReleaseId releaseId = dms.createReleaseId("ws.exon.ecm.trade", "OrderFlow", "1.0");
        dms.loadRules(releaseId, "aggregator", AGGREGATES);

        final PageReader.Factory<NewDealEvent> rf = new StringPageReader.ClasspathFactory(DEALS_BTC_FULL, BTC_PRECISION, 2000000);
        final Duration duration = Duration.ofDays(1);
        final OrderFlowTask factory = context.getBean(OrderFlowTask.class);
        //        factory.setDebug(true);
        final KieSession session = factory.init(RXV_AF, date, duration, "ws.exon.ecm.trade:OrderFlow:1.0", rf);

        final Queue<Object> q = new ConcurrentLinkedQueue<>();


        session.registerChannel("CompositeDeals", new ListChannel());
        session.registerChannel("Clusters", new ListChannel());
        session.registerChannel("Candles", new ListChannel());
        session.registerChannel("Gaps", new ListChannel());

        final Collection<?> defaultObjects = session.getObjects();
        final Collection<?> mdObjects = session.getEntryPoint("Market Data/Trades").getObjects();

        session.insert(GF);

        final ExecutorService executor = Executors.newFixedThreadPool(10);
        final long start = System.currentTimeMillis();
//        executor.submit(factory);
//        executor.shutdown();
        factory.run();

    }
}
