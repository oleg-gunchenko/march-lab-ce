package ws.exon.cm.market.model.core.common;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class FPValue {
    public static final FPValue ZERO = new FPValue(0, 0);
    private final int precision;
    private long val;
    private BigDecimal value;

    public FPValue(final long value, final int precision) {
        this(precision);
        setLong(value);
    }

    public FPValue(final BigDecimal value, final int precision) {
        this(precision);
        setDecimal(value);
    }

    public long getLong() {
        return val;
    }

    public void setLong(final long val) {
        this.value = BigDecimal.valueOf(val).movePointLeft(precision);
        this.val = val;
    }

    public BigDecimal getDecimal() {
        return value;
    }

    public void setDecimal(@NonNull final BigDecimal val) {
        this.val = val.movePointRight(precision).longValue();
        this.value = val;
    }
}
