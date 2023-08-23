package ws.exon.cm.market.model.rules.range;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ws.exon.cm.market.model.DirectionClass;
import ws.exon.cm.market.model.core.cep.PriceLong;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.orderflow.aggregator.Aggregate;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PriceRangeCandle extends RangeCandle {
    private long series;

    public PriceRangeCandle(Symbol symbol, Aggregate aggregate, long threshold, int clusterWidth, PriceLong priceLong) {
        super(symbol, aggregate, threshold, clusterWidth, priceLong);
        series = 1;
    }

    public void setupRange(final PriceRangeCandle old, final long series, final int bodySize) {
        this.series = series + 1;
        long nr = bodySize * this.series;
        if (this.series >= 12)
            System.out.println(this.series);
        final DirectionClass ldir = old.classifyDirection();
        if (ldir == DirectionClass.BULL) {
            final long ub = old.getUpperBound();
            setUpperBound(ub + bodySize);
            setLowerBound(ub - nr);
        } else if (ldir == DirectionClass.BEAR) {
            final long lb = old.getLowerBound();
            setLowerBound(lb - bodySize);
            setUpperBound(lb + nr);
        }
    }
}
