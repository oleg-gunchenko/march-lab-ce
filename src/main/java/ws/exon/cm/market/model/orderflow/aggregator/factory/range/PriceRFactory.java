package ws.exon.cm.market.model.orderflow.aggregator.factory.range;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ws.exon.cm.market.model.DirectionClass;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.orderflow.aggregator.factory.AggregationFactory;

import java.util.Set;

@Data
@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PriceRFactory extends AggregationFactory {
    private int bodySize;
    private long series;
    private DirectionClass lastDirection;

    public PriceRFactory(final Symbol symbol, final Set<Symbol> index, final int[] clusterWidth, final int threshold) {
        super(symbol, index, clusterWidth, threshold);
        this.bodySize = threshold * cw();
    }
}
