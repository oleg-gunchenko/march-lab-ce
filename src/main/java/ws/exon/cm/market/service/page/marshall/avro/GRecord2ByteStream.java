package ws.exon.cm.market.service.page.marshall.avro;

import lombok.SneakyThrows;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class GRecord2ByteStream extends GRecord2Binary<ByteArrayOutputStream> {
    public GRecord2ByteStream(final Schema schema, final ByteBuffer buffer) {
        super(schema, buffer);
    }

    public GRecord2ByteStream(final Schema schema, final int bufferSize) {
        super(schema, bufferSize);
    }

    public GRecord2ByteStream(final Schema schema) {
        super(schema);
    }

    @Override
    @SneakyThrows
    public ByteArrayOutputStream transform(final GenericRecord genericRecord) {
        return toByteArray(genericRecord);
    }
}
