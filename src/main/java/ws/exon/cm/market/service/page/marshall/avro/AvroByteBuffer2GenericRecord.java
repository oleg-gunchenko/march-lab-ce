package ws.exon.cm.market.service.page.marshall.avro;

import lombok.SneakyThrows;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import java.nio.ByteBuffer;


public class AvroByteBuffer2GenericRecord extends Avro2GenericRecord<ByteBuffer> {
    public AvroByteBuffer2GenericRecord(final Schema schema) {
        super(schema);
    }

    @SneakyThrows
    public GenericRecord transform(final ByteBuffer data) {
        decoder = decoderFactory.binaryDecoder(data.array(), decoder);
        return reader.read(record, decoder);
    }
}
