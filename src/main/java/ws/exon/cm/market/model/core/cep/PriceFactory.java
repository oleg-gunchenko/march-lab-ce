package ws.exon.cm.market.model.core.cep;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ws.exon.cm.market.model.core.common.candle.Price;

import java.time.Instant;
import java.util.Objects;

import static ws.exon.cm.market.model.core.cep.DecimalUtils.convert;

@Data
@RequiredArgsConstructor
public class PriceFactory {
    private final int precision;

    public Price instantiate(final long ts, final Object o) {
        return new PriceLong(ts, Objects.requireNonNull(convert(o)), precision);
    }

    public Price instantiate(final Instant ts, final Object o) {
        return new PriceLong(ts, Objects.requireNonNull(convert(o)), precision);
    }
}
