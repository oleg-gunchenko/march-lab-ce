package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

public class MICEXStockMarketData extends MICEXCommonMarketDataRaw {
    @QuikAttr(title = "Цена аукциона", shortTitle = "Цена аукц.") private BigDecimal auctionPrice;
    @QuikAttr(title = "Объем аукциона", shortTitle = "Объем аукц.") private BigDecimal auctionVolume;
    @QuikAttr(title = "Количество аукциона", shortTitle = "Кол-во аукц.") private BigDecimal auctionQty;
    @QuikAttr(title = "Количество сделок аукциона", shortTitle = "Кол-во сд. аукц.") private BigDecimal auctionDealCount;
    @QuikAttr(title = "Дисбаланс ПА", shortTitle = "Дисбаланс ПА") private BigDecimal paImbalance;
    @QuikAttr(title = "Цена предторгового периода", shortTitle = "Цена предторг.") private BigDecimal preTradePrice;
    @QuikAttr(title = "Цена контрагента", shortTitle = "Цена контраг.") private BigDecimal counterpartyPrice;
}
