package ws.exon.cm.market.clr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.time.SessionPseudoClock;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.core.common.candle.Deal;
import ws.exon.cm.market.model.event.data.orderflow.NewDealEvent;
import ws.exon.cm.market.model.orderflow.OrderFlowTask;
import ws.exon.cm.market.model.orderflow.aggregator.factory.AggregationFactory;
import ws.exon.cm.market.model.orderflow.aggregator.factory.CumulativeDealFactory;
import ws.exon.cm.market.model.orderflow.aggregator.factory.DurationFactory;
import ws.exon.cm.market.model.orderflow.aggregator.factory.range.PriceRangeFactory;
import ws.exon.cm.market.service.DroolsManagerService;
import ws.exon.cm.market.service.page.PageReader;
import ws.exon.cm.market.service.page.file.csv.StringPageReader;
import ws.exon.cm.market.utlis.DroolsUtil;
import ws.exon.cm.market.utlis.ListChannel;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineTestRunner implements CommandLineRunner {
    public static final String DEALS_BTC_FULL = "test-deals/BTC.full.csv";
    private final static Symbol BTC = Symbol.create("BINANCE", "FUTURES_USDM", "BTCUSDT");
    final static AggregationFactory AF_1 = new DurationFactory(BTC, new int[]{10}, 60000);
    final static AggregationFactory AF_5 = new DurationFactory(BTC, new int[]{10}, 5000);
    final static AggregationFactory AF_10 = new DurationFactory(BTC, new int[]{10}, 10000);
    final static AggregationFactory AF_20 = new DurationFactory(BTC, new int[]{10}, 20000);
    final static AggregationFactory AF_30 = new DurationFactory(BTC, new int[]{10}, 30000);
    final static AggregationFactory CD = new CumulativeDealFactory(BTC);
    final static AggregationFactory R_AF210 = new PriceRangeFactory(BTC, 1000, 2);
    private static final String[] AGGREGATES = new String[]{"base.drl",
//            "time-duration.drl",
//            "composite-deals.drl"
//            "price-range.drl",
    };
    private static final Set<?> DUR_AF1 = new HashSet<>(Arrays.asList(CD, AF_1));
    private static final Set<?> DUR_AF = new HashSet<>(Arrays.asList(AF_1, AF_5, AF_10, AF_20, AF_30));
    private static final Set<?> DDUR_AF = new HashSet<>(Arrays.asList(CD, AF_1, AF_5, AF_10, AF_20, AF_30));
    private static final Set<?> D_AF = new HashSet<>(Arrays.asList(CD));
    private static final Set<?> R_AF = new HashSet<>(Arrays.asList(CD, R_AF210));
    // private final DroolsManagerService dms;
    private final DroolsManagerService dms;
    private final DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    private final LocalDate date = LocalDate.parse("2022-01-01", f);
    private final int[] BTC_PRECISION = new int[]{2, 3};
    private final ApplicationContext context;
    private PageReader.Factory<NewDealEvent> prf;

    @PostConstruct
    public void init() {
        prf = new DealAvroPageReader.Factory(template, 500);
    }

    private void loadBtcBinance() throws InterruptedException {
        final File rootDir = new File("/Users/ogunchenko/dev/data/trading/btcusdt_bnce/trades/");
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        final Instant start = Instant.now();
        for (int i = 0; i < 4; i++) {
            final LocalDate dt = date.plusDays(i);
            executor.submit(new BinancePageStorage(BTC, dt, BTC_PRECISION, true, rootDir, prf));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
        final Duration duration = Duration.between(start, Instant.now());
        System.out.println(duration);
    }

    private void processPage() {
        final PageReader pr = new DealAvroPageReader.Factory(template, 500).create(BTC, date);
        List<Deal> deals = pr.getDeals(null);
        deals = pr.getDeals(deals);
    }

    private void loadBTCExonPage() throws IOException {
        final File rootDir = new File("/Users/ogunchenko/dev/data/market/trades");
        final DealAvroPageStorage tsp = new BinancePageStorageAvro(BTC, date, BTC_PRECISION, true, rootDir, prf);
        tsp.loadPage(tsp.getSymbolFile());
    }

    private void storeExonPage() throws IOException {
        final File rootDir = new File("/Users/ogunchenko/dev/data/market/BTC.csv.gz");
        final DealAvroPageStorage tsp = new ExonPageStorageAvro(BTC, date, BTC_PRECISION, true, rootDir, prf);
        tsp.storePage(rootDir);
    }

    private void testContainerCreation(final ReleaseId releaseId) {
        final KieContainer container = dms.getKieContainer(releaseId);
        final KieSession defaultSession = container.newKieSession();
        if (defaultSession.getSessionClock() instanceof SessionPseudoClock)
            System.out.println("pseudo");
        else
            System.out.println("realtime");
        final KieSession rtSession = container.newKieSession(DroolsUtil.getRealTimeSessionName(releaseId));
        if (rtSession.getSessionClock() instanceof SessionPseudoClock)
            System.out.println("pseudo");
        else
            System.out.println("realtime");
        final KieSession btSession = container.newKieSession(DroolsUtil.getBackTestSessionName(releaseId));
        final SessionPseudoClock clock = btSession.getSessionClock();
        if (!(clock instanceof SessionPseudoClock))
            System.out.println("realtime");
        else
            System.out.println("pseudo");
    }

    private Collection<Object> getChannelData(final KieSession session, final String channel) {
        final ListChannel lc = (ListChannel) session.getChannels().get(channel);
        return lc.getObjects();
    }

    @Override
    public void run(final String... args) throws Exception {
        final ReleaseId releaseId = dms.createReleaseId("ws.exon.ecm.trade", "OrderFlow", "1.0");
        dms.loadRules(releaseId, "aggregator", AGGREGATES);
//        testContainerCreation(releaseId);
//        loadBtcBinance();
//        storeExonPage();


        final PageReader.PageReaderFactory rf = new StringPageReader.ClasspathFactory(DEALS_BTC_FULL, BTC_PRECISION, 2000000);
        final Duration duration = Duration.ofDays(1);
        final OrderFlowTask factory = context.getBean(OrderFlowTask.class);
//        factory.setDebug(true);
        final KieSession session = factory.init(RXV_AF, date, duration, "ws.exon.ecm.trade:OrderFlow:1.0", rf);

        final Queue<Object> q = new ConcurrentLinkedQueue<>();


        session.registerChannel("CompositeDeals", new ListChannel());
        session.registerChannel("Clusters", new ListChannel());
        session.registerChannel("Candles", new ListChannel());
        session.registerChannel("Gaps", new ListChannel());

        final Collection<Object> candles = getChannelData(session, "Candles");
        final Collection<Object> deals = getChannelData(session, "CompositeDeals");
        final Collection<Object> clusters = getChannelData(session, "Clusters");
        final Collection<Object> gaps = getChannelData(session, "Gaps");

        final Collection<?> defaultObjects = session.getObjects();
        final Collection<?> mdObjects = session.getEntryPoint("Market Data/Trades").getObjects();

        session.insert(GF);

        final ExecutorService executor = Executors.newFixedThreadPool(10);
        final long start = System.currentTimeMillis();
//        executor.submit(factory);
//        executor.shutdown();
        factory.run();
        final boolean result = executor.awaitTermination(1, TimeUnit.DAYS);
        final long dt = System.currentTimeMillis() - start;

        System.out.println("zz: " + dt);
    }
}
