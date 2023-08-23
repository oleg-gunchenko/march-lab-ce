package ws.exon.cm.market.service.page;

import org.apache.avro.generic.GenericRecord;
import org.apache.commons.io.FileUtils;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.event.Event;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class FileDataPage<O extends Event> extends DataPage<GenericRecord, O> {
    private static final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
    private static final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");

    protected final File file;

    public FileDataPage(final Symbol symbol, final LocalDate date, final File root, final String suffix) throws IOException {
        super(symbol, date);
        this.file = getFileName(getDirectory(root), suffix);
    }

    private File getDirectory(final File root) throws IOException {
        final String year = yearFormatter.format(date);
        final String month = monthFormatter.format(date);
        final String exchange = symbol.getExchange();
        final String market = symbol.getMarket();
        final String instrument = symbol.getInstrument();
        final File dir = new File(root, String.format("%s/%s/%s/%s/%s", exchange, market, instrument, year, month));
        FileUtils.forceMkdir(dir);
        return dir;
    }

    private File getFileName(final File root, final String suffix) throws IOException {
        final String day = dayFormatter.format(date);
        final String instrument = symbol.getInstrument();
        final String name = String.format("%s-%s.%s", instrument, day, suffix);
        return new File(getDirectory(root), name);
    }
}
