package ws.exon.cm.market.service.page.marshall.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.collections4.Transformer;

import java.nio.ByteBuffer;

public abstract class GRecordTransformer<T> extends AvroTransformer<GenericRecord, T> implements Transformer<GenericRecord, T> {
    protected static final int BUFFER_SIZE = 0x1024;
    protected final ByteBuffer buffer;

    public GRecordTransformer(Schema schema, ByteBuffer buffer) {
        super(schema);
        this.buffer = buffer;
    }

    public GRecordTransformer(final Schema schema, final int bufferSize) {
        this(schema, ByteBuffer.allocate(bufferSize));
    }

    public GRecordTransformer(final Schema schema) {
        this(schema, BUFFER_SIZE);
    }
}
