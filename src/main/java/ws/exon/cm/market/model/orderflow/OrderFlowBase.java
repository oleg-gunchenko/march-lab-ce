package ws.exon.cm.market.model.orderflow;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.time.SessionClock;
import org.kie.api.time.SessionPseudoClock;
import ws.exon.cm.market.model.core.common.candle.Deal;
import ws.exon.cm.market.model.core.common.candle.Price;
import ws.exon.cm.market.model.orderflow.aggregator.factory.AggregationFactory;
import ws.exon.cm.market.model.orderflow.aggregator.factory.DurationFactory;
import ws.exon.cm.market.model.rules.time.CheckpointInterval;
import ws.exon.cm.market.model.rules.time.DurationCheckpoint;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.time.ZoneOffset.UTC;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderFlowBase {
    public static final int CHECKPOINT_INTERVAL = 100;
    @Getter(AccessLevel.NONE)
    private final Map<Object, FactHandle> factoryHandlers = new HashMap<>();
    @Getter(AccessLevel.NONE)
    private final List<FactHandle> checkpointsFH = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    private final Set<DurationCheckpoint> checkpoints = new HashSet<>();
    @Setter
    protected KieSession session;
    protected SessionPseudoClock clock;
    @Setter
    protected LocalDate date;
    @Setter
    protected Set<?> factories;
    private EntryPoint tradesEP;
    private EntryPoint systemEP;
    @Setter
    private long checkpointInterval;
    private boolean hasDuration;
    private long droolsCurrentTime;
    private boolean wmDirty;

    private static long dateToMillis(final LocalDate date) {
        return date.atStartOfDay(UTC).toInstant().toEpochMilli();
    }

//    public OrderFlowBase(final Set<AggregationFactory> factories, final LocalDate date, final KieSession session, final long checkpointInterval) {
//        this.session = session;
//        this.date = date;
//        this.factories = factories;
//        this.checkpointInterval = checkpointInterval;
//    }

    protected void init() {
        this.systemEP = session.getEntryPoint("System");
        this.tradesEP = session.getEntryPoint("Market Data/Trades");

        for (final Object af : factories) {
            addFactory(af);
            if (af instanceof DurationFactory)
                checkpoints.add(new DurationCheckpoint(((DurationFactory) af).getThreshold()));
        }
        this.hasDuration = !checkpoints.isEmpty();
        if (hasDuration)
            session.insert(new CheckpointInterval(0, checkpointInterval));

        final SessionClock clock = session.getSessionClock();

        if (session.getGlobals().getGlobalKeys().contains("clock"))
            session.setGlobal("clock", clock);

        if (!(clock instanceof SessionPseudoClock))
            this.clock = null;
        else {
            final long start = dateToMillis(date);
            this.clock = (SessionPseudoClock) clock;
            ((SessionPseudoClock) clock).advanceTime(start - clock.getCurrentTime(), TimeUnit.MILLISECONDS);
            insertDurationCheckpoint(start);
            this.droolsCurrentTime = clock.getCurrentTime();
        }
        wmDirty = true;
        evaluateRules();
    }

    private void evaluateRules() {
        if (wmDirty) {
            session.fireAllRules();
            wmDirty = false;
        }
    }

    private void insertDurationCheckpoint(final long ts) {
        for (final DurationCheckpoint dc : checkpoints) {
            dc.setTs(ts);
            session.insert(dc);
        }
        wmDirty = true;
    }

    public void addFactory(final Object factory) {
        final FactHandle handle = session.insert(factory);
        factoryHandlers.put(factory, handle);
    }

    public void delete(final AggregationFactory factory) {
        final FactHandle handle = factoryHandlers.get(factory);
        session.delete(handle);
    }

    private void advanceTime(int rounds, final long dt) {
        while (rounds-- > 0)
            advanceTime(CHECKPOINT_INTERVAL);
    }

    private long advanceTime(final long dt) {
        if (dt > 0) {
//            System.out.printf("Java/advanceTime:[%d]\n", dt);
            clock.advanceTime(dt, TimeUnit.MILLISECONDS);
            this.droolsCurrentTime = clock.getCurrentTime();
        }
        return dt;
    }

    private long getTimeDelta(final long ts) {
        final long clk = this.droolsCurrentTime;
        final long dt = ts - clk;
//        System.out.printf("Java/getTimeDelta:[%d, %d, %d]\n", clk, ts, dt);
        return dt;
    }

    private long getTimeDelta(final Price priceLong) {
        return getTimeDelta(priceLong.getTs());
    }

    private long advanceToNearestBound(final long dt) {
        final long cts = droolsCurrentTime;
        final double df = (double) cts / checkpointInterval;
        final long rcts = ((long) df) * checkpointInterval;
        final long dts = getTimeDelta(rcts + checkpointInterval);
        return dt - advanceTime(Math.min(dts, dt));
    }

    public FactHandle insertDeal(final Deal deal) {
        final FactHandle handle = tradesEP.insert(deal);
        wmDirty = true;
        final long dt = getTimeDelta(deal.getPrice());
        if (clock != null && dt > 0) {
            advanceTime(dt);
        }
        evaluateRules();
        return handle;
    }

    public void processDeals(final List<Deal> deals) {
        long dt;
        long start = System.currentTimeMillis();
        int counter = 0;
        int sec = 0;
        int cdeals = 0;
        final long size = deals.size();
        for (final Deal d : deals) {
            cdeals++;
            final long ts = d.getPrice().getTs();
            if (clock != null && hasDuration) {
//                System.out.println("Java/process time intervals");
                for (dt = getTimeDelta(ts); dt > checkpointInterval; ) {
                    dt = advanceToNearestBound(dt);
                    insertDurationCheckpoint(droolsCurrentTime);
                    evaluateRules();
                }
            }

            insertDeal(d);
            counter++;
            long cp = System.currentTimeMillis();
            if (cp - start > 1000) {
                System.out.printf("%d: %d, %d, %d\n", sec++, counter, cdeals, size);
                counter = 0;
                start = cp;
            }
        }
    }

    public void finaliseFlow() {
        final long ts = dateToMillis(date.plusDays(1));
        advanceTime(getTimeDelta(ts));
    }
}
