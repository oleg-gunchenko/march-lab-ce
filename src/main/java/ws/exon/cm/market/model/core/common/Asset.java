package ws.exon.cm.market.model.core.common;

import lombok.Data;
import ws.exon.cm.market.model.core.common.candle.Price;

import java.time.LocalDate;

@Data
public class Asset {
    private Price price;

    public Price getPrice(LocalDate toLocalDate) {
        return price;
    }
}
