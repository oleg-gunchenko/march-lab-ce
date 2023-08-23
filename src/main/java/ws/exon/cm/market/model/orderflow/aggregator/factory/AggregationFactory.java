package ws.exon.cm.market.model.orderflow.aggregator.factory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ws.exon.cm.market.model.core.common.Symbol;

import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AggregationFactory {

    @ToString.Include(rank = 1)
    @EqualsAndHashCode.Include
    protected long threshold;
    protected long lastId;
    @EqualsAndHashCode.Include
    @ToString.Include(rank = -1)
    private Symbol symbol;
    @EqualsAndHashCode.Include
    @ToString.Include
    private int[] clusterWidth;
    private Set<Symbol> index;


    public AggregationFactory(final Symbol symbol, final Set<Symbol> index, final int[] clusterWidth, final long threshold) {
        this(symbol, clusterWidth, threshold);
        this.index = index;
    }

    public AggregationFactory(final Symbol symbol, final int[] clusterWidth, final long threshold) {
        this(symbol, clusterWidth);
        this.threshold = threshold;
    }

    public AggregationFactory(final Symbol symbol, final int[] clusterWidth) {
        this.symbol = symbol;
        this.clusterWidth = clusterWidth;
        this.index = Collections.singleton(symbol);
    }

    public AggregationFactory(final Symbol symbol) {
        this(symbol, new int[]{1});
    }

    public int cw() {
        return clusterWidth[0];
    }
}
