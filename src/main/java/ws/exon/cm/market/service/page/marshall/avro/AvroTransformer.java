package ws.exon.cm.market.service.page.marshall.avro;

import lombok.Getter;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.collections4.Transformer;

public abstract class AvroTransformer<I, O> implements Transformer<I, O> {
    @Getter
    protected final Schema schema;
    protected final GenericRecord record;

    public AvroTransformer(final Schema schema) {
        this.schema = schema;
        this.record = new GenericData.Record(schema);
    }
}
