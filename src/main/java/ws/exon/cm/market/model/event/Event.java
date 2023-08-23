package ws.exon.cm.market.model.event;

import java.time.Instant;

public interface Event extends Comparable<Event> {
    Instant getTimestamp();

    @Override
    default int compareTo(final Event o) {
        return getTimestamp().compareTo(o.getTimestamp());
    }
}
