package ws.exon.cm.market.model.rules.time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Data
@Timestamp("ts")
@Expires("5ms")
@NoArgsConstructor
@AllArgsConstructor
@Role(Role.Type.EVENT)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DurationCheckpoint {
    @EqualsAndHashCode.Include
    private long threshold;

    private long ts;

    public DurationCheckpoint(long threshold) {
        this.threshold = threshold;
    }
}
