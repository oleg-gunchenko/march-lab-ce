package ws.exon.cm.market.service.page.file.avro;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.event.Event;
import ws.exon.cm.market.service.page.marshall.avro.AvroInputStream2GenericRecord;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;

public class AvroPageWriter<T extends Event> extends AvroPage<T> {
    private final AvroInputStream2GenericRecord<T> converter;
    protected DataFileWriter<GenericRecord> dataFileWriter;
    private CodecFactory codecFactory;

    public AvroPageWriter(final Symbol symbol, final LocalDate date, final File root,
                          final AvroInputStream2GenericRecord.Factory<T> converterFactory) throws IOException {
        super(symbol, date, root, converterFactory);
        this.converter = converterFactory.create();
    }

    public void setCompression(final int compressionLevel) {
        codecFactory = CodecFactory.deflateCodec(compressionLevel);
    }

    public void createDataset(final File file) throws IOException {
        final Schema schema = converter.getSchema();
        final DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        this.dataFileWriter = new DataFileWriter<>(datumWriter);
        this.dataFileWriter.setCodec(codecFactory);
        this.dataFileWriter.create(schema, file);
    }

    public void createDataset(final File file, final int compression) throws IOException {
        setCompression(compression);
        createDataset(file);
    }

    public void writeRecord(final T object) throws IOException {
        dataFileWriter.append(converter.toRecord(object));
    }

    public void writeRecord(final ByteBuffer byteBuffer) throws IOException {
        dataFileWriter.appendEncoded(byteBuffer);
    }

    public void writeRecord(final GenericRecord record) throws IOException {
        dataFileWriter.append(record);
    }

    public void close() throws IOException {
        dataFileWriter.close();
    }
}
