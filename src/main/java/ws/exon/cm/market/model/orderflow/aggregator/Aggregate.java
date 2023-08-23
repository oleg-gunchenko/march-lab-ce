package ws.exon.cm.market.model.orderflow.aggregator;

import lombok.Data;
import lombok.ToString;
import org.kie.api.definition.type.Duration;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import ws.exon.cm.market.model.core.cep.DealLong;
import ws.exon.cm.market.model.core.cep.PriceLong;
import ws.exon.cm.market.model.core.common.OHLC;
import ws.exon.cm.market.model.core.common.candle.Deal;

import java.util.List;
import java.util.UUID;

@Data
@Role(Role.Type.EVENT)
@Expires("14d")
@Timestamp("price.ts")
@Duration("duration")
@ToString(onlyExplicitlyIncluded = true)
public class Aggregate {
    private final UUID id;
    private final long openDealId;
    private final int pricePrecision;
    private final long openOI;
    private final Counter bid = new Counter();
    private final Counter ask = new Counter();
    private final Counter oio = new Counter();
    private final Counter oic = new Counter();
    private final Counter oiz = new Counter();
    @ToString.Include
    private long openTs;
    @ToString.Include
    private long closeTs;
    private long closeDealId;
    private long duration;
    private long closeOI;
    private OHLC price;
    private OHLC delta;
    private OHLC oi;
    @ToString.Include
    private long contractVolume;
    @ToString.Include
    private long contractDelta;
    @ToString.Include
    private long dealVolume;
    @ToString.Include
    private long dealDelta;
    private long moneyVolume;
    private long moneyDelta;
    private long oiVolume;
    private long oiDelta;
    private List<Aggregate> clusters;

    public Aggregate(final UUID id, final DealLong deal) {
        this(id, deal, getDelta(deal));
    }

    public Aggregate(final UUID id, final DealLong deal, final long delta) {
        this(id, deal, delta, 0);
    }

    public Aggregate(final UUID id, final DealLong deal, final long delta, final long oi) {
        this.id = id;
        this.closeDealId = openDealId = deal.getId();
        this.openOI = this.closeOI = deal.getOpenInterest();
        final PriceLong priceLong = (PriceLong) deal.getPrice();
        final long ts = priceLong.getTs();
        this.closeTs = this.openTs = ts;
        this.price = new OHLC(priceLong);
        this.pricePrecision = priceLong.getPrecision();

        this.delta = new OHLC(new PriceLong(delta, ts, pricePrecision));
        this.oi = new OHLC(new PriceLong(oi, ts, pricePrecision));

        mergeSideStat(deal.getSide(), priceLong.getLValue(), deal.getLongVolume(), deal.getOpenInterest());
    }

    private static long getDelta(final DealLong deal) {
        final Deal.Side side = deal.getSide();
        if (side == Deal.Side.NONE)
            return 0;
        return side == Deal.Side.BUY ? deal.getLongVolume() : -deal.getLongVolume();
    }

    private void mergeSideStat(final Deal.Side side, final long price, final long volume, final long oi) {
        if (side != Deal.Side.NONE) {
            final Counter counter = side == Deal.Side.BUY ? ask : bid;
            counter.incr(price, volume, oi);
        }
    }

    public void merge(final DealLong DealLong) {
        merge(DealLong, getDelta(DealLong));
    }

    public void merge(final DealLong DealLong, final long delta) {
        merge(DealLong, delta, 0);
    }

    public void merge(final DealLong deal, final long delta, final long oi) {
        final PriceLong priceLong = (PriceLong) deal.getPrice();
        mergeSideStat(deal.getSide(), priceLong.getLValue(), deal.getLongVolume(), deal.getOpenInterest());

        final long ts = priceLong.getTs();
        this.price.evaluate(priceLong);
        this.delta.evaluate(new PriceLong(delta, ts, pricePrecision));
        this.oi.evaluate(new PriceLong(oi, ts, pricePrecision));

        closeDealId = deal.getId();
        closeTs = ts;
        closeOI = deal.getOpenInterest();
    }

    public long dealVolume() {
        return ask.getDeals() + bid.getDeals();
    }

    public long moneyVolume() {
        return ask.getMoney() + bid.getMoney();
    }

    public long contractVolume() {
        return ask.getVolume() + bid.getVolume();
    }

    public long oiVolume() {
        return ask.getOI() + bid.getOI();
    }

    public long dealDelta() {
        return ask.getDeals() - bid.getDeals();
    }

    public long moneyDelta() {
        return ask.getMoney() - bid.getMoney();
    }

    public long contractDelta() {
        return ask.getVolume() - bid.getVolume();
    }

    public long oiDelta() {
        return ask.getOI() - bid.getOI();
    }

    public long duration() {
        return closeTs - openTs;
    }

    @Data
    public static class Counter {
        private int deals;
        private long money;
        private int oiod;
        private int oicd;
        private long volume;
        private long oio;
        private long oic;

        private void evalOI(final long oi) {
            if (oi == 0)
                deals++;
            else if (oi > 0) {
                oio += oi;
                oiod++;
            } else {
                oic += -oi;
                oicd++;
            }
        }

        public long getOI() {
            return oic - oio;
        }

        public long getDeals() {
            return deals + oiod + oicd;
        }

        public void incr(final long price, final long volume, final long oi) {
            this.volume += volume;
            this.money += volume * price;
            evalOI(oi);
        }
    }
}
