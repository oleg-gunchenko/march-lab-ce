package ws.exon.cm.market.service.expert.channels;

import org.kie.api.runtime.Channel;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

public class ChainedSessionChannel implements Channel {
    private final EntryPoint ep;

    public ChainedSessionChannel(final KieSession session) {
        this(session, null);
    }

    public ChainedSessionChannel(final KieSession session, final String name) {
        final EntryPoint ep = name != null ? session.getEntryPoint(name) : session;
        this.ep = ep != null ? ep : session;
    }

    @Override
    public void send(Object object) {
        this.ep.insert(object);
    }
}
