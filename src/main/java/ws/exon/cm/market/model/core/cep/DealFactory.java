package ws.exon.cm.market.model.core.cep;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.core.common.candle.Deal;
import ws.exon.cm.market.model.core.common.candle.Price;

import java.time.Instant;

import static ws.exon.cm.market.model.core.cep.DecimalUtils.convert;

@Data
@RequiredArgsConstructor
public class DealFactory {
    private final Symbol symbol;
    private final int volumePrecision;
    private final PriceFactory priceFactory;

    public DealFactory(final Symbol symbol, int pricePrecision, int volumePrecision) {
        this(symbol, volumePrecision, new PriceFactory(pricePrecision));
    }

    public Deal instantiate(final long id, final long ts, final Object price, final Deal.Side side, final Object volume, final long oi) {
        final Price p = priceFactory.instantiate(ts, price);
        return new DealLong(id, symbol, side, p, convert(volume), volumePrecision, oi);
    }

    public Deal instantiate(final long id, final Instant ts, final Object price, final Deal.Side side, final Object volume, final int oi) {
        final Price p = priceFactory.instantiate(ts, price);
        return new DealLong(id, symbol, side, p, convert(volume), volumePrecision, oi);
    }

    public Deal instantiate(final long id, final Instant ts, final Object price) {
        return instantiate(id, ts, price, Deal.Side.NONE, 0L, 0);
    }

    public Deal instantiate(final long id, final long ts, final Object price) {
        return instantiate(id, ts, price, Deal.Side.NONE, 0L, 0);
    }
}
