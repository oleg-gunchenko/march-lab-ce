package ws.exon.cm.market.model.core.portfolio;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.threeten.extra.Interval;
import org.threeten.extra.PeriodDuration;
import ws.exon.cm.market.model.core.common.Asset;

import java.math.BigDecimal;

public interface IPosition {
    long getPlannedVolume();

    long getOpenedVolume();

    long getExposedVolume();

    State getState();

    Interval getTimeInterval();

    PnL getPnl();

    boolean isCloseOnly();

    enum State {NEW, OPENING, OPENED, CLOSING, CLOSED}

    @Data
    @AllArgsConstructor
    class PnL {
        public static final PnL ZERO = new PnL(null, PeriodDuration.ZERO, 0, 0, 0);

        private Asset asset;
        private PeriodDuration pd;
        private long fees;
        private long realized;
        private long opened;

        public BigDecimal getTotal() {
            return BigDecimal.valueOf(realized + opened - fees);
        }
    }
}
