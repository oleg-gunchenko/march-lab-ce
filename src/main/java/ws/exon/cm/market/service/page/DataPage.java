package ws.exon.cm.market.service.page;

import lombok.RequiredArgsConstructor;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.event.Event;

import java.io.Closeable;
import java.time.LocalDate;

@RequiredArgsConstructor
public abstract class DataPage<I, O extends Event> implements Comparable<DataPage<I, O>>, Iterable<O>, Closeable {
    protected final Symbol symbol;
    protected final LocalDate date;

    @Override
    public int compareTo(final DataPage<I, O> o) {
        return date.compareTo(o.date);
    }
}
