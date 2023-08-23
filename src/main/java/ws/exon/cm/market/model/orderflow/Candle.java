package ws.exon.cm.market.model.orderflow;

import lombok.*;
import org.kie.api.definition.type.Duration;
import org.kie.api.definition.type.Timestamp;
import ws.exon.cm.market.model.DirectionClass;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.orderflow.aggregator.Aggregate;

import java.util.SortedMap;
import java.util.TreeMap;

@Data
@Timestamp("ts")
@Duration("duration")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Candle {
    private final SortedMap<Long, Cluster> clusters = new TreeMap<>();
    @ToString.Include
    @EqualsAndHashCode.Include
    protected long threshold;
    @ToString.Include
    protected Aggregate aggregate;
    @ToString.Include
    @EqualsAndHashCode.Include
    protected long ts;
    @ToString.Include(rank = -1)
    @EqualsAndHashCode.Include
    private Symbol symbol;
    private State state = State.OPEN;

    public Candle(final Symbol symbol, final Aggregate aggregate, long threshold) {
        this.symbol = symbol;
        this.threshold = threshold;
        this.aggregate = aggregate;
    }

    public Candle(Symbol symbol, long threshold, Aggregate aggregate) {
        this.symbol = symbol;
        this.threshold = threshold;
        this.aggregate = aggregate;
    }

    public long getDuration() {
        return aggregate.duration();
    }

    public DirectionClass classifyDirection() {
        final long delta = aggregate.getPrice().priceDelta();
        if (delta == 0)
            return DirectionClass.DOJI;
        return delta > 0 ? DirectionClass.BULL : DirectionClass.BEAR;
    }

    public Aggregate getAggregate() {
        return aggregate;
    }

    public enum State {
        OPEN, CLOSED
    }
}
