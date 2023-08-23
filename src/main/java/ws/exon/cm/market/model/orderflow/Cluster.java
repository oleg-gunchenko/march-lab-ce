package ws.exon.cm.market.model.orderflow;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ws.exon.cm.market.model.core.cep.DealLong;
import ws.exon.cm.market.model.core.common.FPValue;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.orderflow.aggregator.Aggregate;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Cluster {
    private final Symbol symbol;
    private final Candle candle;
    private final Aggregate aggregate;
    private final FPValue price;
    private final int width;

    private Cluster(final BigDecimal price, final int width, final Candle candle, final DealLong deal) {
        this.candle = candle;
        this.symbol = candle.getSymbol();
        this.aggregate = new Aggregate(UUID.randomUUID(), deal);
        this.price = new FPValue(price, symbol.getPricePrecision());
        this.width = width;
    }
}
