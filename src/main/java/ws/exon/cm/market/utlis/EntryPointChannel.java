package ws.exon.cm.market.utlis;

import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.Channel;
import org.kie.api.runtime.rule.EntryPoint;

@RequiredArgsConstructor
public class EntryPointChannel implements Channel {
    private final EntryPoint point;

    @Override
    public void send(Object object) {
        point.insert(object);
    }
}
