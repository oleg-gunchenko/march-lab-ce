package ws.exon.cm.market.model.core.cep;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import ws.exon.cm.market.model.core.common.candle.Price;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@EqualsAndHashCode
public class PriceLong implements Price {
    private final long ts;
    private final long lValue;
    private final int precision;
    private final Instant timestamp;
    private final BigDecimal value;

    public PriceLong(final long ts, long value, int precision) {
        this.ts = ts;
        this.lValue = value;
        this.precision = precision;
        this.timestamp = Instant.ofEpochMilli(ts);
        this.value = BigDecimal.valueOf(value).movePointLeft(precision);
    }

    public PriceLong(final long ts, final BigDecimal value, int precision) {
        this.ts = ts;
        this.lValue = value.movePointRight(precision).longValue();
        this.precision = precision;
        this.timestamp = Instant.ofEpochMilli(ts);
        this.value = value;
    }

    public PriceLong(final Instant ts, long value, int precision) {
        this.timestamp = ts;
        this.lValue = value;
        this.precision = precision;
        this.ts = ts.toEpochMilli();
        this.value = BigDecimal.valueOf(value).movePointLeft(precision);
    }

    public PriceLong(final Instant ts, final BigDecimal value, int precision) {
        this.timestamp = ts;
        this.lValue = value.movePointRight(precision).longValue();
        this.precision = precision;
        this.ts = ts.toEpochMilli();
        this.value = value;
    }

    public Instant getTimestamp() {
        return Instant.ofEpochMilli(ts);
    }

    @Override
    public long delta(Price other) {
        if (other instanceof PriceLong)
            return ((PriceLong) other).lValue - lValue;
        return other.getValue().longValue() - lValue;
    }

    private int compareInternal(PriceLong o) {
        final int result = Long.compare(this.ts, o.getTs());
        return result >= 0 ? Long.compare(this.lValue, o.lValue) : result;
    }

    @Override
    public int compareTo(@NonNull final Price o) {
        if (o instanceof PriceLong)
            return compareInternal((PriceLong) o);
        return this.compareTo(o);
    }
}
