package ws.exon.cm.market.model.core.common.candle;

import ws.exon.cm.market.model.core.common.Symbol;

import java.math.BigDecimal;

public interface Deal {
    long getId();

    Price getPrice();

    Symbol getSymbol();

    Side getSide();

    BigDecimal getVolume();

    enum Side {BUY, SELL, NONE}
}
