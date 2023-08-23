package ws.exon.cm.market.service.page.file.avro;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.service.page.marshall.avro.GenericRecord2Event;

import java.io.Closeable;
import java.util.Iterator;
import java.util.UUID;

public abstract class AvroEventIterator<T extends Event> implements Iterator<T>, Comparable<AvroEventIterator<T>>, Closeable {
    protected final GenericRecord2Event<T> converter;
    @Getter
    @EqualsAndHashCode.Include
    private final UUID id = UUID.randomUUID();
    protected Schema schema;
    protected GenericRecord record;

    public AvroEventIterator(final GenericRecord2Event<T> converter) {
        this.converter = converter;
        this.schema = converter.getSchema();
        this.record = new GenericData.Record(this.schema);
    }

    @Override
    public int compareTo(final AvroEventIterator o) {
        return id.compareTo(o.id);
    }
}
