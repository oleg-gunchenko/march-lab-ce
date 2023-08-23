package ws.exon.cm.market.model.rules.time;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Role;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.orderflow.Candle;
import ws.exon.cm.market.model.orderflow.aggregator.Aggregate;

import static org.kie.api.definition.type.Role.Type.EVENT;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Role(EVENT)
public class TimeCandle extends Candle {
    public TimeCandle(final Symbol symbol, final long threshold, final long ts, final Aggregate aggregate) {
        super(symbol, threshold, aggregate);
        long tts = (long) ((double) ts / threshold);
        this.ts = tts * threshold;
    }

    @Override
    public long getDuration() {
        return threshold;
    }
}
