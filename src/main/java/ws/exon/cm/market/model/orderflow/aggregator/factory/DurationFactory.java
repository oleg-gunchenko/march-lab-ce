package ws.exon.cm.market.model.orderflow.aggregator.factory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.orderflow.aggregator.meta.CandleFactory;
import ws.exon.cm.market.model.orderflow.aggregator.meta.FactoryClass;
import ws.exon.cm.market.model.orderflow.aggregator.meta.Indicator;

import java.util.Collections;
import java.util.Set;

@Data
@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CandleFactory(name = "TimeDuration", type = FactoryClass.DURATION, indicator = Indicator.TIME)
public class DurationFactory extends AggregationFactory {
    public DurationFactory(Symbol symbol, Set<Symbol> index, int[] clusterWidth, int threshold) {
        super(symbol, index, clusterWidth, threshold);
    }

    public DurationFactory(Symbol symbol, int[] clusterWidth, int threshold) {
        this(symbol, Collections.singleton(symbol), clusterWidth, threshold);
    }
}
