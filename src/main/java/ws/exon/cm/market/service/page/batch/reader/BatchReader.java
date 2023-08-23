package ws.exon.cm.market.service.page.batch.reader;

import lombok.RequiredArgsConstructor;
import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.service.page.batch.DatasetReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class BatchReader<T extends Event> implements DatasetReader<T> {
    private final Iterator<T> iterator;
    private final List<T> data;
    private final int threshold;

    public BatchReader(final Iterator<T> iterator, final int threshold) {
        this.iterator = iterator;
        this.data = new ArrayList<>(threshold);
        this.threshold = threshold;
    }

    @Override
    public List<T> call() {
        data.clear();
        if (!iterator.hasNext())
            return data;
        for (int i = 0; i < threshold && iterator.hasNext(); i++)
            data.add(iterator.next());
        return data;
    }
}
