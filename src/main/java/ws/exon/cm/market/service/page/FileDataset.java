package ws.exon.cm.market.service.page;

import lombok.Getter;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.service.page.file.avro.AvroPageReader;
import ws.exon.cm.market.service.page.marshall.avro.AvroInputStream2GenericRecord;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class FileDataset<T extends Event> implements Iterable<T> {
    @Getter
    private final Symbol symbol;
    @Getter
    private final LocalDate start;
    @Getter
    private final int period;
    private final SortedMap<LocalDate, PageReader<T>> chunks = new TreeMap<>();
    private final Set<Iterator<T>> iterators = new HashSet<>();

    public FileDataset(final Symbol symbol, final LocalDate start, final int period, final File root, final AvroInputStream2GenericRecord.Factory<T> factory) throws IOException {
        this.symbol = symbol;
        this.start = start;
        this.period = period;

        for (int i = 0; i < period; i++) {
            final LocalDate date = start.minusDays(i);
            final AvroPageReader<T> reader = new AvroPageReader<>(symbol, date, root, factory);
            chunks.put(date, reader);
        }
    }

    public Iterator<T> iterator() {
        final Iterator<T> it = new DatasetIterator();
        iterators.add(it);
        return it;
    }

    private class DatasetIterator implements Iterator<T>, Comparable<DatasetIterator> {
        private final UUID id = UUID.randomUUID();
        private final Iterator<PageReader<T>> chunks = FileDataset.this.chunks.values().iterator();
        private PageReader<T> currentChunk;

        @Override
        public boolean hasNext() {
            return chunks.hasNext() || (currentChunk != null && currentChunk.hasNext());
        }

        @Override
        public T next() {
            if (currentChunk == null || !currentChunk.hasNext())
                currentChunk = chunks.next();
            return currentChunk.next();
        }

        @Override
        public int compareTo(DatasetIterator o) {
            return id.compareTo(o.id);
        }
    }
}
