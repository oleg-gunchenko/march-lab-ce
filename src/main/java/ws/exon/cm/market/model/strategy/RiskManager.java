package ws.exon.cm.market.model.strategy;

import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.core.common.candle.Price;
import ws.exon.cm.market.model.core.order.fence.StopFence;
import ws.exon.cm.market.model.core.portfolio.Direction;
import ws.exon.cm.market.model.core.portfolio.IPosition;
import ws.exon.cm.market.model.core.portfolio.Portfolio;
import ws.exon.cm.market.model.core.portfolio.Position;

import java.math.BigDecimal;
import java.util.SortedSet;

public interface RiskManager {
    void bind(final Portfolio portfolio, final Strategy strategy);

    void bind(final Strategy strategy, final RiskStrategy riskStrategy);

    void unbind(final Portfolio portfolio, final Strategy strategy);

    void unbind(final Strategy strategy, final RiskStrategy riskStrategy);

    Position open(final Symbol symbol, final Direction direction, final Strategy strategy);

    void close(final BigDecimal percentage, final Position position);

    default void close(final Position position) {
        close(BigDecimal.ONE, position);
    }

    StopFence stop(final Position position, final Price price, final BigDecimal percentage);

    Position flip(final Position position, final Strategy strategy);

    SortedSet<Position> positions(final Strategy strategy, final IPosition.State state);

    default Position openLong(final Symbol symbol, final Strategy strategy) {
        return open(symbol, Direction.LONG, strategy);
    }

    default Position openShort(final Symbol symbol, final Strategy strategy) {
        return open(symbol, Direction.SHORT, strategy);
    }

    default SortedSet<Position> positions(final Strategy strategy) {
        return positions(strategy, null);
    }
}
