package ws.exon.cm.market.model.rules;

import lombok.*;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import ws.exon.cm.market.model.core.cep.DealLong;
import ws.exon.cm.market.model.core.common.Symbol;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Role(Role.Type.EVENT)
@Timestamp("ts")
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CumulativeData {
    @ToString.Include
    private DealLong deal;
    @EqualsAndHashCode.Include
    private Symbol symbol;
    private long delta;
    private long oi;
    private long sessionDelta;
    private long sessionOI;

    public CumulativeData(final Symbol symbol, final DealLong deal) {
        this.symbol = symbol;
        this.deal = deal;
    }

    public long getTs() {
        return deal.getTs();
    }
}
