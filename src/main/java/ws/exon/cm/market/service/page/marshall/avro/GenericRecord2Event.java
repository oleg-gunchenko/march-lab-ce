package ws.exon.cm.market.service.page.marshall.avro;

import org.apache.avro.Conversions;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.avro.data.TimeConversions;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import ws.exon.cm.market.model.event.Event;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;

//    {
//        "namespace": "com.chariotsolutions.example.avro",
//            "type": "record",
//            "name": "CheckoutComplete",
//            "fields": [
//        {
//            "name": "eventType",
//                "type": "string"
//        },
//        {
//            "name": "eventId",
//                "type": "string"
//        },
//        {
//            "name": "timestamp",
//                "type": {
//            "type": "long",
//                    "logicalType": "timestamp-millis"
//        }
//        },
//        {
//            "name": "userId",
//                "type": "string"
//        },
//        {
//            "name": "itemsInCart",
//                "type": "int"
//        },
//        {
//            "name": "totalValue",
//                "type": {
//            "type": "bytes",
//                    "logicalType": "decimal",
//                    "precision": 16,
//                    "scale": 2
//        }
//        }
//  ]
//    }


public abstract class GenericRecord2Event<T extends Event> extends AvroTransformer<GenericRecord, T> {
    private final static TimeConversions.TimestampMillisConversion TIMESTAMP_CONVERTER = new TimeConversions.TimestampMillisConversion();
    private final static Conversions.DecimalConversion DECIMAL_CONVERTER = new Conversions.DecimalConversion();

    public GenericRecord2Event(final Schema schema) {
        super(schema);
    }

    private void createInstance() throws IOException {
        final File employeeSchema = new File("src/main/avro/employee.avsc");
//Serializing and deserializing without code generation. Using //Schema parsers
        final Schema schema = new Schema.Parser().parse(employeeSchema);
        final GenericRecord empl1 = new GenericData.Record(schema);

        final LogicalType timestampLT = schema.getField("timestamp").schema().getLogicalType();
        final LogicalType totalValueLT = schema.getField("totalValue").schema().getLogicalType();
        TIMESTAMP_CONVERTER.toLong(Instant.MAX, schema, timestampLT);
        DECIMAL_CONVERTER.toBytes(BigDecimal.ONE, schema, totalValueLT);

//Get the inner object Department
        final GenericData.Record dept = new GenericData.Record(schema.getField("department").schema());
        dept.put("id", 99);
        dept.put("name", "Accounts");

        empl1.put("id", 1);
        empl1.put("first", "Nikhil");
        empl1.put("last", "Chinnappa");
        empl1.put("department", dept);

        GenericRecord empl2 = new GenericData.Record(schema);
        empl2.put("id", 2);
        empl2.put("first", "Rick");
        empl2.put("last", "Ross");
        empl2.put("department", dept);

        GenericRecord empl3 = new GenericData.Record(schema);
        empl3.put("id", 3);
        empl3.put("first", "Sunil");
        empl3.put("last", "Gav");
        empl3.put("department", dept);
    }
}
