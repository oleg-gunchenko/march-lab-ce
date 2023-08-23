package ws.exon.cm.market.service.page.batch;

import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.service.page.batch.reader.TimeWindowReader;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class TimeBatchDataset<T extends Event> extends BatchDataset<T> {
    public TimeBatchDataset(ExecutorService executorService, int windowSize) {
        super(executorService, windowSize);
    }

    @Override
    public Iterator<List<T>> iterator() {
        final Iterator<List<T>> it = new DatasetIterator(executorService) {
            @Override
            protected DatasetReader<T> getReader(final Iterator<T> it) {
                return new TimeWindowReader<>(it, threshold);
            }
        };
        this.iterators.add(it);
        return it;
    }
}
