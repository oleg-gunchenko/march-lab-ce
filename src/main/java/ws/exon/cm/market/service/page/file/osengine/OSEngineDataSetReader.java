package ws.exon.cm.market.service.page.file.osengine;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.Factory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.threeten.extra.Interval;
import ws.exon.cm.market.model.core.cep.DealFactory;
import ws.exon.cm.market.model.core.common.Exchange;
import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.core.common.candle.Deal;
import ws.exon.cm.market.model.orderflow.Candle;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.Boolean.parseBoolean;

@Data
@RequiredArgsConstructor
public class OSEngineDataSetReader {
    public static final Exchange EXCHANGE = new Exchange("Москухня", "MOEX", ZoneOffset.of("Europe/Moscow"));
    public static final String DS_NAME_PREFIX = "Set_";
    public static final String SEC = "sec";
    public static final String MIN = "min";
    public static final String TICK = "tick";
    public static final String HOUR = "hour";
    public static final String DAY = "day";
    private static final DateTimeFormatter OSE_DATA_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter OSE_DATA_TIME_FORMAT = DateTimeFormatter.ofPattern("hhmmss");
    private static final Map<String, Integer> MULT = new HashMap<>();

    static {
        MULT.put(SEC, 1);
        MULT.put(MIN, 60);
        MULT.put(HOUR, 60 * 60);
        MULT.put(DAY, 60 * 60 * 24);
    }

    private final File rootDir;
    private final Map<String, DataSet> ds = new HashMap<>();
    private final Map<String, Exchange> exchanges = new HashMap<>();

