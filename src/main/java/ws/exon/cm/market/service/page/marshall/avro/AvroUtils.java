package ws.exon.cm.market.service.page.marshall.avro;

import lombok.SneakyThrows;
import org.apache.avro.Schema;

import java.io.File;

public class AvroUtils {
    public static Schema parse(final String schema) {
        return new Schema.Parser().parse(schema);
    }

    @SneakyThrows
    public static Schema parse(final File schema) {
        return new Schema.Parser().parse(schema);
    }
}
