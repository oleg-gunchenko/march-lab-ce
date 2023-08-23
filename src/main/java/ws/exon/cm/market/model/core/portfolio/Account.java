package ws.exon.cm.market.model.core.portfolio;

import lombok.Data;
import ws.exon.cm.market.model.core.common.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Account {
    public UUID id;

    private Symbol base;
    private BigDecimal funding;
    private BigDecimal locked;
}
