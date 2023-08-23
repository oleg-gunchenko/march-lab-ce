package ws.exon.cm.market.service.page.batch;

import ws.exon.cm.market.model.event.Event;

import java.util.List;
import java.util.concurrent.Callable;

public interface DatasetReader<T extends Event> extends Callable<List<T>> {
}
