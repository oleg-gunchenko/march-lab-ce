package ws.exon.cm.market.model.orderflow.aggregator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.kie.api.definition.type.Duration;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import ws.exon.cm.market.model.DirectionClass;
import ws.exon.cm.market.model.core.cep.DealLong;
import ws.exon.cm.market.model.core.cep.PriceLong;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.core.common.candle.Price;

@Data
@Role(Role.Type.EVENT)
@Timestamp("ts")
@Duration("closeDuration")
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PriceJump {
    @ToString.Include(rank = -1)
    @EqualsAndHashCode.Include
    private final Symbol symbol;

    @ToString.Include
    private final long threshold;

    private final Price first;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final long firstDealId;
    private final long ts;
    private final Price second;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final long secondDealId;

    private final long size;
    private final long openDuration;
    private final DirectionClass dir;
    private long closeDepth;
    private Price close;
    private long closeDealId;
    private long closeDuration;
    private long lastDealId;

    public PriceJump(final DealLong first, final DealLong second, final long threshold) {
        this.symbol = first.getSymbol();
        this.threshold = threshold;
        this.first = first.getPrice();
        this.second = second.getPrice();
        this.firstDealId = first.getId();
        this.secondDealId = second.getId();
        this.ts = this.first.getTs();
        this.openDuration = second.getTs() - this.ts;
        final long fp = ((PriceLong) this.first).getLValue();
        final long sp = ((PriceLong) this.second).getLValue();
        this.size = Math.abs(fp - sp);
        this.dir = fp < sp ? DirectionClass.BULL : DirectionClass.BEAR;
    }

    public boolean isClosed() {
        return closeDepth >= size;
    }
}
