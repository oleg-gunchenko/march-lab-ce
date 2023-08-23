package ws.exon.cm.market.model.strategy;

import ws.exon.cm.market.model.core.common.Symbol;
import ws.exon.cm.market.model.core.common.candle.Deal;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

public interface MarketDataProvider {
    String getExchangeCode();

    MarketFee getExchangeFees(final Symbol symbol);

    String getBrokerCode();

    MarketFee getBrokerFee(final Symbol symbol);

    Set<String> getExchangeMarkets();

    Set<Symbol> getMarketSymbols(final String market);

    boolean hasDealsSupply(final Symbol symbol);

    Iterator<Deal> getDealIterator(final Symbol symbol, final LocalDate from, final LocalDate to);

    boolean hasTimeCandleSupply(final Symbol symbol);

    SortedSet<Integer> getTimeframes(final Symbol symbol);

    Iterator<Deal> getCandleIterator(final Symbol symbol, final int period, final LocalDate from, final LocalDate to);
}
