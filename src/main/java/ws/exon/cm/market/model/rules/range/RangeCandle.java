package ws.exon.cm.market.model.rules.range;

import lombok.*;
import ws.exon.cm.market.model.core.cep.PriceLong;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.orderflow.Candle;
import ws.exon.cm.market.model.orderflow.aggregator.Aggregate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class RangeCandle extends Candle {
    protected int clusterWidth;
    protected long upperBound;
    protected long lowerBound;

    public RangeCandle(Symbol symbol, Aggregate aggregate, long threshold, int clusterWidth) {
        super(symbol, aggregate, threshold);
        this.clusterWidth = clusterWidth;
    }

    public RangeCandle(Symbol symbol, long threshold, Aggregate aggregate, int clusterWidth) {
        super(symbol, threshold, aggregate);
        this.clusterWidth = clusterWidth;
    }

    public RangeCandle(Symbol symbol, Aggregate aggregate, long threshold, int clusterWidth, final PriceLong priceLong) {
        this(symbol, aggregate, threshold, clusterWidth);
        this.ts = priceLong.getTs();
        resetBounds(priceLong.getLValue());
    }

    public long getDuration() {
        return aggregate.duration();
    }

    public void resetBounds(final long price) {
        final long currentClusterLowerBound = ((long) ((double) price / clusterWidth)) * clusterWidth;
        final long boundary = (threshold - 1) * clusterWidth;
        this.lowerBound = currentClusterLowerBound - boundary;
        this.upperBound = currentClusterLowerBound + 2 * boundary;
    }
}
