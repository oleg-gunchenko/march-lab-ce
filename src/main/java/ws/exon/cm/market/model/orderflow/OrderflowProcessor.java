package ws.exon.cm.market.model.orderflow;

import ws.exon.cm.market.model.orderflow.listener.DealEventListener;

import java.util.Set;

public interface OrderflowProcessor extends DealEventListener {
    void initialize();

    Set<String> getOrderflowEvents();

    void registerCallback(final String channel, final OrderflowListener consumer);
}
