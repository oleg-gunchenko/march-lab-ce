package ws.exon.cm.market.model.core.order;

import lombok.Data;
import ws.exon.cm.market.model.core.common.candle.Deal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Order implements Comparable<Order> {
    private UUID id;
    private String description;
    private State state;
    private Fulfillment fulfillment;
    private List<Trade> trades = new ArrayList<>();
    private long plannedQuantity;
    private Deal.Side side;
    private long blocked;

    private long executed() {
        if (trades.isEmpty())
            return 0;
        return trades.stream().map(t -> t.getDealLong().getLongVolume()).reduce(0L, Long::sum);
    }

    public long getFeesTotal() {
        return trades.stream().map(Trade::getFee).reduce(0L, Long::sum);
    }

    public Fulfillment getFulfillment() {
        if (trades.isEmpty())
            return Fulfillment.NONE;

        long qty = executed();
        if (plannedQuantity > qty)
            return Fulfillment.PARTIAL;

        return Fulfillment.FULL;
    }

    public long exposedQuantity() {
        return executed();
    }

    @Override
    public int compareTo(Order o) {
        return id.compareTo(o.id);
    }

    public enum State {NEW, OPENING, OPENED, COMPLETED, CANCELLING, CANCELED, FAILED}

    public enum Fulfillment {NONE, PARTIAL, FULL}
}
