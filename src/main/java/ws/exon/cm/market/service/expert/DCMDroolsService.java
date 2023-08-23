package ws.exon.cm.market.service.expert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugProcessEventListener;
import org.drools.core.event.DebugRuleRuntimeEventListener;
import org.hashids.Hashids;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.time.SessionClock;
import org.kie.api.time.SessionPseudoClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ws.exon.cm.market.service.expert.channels.NotificationChannel;
import ws.exon.cm.market.service.expert.channels.ObjectStorageChannel;

import java.util.Collection;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DCMDroolsService {
    @Value("#{script}")

    private static final String salt =
            "WTHNi59UkQAMCTUXVjVz" +
                    "mL8q82iVdJqcUKNbUXIT" +
                    "uLRVS8iSKe7HJU533NcS" +
                    "qBc9A1AaTFQrffBVwdMb" +
                    "F8UlybL7U3dpVLb42Pj5" +
                    "SCApnLsrnlhWkCKKWVfq" +
                    "ERNHJtGMlxehK0igwyzW" +
                    "q5uG0wHQ5xiREwMXJDvr" +
                    "0MMOAmuQ7xn1sDQ0eGQZ" +
                    "CCkFlf2IICVSYcyd1epS";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final NotificationChannel notificationChannel;
    private final ObjectStorageChannel storageChannel;
    private final Hashids hashids = new Hashids(salt, 8);
    private Future<?> future;
    private KieContainer kieContainer;
    private KieSession session;


    private FactHandle insert(KieSession kieSession, String stream, Object fact) {
        //LOGGER.info("Inserting fact with id: '" + fact.getId() + "' into stream: " + stream);
        SessionClock clock = kieSession.getSessionClock();
        if (!(clock instanceof SessionPseudoClock)) {
            String errorMessage = "This fact inserter can only be used with KieSessions that use a SessionPseudoClock";
            //LOGGER.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        SessionPseudoClock pseudoClock = (SessionPseudoClock) clock;
        EntryPoint ep = kieSession.getEntryPoint(stream);

        // First insert the fact
        //LOGGER.debug("Inserting fact: " + fact.toString());
        FactHandle factHandle = ep.insert(fact);

        // And then advance the clock
        // We only need to advance the time when dealing with Events. Our facts don't have timestamps.
        return factHandle;
    }

    //@PostConstruct
    private void init() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        session = kieContainer.newKieSession();
        session.fireUntilHalt();
        Future<Integer> futureResult = service.submit((Callable<Integer>) session::fireAllRules);

        try {
            int numberOfFiredRules = futureResult.get(500, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            session.halt();
            futureResult.cancel(true);
            session.dispose();
        } catch (Exception e2) {
        } finally {
            service.shutdown();
        }

        session.registerChannel("store", storageChannel);
        session.registerChannel("notify", notificationChannel);
        session.addEventListener(new DebugRuleRuntimeEventListener());
        session.addEventListener(new DebugAgendaEventListener());
        session.addEventListener(new DebugProcessEventListener());
//        session.setGlobal("orderDiscount", orderDiscount);

        this.future = executor.submit(() -> session.fireUntilHalt());
    }

    public void insertDocument(final Object document) {
        session.insert(document);
    }

    public void insertDocument(final Collection<Object> documents) {
        session.submit(kieSession -> documents.forEach(kieSession::insert));
    }

    //    @PreDestroy
    private void destroy() throws InterruptedException, ExecutionException {
        executor.shutdown();
        session.halt();
        if (future != null)
            future.get();
        executor.awaitTermination(3, TimeUnit.MINUTES);
        session.dispose();
    }
}
