package ws.exon.cm.market.model.orderflow.aggregator.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CandleFactory {
    String name();

    FactoryClass type();

    Indicator indicator();
}
