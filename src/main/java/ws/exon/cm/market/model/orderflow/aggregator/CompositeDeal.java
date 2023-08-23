package ws.exon.cm.market.model.orderflow.aggregator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import ws.exon.cm.market.model.core.common.Symbol;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Role(Role.Type.EVENT)
@Timestamp("ts")
@Expires("2s")
public class CompositeDeal {
    @ToString.Include
    @EqualsAndHashCode.Include
    private long openId;
    @ToString.Include
    @EqualsAndHashCode.Include
    private long closeId;
    @ToString.Include(rank = -1)
    @EqualsAndHashCode.Include
    private Symbol symbol;
    private long openPrice;
    private long closePrice;
    @ToString.Include(rank = 10)
    private long ts;
    private int direction;

    private long volume;
    private long oi;
    private long openOI;
    private long closeOI;

    private boolean sent;
}
