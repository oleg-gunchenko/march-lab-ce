package ws.exon.cm.market.model.core.cep;

import java.math.BigDecimal;

public class DecimalUtils {
    public static BigDecimal convert(final Object o) {
        if (o instanceof String)
            return new BigDecimal((String) o);
        else if (o instanceof BigDecimal)
            return (BigDecimal) o;
        else if (o instanceof Long)
            return BigDecimal.valueOf((Long) o);

        throw new IllegalArgumentException();
    }
}
