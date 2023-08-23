package ws.exon.cm.market.model.core.common.candle;

import java.math.BigDecimal;
import java.time.Instant;

public interface Price extends Comparable<Price> {
    BigDecimal getValue();

    Instant getTimestamp();

    long getTs();

    long delta(Price other);
}
