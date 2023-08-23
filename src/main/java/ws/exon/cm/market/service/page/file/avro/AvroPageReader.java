package ws.exon.cm.market.service.page.file.avro;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.commons.collections4.Factory;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.service.page.marshall.avro.GenericRecord2Event;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AvroPageReader<T extends Event> extends AvroPage<T> {
    private final Set<AvroFileEventIterator> iterators = new HashSet<>();

    public AvroPageReader(final Symbol symbol, final LocalDate date, final File root,
                          final Factory<GenericRecord2Event<T>> converterFactory) throws IOException {
        super(symbol, date, root, converterFactory);

    }

    @Override
    @SneakyThrows
    public Iterator<T> iterator() {
        final AvroFileEventIterator it = new AvroFileEventIterator();
        iterators.add(it);
        return it;
    }

    @Override
    public void close() throws IOException {
        this.iterators.forEach(it -> {
            try {
                it.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.iterators.clear();
    }

    @EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
    public class AvroFileEventIterator extends AvroEventIterator<T> {

        protected DataFileReader<GenericRecord> dataFileReader;

        public AvroFileEventIterator() throws IOException {
            super(converterFactory.create());
            final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
            this.dataFileReader = new DataFileReader<>(file, datumReader);
        }

        public void close() throws IOException {
            dataFileReader.close();
            avroPageReader.iterators.remove(this);
        }

        public boolean hasNext() {
            return dataFileReader.hasNext();
        }

        @SneakyThrows
        public T next() {
            record = dataFileReader.next(record);
            return converter.transform(record);
        }
    }
}
