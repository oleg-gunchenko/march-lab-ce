package ws.exon.cm.market.service.page.file.avro;

import org.apache.commons.collections4.Factory;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.service.page.FileDataPage;
import ws.exon.cm.market.service.page.marshall.avro.GenericRecord2Event;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public abstract class AvroPage<I, T extends Event> extends FileDataPage<T> {
    protected final Factory<GenericRecord2Event<T>> converterFactory;

    public AvroPage(final Symbol symbol, final LocalDate date,
                    final File root, final Factory<GenericRecord2Event<T>> converterFactory) throws IOException {
        super(symbol, date, root, "avro");
        this.converterFactory = converterFactory;
    }
}
