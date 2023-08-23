package ws.exon.cm.market.model.core.common;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Symbol implements Comparable<Symbol> {
    @EqualsAndHashCode.Include
    private UUID id;
    private String code;

    private String exchange = StringUtils.EMPTY;
    private String market = StringUtils.EMPTY;
    private String instrument = StringUtils.EMPTY;


    private BigDecimal priceStepSize;
    private BigDecimal priceStepCost;
    private int pricePrecision;

    private BigDecimal minVolumeSize;
    private int volumePrecision;

    private int oiPrecision;

    private static String formatCode(final String exchange, final String market, final String instrument) {
        return String.format("symbol://%s/%s/%s", exchange, market, instrument);
    }

    public static UUID symbolId(final String exchange, final String market, final String instrument) {
        return UUID.nameUUIDFromBytes(formatCode(exchange, market, instrument).getBytes(StandardCharsets.UTF_8));
    }

    public static Symbol fromString(final String text) {
        final String[] parts = text.split(":");
        return create(parts[1], parts[2], parts[0]);
    }

    public static Symbol create(final String exchange, final String market, final String instrument) {
        return builder().exchange(exchange).market(market).instrument(instrument).build();
    }

    public UUID getId() {
        if (id == null)
            id = symbolId(exchange, market, instrument);
        return id;
    }

    public String getCode() {
        if (code == null)
            code = formatCode(exchange, market, instrument);
        return code;
    }

    public String format(final String fmt) {
        return String.format(fmt, instrument, exchange, market).toUpperCase();
    }

    public String toString() {
        return format("%s:%s:%s");
    }

    @Override
    public int compareTo(final Symbol o) {
        return id.compareTo(o.id);
    }
}
