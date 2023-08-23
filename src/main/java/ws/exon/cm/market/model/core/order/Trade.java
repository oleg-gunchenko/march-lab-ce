package ws.exon.cm.market.model.core.order;

import lombok.Data;
import ws.exon.cm.market.model.core.cep.DealLong;

@Data
public class Trade {
    private long id;
    private DealLong DealLong;
    private long fee;
}
