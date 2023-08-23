package ws.exon.cm.market.model.core.cep;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import ws.exon.cm.market.model.core.common.FPValue;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.core.common.candle.Deal;
import ws.exon.cm.market.model.core.common.candle.Price;

import java.math.BigDecimal;

@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Role(Role.Type.EVENT)
@Timestamp("ts")
@Expires("1s")
public class DealLong implements Deal {
    @ToString.Include
    @EqualsAndHashCode.Include
    private long id;

    @ToString.Include(rank = -1)
    private Symbol symbol;

    @ToString.Include
    private Side side;

    @ToString.Include(rank = 2)
    private Price price;
    private FPValue volume;
    @ToString.Include
    private long longVolume;
    private long openInterest;

    public DealLong(long id, final Symbol symbol, final Price price, final Side side, final FPValue volume, long openInterest) {
        this.id = id;
        this.symbol = symbol;
        this.side = side;
        this.price = price;
        this.volume = volume;
        this.openInterest = openInterest;
    }

    public DealLong(long id, final Symbol symbol, final Price price, final Side side, final BigDecimal volume, int volumePrecision, long openInterest) {
        this(id, symbol, price, side, new FPValue(volume, volumePrecision), openInterest);
    }

    public DealLong(long id, final Symbol symbol, final Price price, final Side side, long volume, int volumePrecision, long openInterest) {
        this(id, symbol, price, side, new FPValue(volume, volumePrecision), openInterest);
    }

    public DealLong(long id, final Symbol symbol, final Price price, final Side side, final BigDecimal volume, int volumePrecision) {
        this(id, symbol, price, side, new FPValue(volume, volumePrecision), 0);
    }

    public DealLong(long id, final Symbol symbol, final Price price, final Side side, long volume, int volumePrecision) {
        this(id, symbol, price, side, new FPValue(volume, volumePrecision), 0);
    }

    public DealLong(long id, Symbol symbol, Price price) {
        this(id, symbol, price, Side.NONE, FPValue.ZERO, 0);
    }

    public static Deal fromString(final String line, final Symbol symbol, int pricePrecision, int volumePrecision) {
        final String[] parts = line.split(":");
        final long ts = Long.parseLong(parts[1]);
        final long price = Long.parseLong(parts[2]);
        final PriceLong matchPriceLong = new PriceLong(price, ts, pricePrecision);
        final long id = Long.parseLong(parts[0]);

        if (parts.length == 4)
            return new DealLong(id, symbol, matchPriceLong);

        final Side side = parts[3].equals("B") ? Side.BUY : Side.SELL;
        final long volume = Long.parseLong(parts[4]);
        if (parts.length == 5)
            return new DealLong(id, symbol, matchPriceLong, side, volume, volumePrecision);

        final long oi = Long.parseLong(parts[5]);
        return new DealLong(id, symbol, matchPriceLong, side, volume, volumePrecision, oi);
    }

    //    @ToString.Include(name = "ts")
    public long getTs() {
        return price.getTs();
    }

    public long priceDiff(final Price price) {
        return Math.abs(this.price.delta(price));
    }

    public BigDecimal getVolume() {
        return volume.getDecimal();
    }

    public Price getPrice() {
        return price;
    }
}
