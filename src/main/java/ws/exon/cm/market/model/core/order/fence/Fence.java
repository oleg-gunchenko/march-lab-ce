package ws.exon.cm.market.model.core.order.fence;

import lombok.Data;
import ws.exon.cm.market.model.core.order.Order;

import java.util.UUID;

@Data
public class Fence implements Comparable<Fence> {
    private UUID id;
    private long price;
    private Order order;
    private Placement placement;

    @Override
    public int compareTo(Fence o) {
        int result = Long.compare(price, o.price);
        if (result != 0)
            return result;
        return order.compareTo(o.order);
    }

    public enum Placement {LOCAL, REMOTE}
}
