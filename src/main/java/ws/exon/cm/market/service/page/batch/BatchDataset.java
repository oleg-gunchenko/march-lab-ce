package ws.exon.cm.market.service.page.batch;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.service.page.FileDataset;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class BatchDataset<T extends Event> implements Iterable<List<T>>, Closeable {
    protected final ExecutorService executorService;
    protected final int threshold;
    protected final List<FileDataset<T>> datasets = new ArrayList<>();
    protected final Set<Iterator<List<T>>> iterators = new HashSet<>();

    public void addDataset(final FileDataset<T> dataset) {
        datasets.add(dataset);
    }

    @Override
    public void close() throws IOException {
    }

    protected abstract class DatasetIterator implements Iterator<List<T>>, Comparable<DatasetIterator> {
        protected final Map<Iterator<T>, DatasetReader<T>> readers = new HashMap<>();
        private final ExecutorService executorService;
        private final List<T> container;
        private final UUID id = UUID.randomUUID();

        public DatasetIterator(final ExecutorService executorService) {
            this.container = new ArrayList<>(datasets.size() * 100);
            this.executorService = executorService;
            for (final FileDataset<T> dataset : datasets) {
                final Iterator<T> it = dataset.iterator();
                readers.put(it, getReader(it));
            }
        }

        protected abstract DatasetReader<T> getReader(final Iterator<T> it);

        @Override
        public boolean hasNext() {
            boolean result = false;
            for (final Iterator<T> it : readers.keySet())
                result |= it.hasNext();
            return result;
        }

        @Override
        @SneakyThrows
        public List<T> next() {
            final Collection<DatasetReader<T>> rr = readers.entrySet().stream()
                    .filter(r -> r.getKey().hasNext())
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            final List<Future<List<T>>> futures = executorService.invokeAll(rr);
            container.clear();
            Thread.yield();
            for (final Future<List<T>> future : futures)
                container.addAll(future.get());
            container.sort(Event::compareTo);
            return container;
        }

        @Override
        public int compareTo(final DatasetIterator o) {
            return id.compareTo(o.id);
        }
    }
}
