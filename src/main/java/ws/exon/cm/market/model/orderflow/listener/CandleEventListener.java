package ws.exon.cm.market.model.orderflow.listener;

import ws.exon.cm.market.model.event.data.orderflow.NewCandleEvent;
import ws.exon.cm.market.model.event.data.orderflow.UpdateCandleEvent;
import ws.exon.cm.market.model.orderflow.OrderflowListener;

public interface CandleEventListener extends OrderflowListener {
    void onNewCandle(final NewCandleEvent event);

    void onUpdateCandle(final UpdateCandleEvent event);

    void onCompleteCandle(final UpdateCandleEvent event);
}
