package ws.exon.cm.market.service.page.batch;

import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.service.page.batch.reader.BatchReader;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class CountBatchDataset<T extends Event> extends BatchDataset<T> {
    public CountBatchDataset(final ExecutorService executorService, final int batchSize) {
        super(executorService, batchSize);
    }

    @Override
    public Iterator<List<T>> iterator() {
        final Iterator<List<T>> it = new DatasetIterator(executorService) {
            @Override
            protected DatasetReader<T> getReader(final Iterator<T> it) {
                return new BatchReader<>(it, threshold);
            }
        };
        this.iterators.add(it);
        return it;
    }
}
