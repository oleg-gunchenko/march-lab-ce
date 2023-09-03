package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

public class MICEXFutInfoDataRaw {
    @QuikAttr(title = "Код инструмента", shortTitle = "Код инструмента")
    private String instrumentCode;
    @QuikAttr(title = "Код класса", shortTitle = "Код класса")
    private String instrumentClassCode;
    @QuikAttr(title = "Дата торгов", shortTitle = "Дата торгов")
    private String tradingDate;
    @QuikAttr(title = "Дата погашения", shortTitle = "Погашение")
    private String maturityDate;
    @QuikAttr(title = "Число дней до погашения", shortTitle = "До погашения")
    private Integer daysToMaturity;
    @QuikAttr(title = "Дата исполнения инструмента", shortTitle = "Дата исп.")
    private String executionDate;
    @QuikAttr(title = "Базовый актив", shortTitle = "Баз. актив")
    private String baseInstrumentCode;
    @QuikAttr(title = "Класс базового актива", shortTitle = "Класс баз. актива")
    private String baseClassCode;
    @QuikAttr(title = "Гарантийное обеспечение продавца", shortTitle = "ГО продавца")
    private BigDecimal sellerWarrantyCoverage;
    @QuikAttr(title = "Гарантийное обеспечение покупателя", shortTitle = "ГО покупателя")
    private BigDecimal buyerWarrantyCoverage;
    @QuikAttr(title = "Тип фьючерса", shortTitle = "Тип фьючерса")
    private String futureType;
    @QuikAttr(title = "Тип цены фьючерса", shortTitle = "Тип цены фьюч.")
    private String futurePriceType;
}
