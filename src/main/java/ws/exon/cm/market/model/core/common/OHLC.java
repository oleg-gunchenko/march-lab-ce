package ws.exon.cm.market.model.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import ws.exon.cm.market.model.core.common.candle.Price;

@Data
@AllArgsConstructor
public class OHLC {
    private final Price open;
    private Price close;
    private Price high;
    private Price low;

    public OHLC(final Price open) {
        this.open = this.high = this.low = this.close = open;
    }

    public long priceDelta() {
        return close.delta(open);
    }

    public void evaluate(final Price close) {
        if (close.compareTo(high) > 0) {
            high = close;
            return;
        }
        if (close.compareTo(low) < 0)
            low = close;
        this.close = close;
    }
}
