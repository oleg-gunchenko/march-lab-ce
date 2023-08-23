package ws.exon.cm.market.model.core.common.candle;

import java.math.BigDecimal;
import java.time.Duration;

public interface Candle {
    Duration getDuration();

    Price getOpenPrice();

    Price getHighPrice();

    Price getLowPrice();

    Price getClosePrice();

    BigDecimal getVolume();
}
