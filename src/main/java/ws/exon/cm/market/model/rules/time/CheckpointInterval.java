package ws.exon.cm.market.model.rules.time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckpointInterval {
    private long delay;
    private long period;
}
