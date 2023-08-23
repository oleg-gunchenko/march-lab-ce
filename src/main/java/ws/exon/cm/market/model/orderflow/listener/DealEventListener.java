package ws.exon.cm.market.model.orderflow.listener;

import ws.exon.cm.market.model.event.data.orderflow.NewDealEvent;
import ws.exon.cm.market.model.orderflow.OrderflowListener;

public interface DealEventListener extends OrderflowListener {
    void onNewDeal(final NewDealEvent event);
}
