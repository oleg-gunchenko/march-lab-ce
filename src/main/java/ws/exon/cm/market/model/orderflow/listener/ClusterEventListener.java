package ws.exon.cm.market.model.orderflow.listener;

import ws.exon.cm.market.model.event.data.orderflow.NewClusterEvent;
import ws.exon.cm.market.model.event.data.orderflow.UpdateClusterEvent;
import ws.exon.cm.market.model.orderflow.OrderflowListener;

public interface ClusterEventListener extends OrderflowListener {
    void onNewCluster(final NewClusterEvent event);

    void onUpdateCluster(final UpdateClusterEvent event);

    void onCompleteCluster(final UpdateClusterEvent event);
}
