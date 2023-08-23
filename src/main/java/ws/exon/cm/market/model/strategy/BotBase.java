package ws.exon.cm.market.model.strategy;

import lombok.RequiredArgsConstructor;
import org.kie.api.time.SessionClock;
import ws.exon.cm.market.model.core.portfolio.Portfolio;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class BotBase implements Runnable {
    private final UUID id = UUID.randomUUID();
    private final String name;
    private final Portfolio portfolio;
    private final DatasetManager datasetManager;
    private final AggregationManager aggregationManager;
    private final Strategy strategy;
    private final RiskManager rm;
    private final OrderManager orderManager;
    private final ExecutorService executorService;
    private SessionClock clock;

    @Override
    public void run() {

    }
}
