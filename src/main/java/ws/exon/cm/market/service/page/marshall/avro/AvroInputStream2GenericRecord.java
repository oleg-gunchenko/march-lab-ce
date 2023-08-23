package ws.exon.cm.market.service.page.marshall.avro;

import lombok.SneakyThrows;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import java.io.InputStream;


public class AvroInputStream2GenericRecord extends Avro2GenericRecord<InputStream> {
    public AvroInputStream2GenericRecord(final Schema schema) {
        super(schema);
    }

    @SneakyThrows
    public GenericRecord transform(final InputStream data) {
        decoder = decoderFactory.binaryDecoder(data, decoder);
        return reader.read(record, decoder);
    }
}
