package ws.exon.cm.market.service.page.marshall.avro;

import lombok.SneakyThrows;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import java.nio.ByteBuffer;

public class GRecord2ByteBuffer extends GRecord2Binary<ByteBuffer> {

    public GRecord2ByteBuffer(final Schema schema, final ByteBuffer buffer) {
        super(schema, buffer);
    }

    public GRecord2ByteBuffer(final Schema schema, final int bufferSize) {
        super(schema, bufferSize);
    }

    public GRecord2ByteBuffer(final Schema schema) {
        super(schema);
    }

    @SneakyThrows
    public ByteBuffer transform(GenericRecord genericRecord) {
        buffer.clear();
        buffer.put(toByteArray(genericRecord).toByteArray());
        return buffer;
    }
}
