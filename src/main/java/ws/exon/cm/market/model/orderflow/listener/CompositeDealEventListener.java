package ws.exon.cm.market.model.orderflow.listener;

import ws.exon.cm.market.model.event.data.orderflow.NewCompositeDealEvent;

public interface CompositeDealEventListener {
    void onCompositeDeal(final NewCompositeDealEvent event);
}