    public Set<String> listDataSets() {
        if (!rootDir.isDirectory())
            throw new IllegalStateException("Not a directory");

        final List<File> dss = Arrays.asList(Objects.requireNonNull(rootDir.listFiles((dir, name) -> {
            final File dsDir = new File(dir, name);
            final boolean validDsDir = name.startsWith(DS_NAME_PREFIX) && dsDir.isDirectory();
            if (!validDsDir)
                return false;
            final File dsSettings = new File(dsDir, DataSet.COMMON_SETTINGS_FILENAME);
            return dsSettings.exists() && dsSettings.isFile();
        })));

        if (dss.isEmpty())
            return Collections.emptySet();

        ds.clear();
        dss.stream()
                .map(f -> {
                    try {
                        return new DataSet(f.getName().substring(DS_NAME_PREFIX.length()), f);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(ds -> this.ds.put(ds.name, ds));
        return ds.keySet();
    }

    public Set<String> getSecurities(final String dataset) {
        return ds.get(dataset).getSecurities().keySet();
    }

    public Set<Integer> getSecurityTimeFrames(final String dataset, final String security) throws IOException {
        return ds.get(dataset).getSecurities().get(security).getTimeFrames();
    }

    public List<Deal> getDeals(final String dataset, final String security) {
        return Collections.emptyList();
    }

    @Data
    private static class DataSet {
        public static final String COMMON_SETTINGS_FILENAME = "Settings.txt";

        private final String name;
        private final File rootDir;
        private final Map<String, Security> securities = new HashMap<>();
        private Settings defaults;

        @SuppressWarnings("unchecked")
        public DataSet(String name, File rootDir) throws IOException {
            this.name = name;
            this.rootDir = rootDir;

            final Map<String, TradeSettings> tradeSettings = new HashMap<>();
            final File tsSettings = new File(rootDir, Security.SECURITIES_SETTINGS_FILENAME);
            final List<String> tsl = FileUtils.readLines(tsSettings);
            tsl.stream()
                    .map(TradeSettings::new)
                    .forEach(ts -> tradeSettings.put(ts.getSecName(), ts));

            final File dsSettings = new File(rootDir, COMMON_SETTINGS_FILENAME);
            final Iterator<String> li = FileUtils.readLines(dsSettings).listIterator();
            if (li.hasNext())
                this.defaults = new Settings(li.next());
            li.forEachRemaining(l -> {
                final Security security = new Security(l, rootDir);
                final String secName = security.getSecName();
                securities.put(secName, security);
                security.setTradeSettings(tradeSettings.get(secName));
            });
        }

        @Data
        private static class CandleFactory implements Factory, Closeable {
            private final UUID id = UUID.randomUUID();
            private final Security security;
            private final Integer tf;
            private final LineIterator it;
            private final ZoneOffset zf;
            private final DealFactory dealFactory;
            private final Symbol symbol;

            public CandleFactory(final Security sec, final Integer tf, final LineIterator it, final ZoneOffset zf) {
                this.security = sec;
                this.tf = tf;
                this.it = it;
                this.zf = zf;
                this.symbol = Symbol.create(sec.getExchange().getCode(), sec.getSecClass(), sec.getSecName());

//                this.dealFactory = new DealFactory(this.symbol, )
            }

            @Override
            public Object create() {
                final String line = it.nextLine();
                final String[] parts = line.split(",");
                final Candle candle = new Candle();
                candle.setState(Candle.State.CLOSED);
                candle.setSymbol(symbol);

                final UUID aggregateId = UUID.randomUUID();
                final LocalDate date = LocalDate.parse(parts[0], OSE_DATA_DATE_FORMAT);
                final LocalTime time = LocalTime.parse(parts[1], OSE_DATA_TIME_FORMAT);
                final LocalDateTime ldt = LocalDateTime.of(date, time);


//                final Aggregate aggregate = new Aggregate(aggregateId, );
//                candle.setAggregate(aggregate);
//                candle.setThreshold(tf);
//                candle.setTs(aggregate.getOpenTs());
                return null;
            }

            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public void close() {
                it.close();
            }
        }
    }

    @Data
    public static class TradeSettings {
        private String secName;
        private BigDecimal lot;
        private BigDecimal go;
        private BigDecimal priceStepCost;
        private BigDecimal priceStep;

        public TradeSettings(final String line /*SecuritiesSettings.txt*/) {
            final String[] parts = line.split("\\$");
            secName = parts[0];
            lot = new BigDecimal(parts[1]);
            go = new BigDecimal(parts[2]);
            priceStepCost = new BigDecimal(parts[3]);
            priceStep = new BigDecimal(parts[4]);
        }
    }

    @Data
    public static class Security {
        public static final String SECURITIES_SETTINGS_FILENAME = "SecuritiesSettings.txt";
        private final File rootDir;
        private final Map<UUID, DataSet.CandleFactory> factories = new HashMap<>();
        private String DSName;
        private String secName;
        private String secId;
        private String secClass;
        private Exchange exchange;
        private boolean collapsed;
        private TradeSettings tradeSettings;
        private Settings settings;
        private Map<Integer, File> tfs = new HashMap<>();

        public Security(final String line /*Settings.txt*/, final File rootDir) {
            final String[] parts = line.split("~");

            secName = parts[0];
            secId = parts[1];
            secClass = parts[2];
            collapsed = parseBoolean(parts[3]);
            exchange = exchanges.get(parts[4]);
            DSName = parts[5];
            settings = new Settings(parts[6]);

            this.rootDir = new File(rootDir, secName);
        }

        private static Integer getValue(final String text, final String prefix) {
            return Integer.parseInt(text.substring(prefix.length())) * MULT.get(prefix);
        }

        public Set<Integer> getTimeFrames() {
            tfs.clear();
            Arrays.stream(Objects.requireNonNull(rootDir.listFiles(File::isDirectory)))
                    .map(f -> {
                        final String name = f.getName();
                        final String lcn = name.toLowerCase();
                        final File file = new File(rootDir, name);
                        if (lcn.equals(TICK) && settings.isTfTickIsOn())
                            return Pair.of(0, file);
                        if (lcn.startsWith(SEC) && settings.isTfSecondsOn())
                            return Pair.of(getValue(lcn, SEC), file);
                        if (lcn.startsWith(MIN) && settings.isTfMinuteIsOn())
                            return Pair.of(getValue(lcn, MIN), file);
                        if (lcn.startsWith(HOUR) && settings.isTfHourIsOn())
                            return Pair.of(getValue(lcn, HOUR), file);
                        if (lcn.startsWith(DAY) && settings.isTfDayIsOn())
                            return Pair.of(getValue(lcn, DAY), file);

                        return Pair.of(Integer.MIN_VALUE, file);
                    })
                    .filter(i -> i.getLeft() != Integer.MIN_VALUE)
                    .forEach(p -> tfs.put(p.getLeft(), p.getRight()));
            return tfs.keySet();
        }

        public UUID openCandleIterator(final Integer tf) throws IOException {
            final File deals = new File(tfs.get(tf), String.format("%s.txt", secName));
            final LineIterator li = FileUtils.lineIterator(deals);
            final DataSet.CandleFactory factory = new DataSet.CandleFactory(this, tf, li, getZoneOffset());
            final UUID id = factory.getId();
            factories.put(id, factory);
            return id;
        }

        public List<Candle> getCandles(final UUID factoryId, final List<Candle> buffer) {
            if (!factories.containsKey(factoryId))
                return Collections.emptyList();

            final DataSet.CandleFactory it = factories.get(factoryId);
            if (!it.hasNext())
                return Collections.emptyList();
            final List<Candle> candles = buffer != null ? buffer : new ArrayList<>();
            final int size = candles.size();
            for (int i = 0; i < size && it.hasNext(); i++)
                candles.add((Candle) it.create());

            return candles;
        }

        public void closeCandleIterator(final UUID factoryId) throws IOException {
            if (!factories.containsKey(factoryId))
                return;
            final DataSet.CandleFactory it = factories.get(factoryId);
            it.close();
            factories.remove(factoryId);
        }

        public String dealNormalizer(final String osDealLine) {
            final String[] parts = osDealLine.split(",");
            // 20230103,095947,141.980000000,40,Sell,0
            final BigDecimal price = new BigDecimal(parts[2]);
            final BigDecimal volume = new BigDecimal(parts[3]);
            final String side = parts[4];
            final long millis = Long.parseLong(parts[5]);
            final LocalDate date = LocalDate.parse(parts[0], OSE_DATA_DATE_FORMAT);
            final LocalTime time = LocalTime.parse(parts[1], OSE_DATA_TIME_FORMAT);
            final Long id = parts.length > 5 ? Long.parseLong(parts[6]) : 0L;
            final LocalDateTime ldt = LocalDateTime.of(date, time).plus(millis, ChronoUnit.MILLIS);
            ldt.toEpochSecond(ZoneOffset.UTC);
            return StringUtils.EMPTY;
        }

        public ZoneOffset getZoneOffset() {
            return getSettings().getZoneOffset();
        }
    }

    @Data
    public static class Settings {
        public static final String COMMON_SETTINGS_DELIMITER = "%";
        private static final DateTimeFormatter OSE_SETTINGS_DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy h:mm:ss");

        private boolean enabled; //        Regime = DataSetState.Off;
        private boolean tf1SecondIsOn;
        private boolean tf2SecondIsOn;
        private boolean tf5SecondIsOn;
        private boolean tf10SecondIsOn;
        private boolean tf15SecondIsOn;
        private boolean tf20SecondIsOn;
        private boolean tf30SecondIsOn;

        private boolean tf1MinuteIsOn;
        private boolean tf2MinuteIsOn;
        private boolean tf5MinuteIsOn;
        private boolean tf10MinuteIsOn;
        private boolean tf15MinuteIsOn;
        private boolean tf30MinuteIsOn;
        private boolean tf1HourIsOn;
        private boolean tf2HourIsOn;
        private boolean tf4HourIsOn;
        private boolean tfTickIsOn;
        private boolean tf1DayIsOn;
        private boolean tf5DayIsOn;
        private boolean tf7DayIsOn;
        private boolean tfMarketDepthIsOn;
        private String source;
        private Interval interval;
        private LocalDate timeStart = LocalDate.now().plusDays(-5);
        private LocalDate timeEnd = LocalDate.now().plusDays(5);
        private ZoneOffset zoneOffset = ZoneOffset.UTC;
        private int marketDepthDepth = 5;

        public Settings(final String line) {
            final String[] parts = line.split(COMMON_SETTINGS_DELIMITER);
            int index = 0;
            if (parts.length > 0) {
                enabled = !parts[index++].equalsIgnoreCase("OFF");
                tf1SecondIsOn = parseBoolean(parts[index++]);
                tf2SecondIsOn = parseBoolean(parts[index++]);
                tf5SecondIsOn = parseBoolean(parts[index++]);
                tf10SecondIsOn = parseBoolean(parts[index++]);
                tf15SecondIsOn = parseBoolean(parts[index++]);
                tf20SecondIsOn = parseBoolean(parts[index++]);
                tf30SecondIsOn = parseBoolean(parts[index++]);
                tf1MinuteIsOn = parseBoolean(parts[index++]);
                tf2MinuteIsOn = parseBoolean(parts[index++]);
                tf5MinuteIsOn = parseBoolean(parts[index++]);
                tf10MinuteIsOn = parseBoolean(parts[index++]);
                tf15MinuteIsOn = parseBoolean(parts[index++]);
                tf30MinuteIsOn = parseBoolean(parts[index++]);
                tf1HourIsOn = parseBoolean(parts[index++]);
                tf2HourIsOn = parseBoolean(parts[index++]);
                tf4HourIsOn = parseBoolean(parts[index++]);
                tfTickIsOn = parseBoolean(parts[index++]);
                tfMarketDepthIsOn = parseBoolean(parts[index++]);

                source = parts[index++];
                timeStart = parseDate(parts[index++]);
                timeEnd = parseDate(parts[index++]);

                marketDepthDepth = Integer.parseInt(parts[index++]);
            }
        }

        private static LocalDate parseDate(final String date) {
            return LocalDateTime.parse(date, OSE_SETTINGS_DATETIME_FORMAT).toLocalDate();
        }

        public boolean isTfSecondsOn() {
            return tf1SecondIsOn || tf2SecondIsOn || tf5SecondIsOn
                    || tf10SecondIsOn || tf15SecondIsOn || tf20SecondIsOn || tf30SecondIsOn;
        }

        public boolean isTfMinuteIsOn() {
            return tf1MinuteIsOn || tf2MinuteIsOn || tf5MinuteIsOn
                    || tf10MinuteIsOn || tf15MinuteIsOn || tf30MinuteIsOn;
        }

        public boolean isTfHourIsOn() {
            return tf1HourIsOn || tf2HourIsOn || tf4HourIsOn;
        }

        public boolean isTfDayIsOn() {
            return tf1DayIsOn || tf5DayIsOn || tf7DayIsOn;
        }
    }
}
