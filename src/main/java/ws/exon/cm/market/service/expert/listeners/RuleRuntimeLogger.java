package ws.exon.cm.market.service.expert.listeners;

import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;

public class RuleRuntimeLogger implements RuleRuntimeEventListener {
    @Override
    public void objectInserted(ObjectInsertedEvent event) {

    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent event) {

    }

    @Override
    public void objectDeleted(ObjectDeletedEvent event) {

    }
}
