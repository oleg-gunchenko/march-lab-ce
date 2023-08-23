package ws.exon.cm.market.utlis;

import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.Channel;

@RequiredArgsConstructor
public class ChainedChannel implements Channel {
    private final Channel a;
    private final Channel b;

    @Override
    public void send(Object object) {
        a.send(object);
        b.send(object);
    }
}
