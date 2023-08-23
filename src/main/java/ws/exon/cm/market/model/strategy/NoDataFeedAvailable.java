package ws.exon.cm.market.model.strategy;

import lombok.RequiredArgsConstructor;
import ws.exon.cm.market.model.core.common.Symbol;

@RequiredArgsConstructor
public class NoDataFeedAvailable extends RuntimeException {
    private final Symbol symbol;
}
