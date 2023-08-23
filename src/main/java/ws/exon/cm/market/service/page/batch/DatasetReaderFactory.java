package ws.exon.cm.market.service.page.batch;

import ws.exon.cm.market.model.event.Event;

public interface DatasetReaderFactory<T extends Event> {
    int getThreshold();
}
