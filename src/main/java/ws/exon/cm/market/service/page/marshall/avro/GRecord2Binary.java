package ws.exon.cm.market.service.page.marshall.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class GRecord2Binary<T> extends GRecordTransformer<T> {
    protected final GenericDatumWriter<GenericRecord> writer;
    private final EncoderFactory encoderFactory = EncoderFactory.get();
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BUFFER_SIZE);
    private BinaryEncoder encoder;

    public GRecord2Binary(Schema schema) {
        super(schema);
        this.writer = new GenericDatumWriter<>(schema);
    }

    public GRecord2Binary(Schema schema, int bufferSize) {
        super(schema, bufferSize);
        this.writer = new GenericDatumWriter<>(schema);
    }

    public GRecord2Binary(Schema schema, ByteBuffer buffer) {
        super(schema, buffer);
        this.writer = new GenericDatumWriter<>(schema);
    }

    protected ByteArrayOutputStream toByteArray(final GenericRecord object) throws IOException {
        outputStream.reset();
        encoder = encoderFactory.binaryEncoder(outputStream, encoder);
        writer.write(object, encoder);
        encoder.flush();
        return outputStream;
    }
}
