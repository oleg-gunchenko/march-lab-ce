package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

public class MICEXFutMarketDataRaw extends MICEXCommonMarketDataRaw{
    @QuikAttr(title = "Код инструмента", shortTitle = "Код инструмента") private String instrumentCode;
    @QuikAttr(title = "Код класса", shortTitle = "Код класса") private String instrumentClassCode;
    @QuikAttr(title = "Дата торгов", shortTitle = "Дата торгов") private String tradingDate;
    @QuikAttr(title = "Открытый интерес", shortTitle = "Откр. инт.") private Integer openInterest;
}
