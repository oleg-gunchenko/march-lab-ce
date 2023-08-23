package ws.exon.cm.market.utlis;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.Channel;

import java.util.Collection;
import java.util.LinkedList;

@Data
@RequiredArgsConstructor
public class ListChannel implements Channel {
    private final Collection<Object> objects;

    public ListChannel() {
        this(new LinkedList<>());
    }

    @Override
    public void send(final Object object) {
        objects.add(object);
    }
}
