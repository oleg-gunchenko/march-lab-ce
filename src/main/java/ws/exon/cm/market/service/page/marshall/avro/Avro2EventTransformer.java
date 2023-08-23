package ws.exon.cm.market.service.page.marshall.avro;

import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.collections4.Transformer;
import ws.exon.cm.market.model.event.Event;

@RequiredArgsConstructor
public class Avro2EventTransformer<I, O extends Event> implements Transformer<I, O> {
    private final Avro2GenericRecord<I> reader;
    private final GenericRecord2Event<O> writer;

    @Override
    public O transform(I i) {
        final GenericRecord gr = reader.transform(i);
        return writer.transform(gr);
    }
}
