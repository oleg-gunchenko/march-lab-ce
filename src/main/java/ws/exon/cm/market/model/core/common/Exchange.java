package ws.exon.cm.market.model.core.common;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;

@Data
@RequiredArgsConstructor
public class Exchange {
    private final String name;
    private final String code;
    private final ZoneOffset zoneOffset;
}
