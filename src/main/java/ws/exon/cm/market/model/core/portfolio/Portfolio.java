package ws.exon.cm.market.model.core.portfolio;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.threeten.extra.LocalDateRange;
import ws.exon.cm.market.model.core.cep.PriceLong;
import ws.exon.cm.market.model.core.common.Asset;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class Portfolio {
    private Set<Account> accounts = new HashSet<>();
    private Set<Position> positions = new HashSet<>();

    public SortedMap<LocalDate, List<Position>> filterPositions(final LocalDate start, final LocalDate end,
                                                                final Set<Direction> filter) {
        final SortedMap<LocalDate, List<Position>> positions = new TreeMap<>();
        final LocalDateRange range = LocalDateRange.ofClosed(start, end);
        this.positions.stream()
                .filter(p -> filter.contains(p.getDirection()) && range.contains(p.getOpenDt().toLocalDate()))
                .forEach(p -> {
                    final LocalDate openDate = p.getOpenDt().toLocalDate();
                    final LocalDate closeDate = p.getCloseDt().toLocalDate();
                    LocalDateRange.ofClosed(openDate, closeDate).stream().forEach(day -> {
                        if (!positions.containsKey(day))
                            positions.put(day, new ArrayList<>());
                        final List<Position> pl = positions.get(day);
                        pl.add(p);
                    });
                });

        return positions;
    }

    public SortedMap<LocalDate, List<Position.PnL>> getPnL(final SortedMap<LocalDate, List<Position>> positions) {
        return positions.entrySet().stream().map(e -> {
            final LocalDate date = e.getKey();
            final List<Position.PnL> values = e.getValue().stream().map(p -> {
                final PriceLong priceLong = p.getAsset().getPrice(p.getOpenDt().toLocalDate());
                return p.getPnL(priceLong);
            }).collect(Collectors.toList());
            return Pair.of(date, values);
        }).collect(Collectors.toMap(Pair::getKey, Pair::getValue, (o, n) -> n, TreeMap::new));
    }

    public SortedMap<LocalDate, PnL> getReturns(final SortedMap<LocalDate, final List<Position.PnL>>pnPnl) {
        final SortedMap<LocalDate, PnL> poPnl = new TreeMap<>();
        BigDecimal funding = accounts.stream().map(Account::getFunding).reduce(BigDecimal.ZERO, BigDecimal::add);
        for (final Map.Entry<LocalDate, List<Position.PnL>> e : pnPnl.entrySet()) {
            final PnL pnl = new PnL(base, funding, e.getKey(), e.getValue());
            poPnl.put(e.getKey(), pnl);
            funding = funding.add(pnl.getReturns());
        }
        return poPnl;
    }

    @Data
    public static class PnL {
        private final Asset base;
        private final LocalDate date;
        private final BigDecimal funding;
        private final BigDecimal returns;
        private final Map<Asset, BigDecimal> distribution;

        public PnL(final Asset base, final BigDecimal funding, final LocalDate date, final List<Position.PnL> pnl) {
            this.date = date;
            this.base = base;
            this.funding = funding;

            final Map<Asset, BigDecimal> distribution = pnl.stream()
                    .collect(Collectors.toMap(Position.PnL::getAsset, Position.PnL::getTotal));
            distribution.put(base, funding);
            this.returns = distribution.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            this.distribution = distribution.entrySet().stream()
                    .map(e -> Pair.of(e.getKey(), e.getValue().divide(returns, RoundingMode.FLOOR)))
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        }

        public BigDecimal getReturn() {
            return returns.divide(funding, BigDecimal.ROUND_FLOOR);
        }

        public BigDecimal getLogReturn() {
            return BigDecimalMath.log(returns.divide(funding, BigDecimal.ROUND_FLOOR), MathContext.DECIMAL32);
        }
    }
}
