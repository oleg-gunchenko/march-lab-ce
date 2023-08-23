package ws.exon.cm.market.service.page;

import lombok.SneakyThrows;
import org.apache.avro.io.*;
import ws.exon.cm.market.model.core.cep.DealLong;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.model.event.data.MarketEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public abstract class PageReader<T extends Event> extends DataPage<T> implements Iterator<T> {
    protected final String key;
    protected final String taskId = UUID.randomUUID().toString();

    public PageReader(final Symbol symbol, final LocalDate date) {
        super(symbol, date);
        this.key = formatPageKey(symbol, date);
    }

    @SneakyThrows
    public List<MarketEvent> getMargetEvents(final List<MarketEvent> buffer) {
        final List<MarketEvent> result = buffer == null ? new ArrayList<>(this.batchSize) : buffer;
        final List<String> lines = getCurrentBatch();
        if (lines == null || lines.isEmpty())
            return null;

        for (final String line : lines)
            result.add(DealLong.fromString(line, symbol, pricePrecision, volumePrecision));

        return result;
    }

    public interface Factory<T extends Event> {
        PageReader<T> create(final Symbol symbol, final LocalDate date) throws IOException;
    }
}
