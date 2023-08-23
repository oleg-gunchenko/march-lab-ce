package ws.exon.cm.market.service.page.marshall.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;

public abstract class Avro2GenericRecord<T> extends AvroTransformer<T, GenericRecord> {
    protected final GenericDatumReader<GenericRecord> reader;
    protected final DecoderFactory decoderFactory = DecoderFactory.get();
    protected BinaryDecoder decoder;

    public Avro2GenericRecord(final Schema schema) {
        super(schema);
        this.reader = new GenericDatumReader<>(schema);
    }
}
