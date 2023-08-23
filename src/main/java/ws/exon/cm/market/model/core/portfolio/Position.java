package ws.exon.cm.market.model.core.portfolio;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.collection.CompositeCollection;
import org.threeten.extra.Interval;
import ws.exon.cm.market.model.core.cep.PriceLong;
import ws.exon.cm.market.model.core.common.Asset;
import ws.exon.cm.market.model.core.common.candle.Deal;
import ws.exon.cm.market.model.core.common.candle.Price;
import ws.exon.cm.market.model.core.order.Order;
import ws.exon.cm.market.model.core.order.Trade;
import ws.exon.cm.market.model.core.order.fence.Fence;
import ws.exon.cm.market.model.strategy.Strategy;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;


@Data
public class Position implements Comparable<Position>, IPosition {
    private static final Comparator<Deal> TS_COMPARATOR = Comparator.comparingLong(Deal::getTs);
    private final List<Order> openings = new ArrayList<>();
    private final List<Order> closings = new ArrayList<>();
    private final CompositeCollection<Order> orders = new CompositeCollection<>(openings, closings);
    private final List<Fence> takes = new ArrayList<>();
    private final List<Fence> stops = new ArrayList<>();
    private final Map<Long, Deal> deals = new HashMap<>();
    private final List<OpenVolume> openVolumes = new ArrayList<>();
    private final List<ClosedVolume> closedVolumes = new ArrayList<>();
    private UUID id = UUID.randomUUID();
    private LocalDateTime openDt = LocalDateTime.now();
    private State state = State.NEW;
    private Asset asset;
    private LocalDateTime closeDt;
    private Direction direction;
    private PnL pnl;
    private long plannedFunding;
    private UUID strategyId;
    private boolean closeOnly;
    private Account account;
    private long pnlSign;

    public Position(final Strategy strategy, final Direction direction, final Asset asset, final LocalDateTime openDt) {
        this.asset = asset;
        this.openDt = openDt;
        this.state = State.NEW;
        this.strategyId = strategy.getId();
        setDirection(direction);
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
        this.pnlSign = direction == Direction.LONG ? 1 : -1;
    }

    @SuppressWarnings("unchecked")
    private List<Deal> getDeals(final List<Order> orders, final Instant endTs) {
        final List<Deal> deals = new ArrayList<>();
        orders.forEach(o -> deals.addAll(o.getTrades().stream().map(Trade::getDealLong).collect(Collectors.toList())));
        if (deals.isEmpty())
            return Collections.EMPTY_LIST;
        deals.sort(TS_COMPARATOR);
        return deals.stream()
                .filter(d -> d.getPrice().getTimestamp().isBefore(endTs))
                .collect(Collectors.toList());
    }

    public void evaluateDeals(final Instant endTs) {
        final List<Deal> openDealLongs = getDeals(openings, endTs);
        if (openDealLongs.isEmpty())
            return;

        openVolumes.clear();
        for (final Deal d : openDealLongs) {
            final long id = d.getId();
            openVolumes.add(new OpenVolume(id, (PriceLong) d.getPrice(), d.getLongVolume()));
            deals.putIfAbsent(id, d);
        }

        final List<Deal> closeDealLongs = getDeals(closings, endTs);
        if (closeDealLongs.isEmpty())
            return;

        closedVolumes.clear();
        for (final Deal d : closeDealLongs) {
            long v = d.getLongVolume();
            long p = ((PriceLong) d.getPrice()).getLValue();
            deals.putIfAbsent(d.getId(), d);
            for (final OpenVolume ov : openVolumes) {
                if (ov.getVolume() == 0)
                    continue;

                final long volume = Math.min(ov.getVolume(), v);
                closedVolumes.add(new ClosedVolume(ov.openDealId, d.getId(), ov.getOpen(), (PriceLong) d.getPrice(), volume));
                ov.setVolume(ov.getVolume() - volume);
                v -= volume;

                if (v == 0)
                    break;
            }
        }
    }

    @Override
    public long getPlannedVolume() {
        return openings.stream().map(Order::getPlannedQuantity).reduce(0L, Long::sum);
    }

    @Override
    public long getOpenedVolume() {
        return openings.stream().map(Order::exposedQuantity).reduce(0L, Long::sum);
    }

    @Override
    public long getExposedVolume() {
        return openVolumes.stream().map(OpenVolume::getVolume).reduce(0L, Long::sum);
    }

    @Override
    public State getState() {
        final long open = getExposedVolume();
        if (!openVolumes.isEmpty() && open == 0)
            return State.CLOSED;

        if (closeOnly)
            return State.CLOSING;

        long plan = getPlannedVolume();
        long fact = getOpenedVolume();

        if (plan > fact)
            return State.OPENING;

        return State.OPENED;
    }

    public PnL getPnL(final Price price) {
        if (openVolumes.isEmpty())
            return PnL.ZERO;
        final LocalDateTime ts = LocalDateTime.now();
        final long feesTotal = orders.stream().map(Order::getFeesTotal).reduce(0L, Long::sum);
        final long realizedPnL = pnlSign * closedVolumes.stream()
                .map(ClosedVolume::getExposition)
                .reduce(0L, Long::sum);
        if (price == null)
            return new PnL(asset, ts, feesTotal, realizedPnL, 0);
        else {
            final long pv = price.getLValue();
            long openedPnL = pnlSign * openVolumes.stream()
                    .filter(ov -> ov.getVolume() > 0)
                    .map(ov -> ov.getExposition(pv))
                    .reduce(0L, Long::sum);
            return new PnL(asset, ts, feesTotal, realizedPnL, openedPnL);
        }
    }

    @Override
    public Interval getTimeInterval() {
        return Interval.of(openDt.toInstant(ZoneOffset.UTC), Instant.MAX);
    }

    public Duration getDuration(final Price priceLong) {
        final State state = getState();

        final Deal open = deals.get(closedVolumes.get(0).getOpenDealId());
        if (priceLong != null && (state == State.OPENING || state == State.OPENED || state == State.CLOSING))
            return Duration.between(open.getPrice().getTimestamp(), priceLong.getTimestamp());

        if (state == State.CLOSED || state == State.CLOSING) {
            final Deal close = deals.get(closedVolumes.get(closedVolumes.size() - 1).getCloseDealId());
            return Duration.between(open.getPrice().getTimestamp(), close.getPrice().getTimestamp());
        }

        return Duration.ZERO;
    }

    @Override
    public int compareTo(final Position o) {
        int sc = strategyId.compareTo(o.getStrategyId());
        if (sc != 0)
            return sc;
        int ase = asset.compareTo(o.asset);
        if (ase != 0)
            return ase;
        return openDt.compareTo(o.openDt);
    }

    @Data
    @AllArgsConstructor
    private static final class OpenVolume {
        private final long openDealId;
        private final PriceLong open;
        private long volume;

        public long getExposition(final long price) {
            return (price - open.getLValue()) * volume;
        }
    }

    @Data
    @AllArgsConstructor
    private static final class ClosedVolume {
        private final long openDealId;
        private final long closeDealId;
        private final PriceLong open;
        private final PriceLong close;
        private long volume;

        public long getExposition() {
            return (close.getLValue() - open.getLValue()) * volume;
        }
    }
}
