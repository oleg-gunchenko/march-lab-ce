package ws.exon.cm.market.model.core.order.fence;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TakeFence extends Fence {
    public enum TakeFenceType {PRICE, YIELD}
}
