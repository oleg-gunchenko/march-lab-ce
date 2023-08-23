package ws.exon.cm.market.service.page.file.csv;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.event.data.orderflow.NewDealEvent;
import ws.exon.cm.market.service.page.PageReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BufferedDealsReader extends PageReader<NewDealEvent> {
    private final List<String> data;
    private int index = 0;

    protected BufferedDealsReader(final Symbol symbol, final LocalDate date, final List<String> data) {
        super(symbol, date);
        this.data = data;
    }

    @Override
    protected List<String> getCurrentBatch() {
        final int bs = Math.min(data.size() - index, batchSize);
        final List<String> batch = data.subList(index, index + bs);
        index += batchSize;
        return batch;
    }

    @Override
    protected boolean isPageDataAvailable() {
        return true;
    }

    @Override
    protected boolean hasNext() {
        return index < data.size();
    }

    @Data
    @RequiredArgsConstructor
    public static class FileFactory implements Factory<NewDealEvent> {
        private final File file;
        private final int batchSize;

        @Override
        @SuppressWarnings("unchecked")
        public PageReader<NewDealEvent> create(final Symbol symbol, final LocalDate date) throws IOException {
            return new BufferedDealsReader(symbol, date, FileUtils.readLines(file), batchSize);
        }

        @Override
        public int getBatchSize() {
            return 0;
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class ClasspathFileFactory implements Factory<NewDealEvent> {
        private final String name;
        private final int[] precision;
        private final int batchSize;

        @Override
        @SuppressWarnings("unchecked")
        public PageReader<NewDealEvent> create(final Symbol symbol, final LocalDate date) throws IOException {
            final InputStream is = ClasspathFileFactory.class.getClassLoader().getResourceAsStream(name);
            final ExonRowParser parser = new ExonRowParser(precision[0], precision[1]);
            final List<String> data = (List<String>) IOUtils.readLines(is)
                    .stream()
                    .map(s -> ((String) s).split(":"))
                    .map(r -> parser.parse((String[]) r)).collect(Collectors.toList());
            final PageReader<NewDealEvent> reader = new BufferedDealsReader(symbol, date, data, batchSize);
            IOUtils.closeQuietly(is);
            return reader;
        }
    }
}
