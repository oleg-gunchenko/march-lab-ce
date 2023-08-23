package ws.exon.cm.market.model.orderflow.aggregator.factory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.orderflow.aggregator.meta.CandleFactory;
import ws.exon.cm.market.model.orderflow.aggregator.meta.FactoryClass;
import ws.exon.cm.market.model.orderflow.aggregator.meta.Indicator;

@Data
@Component
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CandleFactory(name = "CumulativeDeal", type = FactoryClass.DEAL, indicator = Indicator.CUMULATIVE)
public class CumulativeDealFactory extends AggregationFactory {
    public CumulativeDealFactory(Symbol symbol) {
        super(symbol);
    }
}
