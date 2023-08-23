package ws.exon.cm.market.model.strategy;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class MarketFee {
    private final BigDecimal maker;
    private final BigDecimal taker;
    private final int precision;
}
