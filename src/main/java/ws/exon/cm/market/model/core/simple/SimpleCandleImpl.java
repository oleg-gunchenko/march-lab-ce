package ws.exon.cm.market.model.core.simple;

import ws.exon.cm.market.model.core.common.OHLC;
import ws.exon.cm.market.model.core.common.candle.Candle;
import ws.exon.cm.market.model.core.common.candle.Price;

import java.math.BigDecimal;
import java.time.Duration;

public class SimpleCandleImpl implements Candle {
    private final OHLC ohlc;
    private final BigDecimal volume;


    public SimpleCandleImpl(final OHLC ohlc, final BigDecimal volume) {
        this.ohlc = ohlc;
        this.volume = volume;
    }

    @Override
    public Price getOpenPrice() {
        return ohlc.getOpen();
    }

    @Override
    public Price getHighPrice() {
        return ohlc.getHigh();
    }

    @Override
    public Price getLowPrice() {
        return ohlc.getLow();
    }

    @Override
    public Price getClosePrice() {
        return ohlc.getClose();
    }

    @Override
    public BigDecimal getVolume() {
        return volume;
    }

    @Override
    public Duration getDuration() {
        return Duration.between(getOpenPrice().getTimestamp(), getClosePrice().getTimestamp());
    }
}
