package ws.exon.cm.market.model.orderflow.aggregator.factory.range;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.orderflow.aggregator.meta.CandleFactory;
import ws.exon.cm.market.model.orderflow.aggregator.meta.FactoryClass;
import ws.exon.cm.market.model.orderflow.aggregator.meta.Indicator;

import java.util.Collections;
import java.util.Set;

@Component
@NoArgsConstructor
@CandleFactory(name = "PriceRange", type = FactoryClass.PRICE, indicator = Indicator.RANGE)
public class PriceRangeFactory extends PriceRFactory {
    public PriceRangeFactory(final Symbol symbol, final Set<Symbol> index, final int[] clusterWidth, final int threshold) {
        super(symbol, index, clusterWidth, threshold);
    }

    public PriceRangeFactory(final Symbol symbol, final int[] clusterWidth, final int threshold) {
        this(symbol, Collections.singleton(symbol), clusterWidth, threshold);
    }

    public PriceRangeFactory(final Symbol symbol, final int clusterWidth, final int threshold) {
        this(symbol, Collections.singleton(symbol), new int[]{clusterWidth}, threshold);
    }
}
