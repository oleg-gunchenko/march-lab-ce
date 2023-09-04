package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

public class MICEXCommonMarketDataRaw {
    @QuikAttr(title = "Код инструмента", shortTitle = "Код инструмента")
    private String instrumentCode;
    @QuikAttr(title = "Код класса", shortTitle = "Код класса")
    private String instrumentClassCode;
    @QuikAttr(title = "Дата торгов", shortTitle = "Дата торгов")
    private String tradingDate;
    @QuikAttr(title = "Время последнего изменения", shortTitle = "Время изм.")
    private String lastUpdateTime;
    @QuikAttr(title = "Время последней сделки", shortTitle = "Время послед.")
    private String lastDealTime;
    @QuikAttr(title = "Количество во всех сделках", shortTitle = "Общее кол-во")
    private Integer totalQty;
    @QuikAttr(title = "Цена последней сделки", shortTitle = "Цена послед.")
    private BigDecimal lastDealPrice;
    @QuikAttr(title = "Количество в последней сделке", shortTitle = "Кол-во послед.")
    private Integer lastDealQty;
    @QuikAttr(title = "Количество сделок за сегодня", shortTitle = "Кол-во сделок")
    private Integer todDealQty;
    @QuikAttr(title = "Оборот в деньгах последней сделки", shortTitle = "Оборот посл.")
    private BigDecimal lastDealVolume;
    @QuikAttr(title = "Оборот в деньгах", shortTitle = "Оборот")
    private BigDecimal totalVolume;
    @QuikAttr(title = "Лучшая цена спроса", shortTitle = "Спрос")
    private BigDecimal askBestPrice;
    @QuikAttr(title = "Спрос по лучшей цене", shortTitle = "Кол. спрос")
    private Integer askBestPriceQty;
    @QuikAttr(title = "Спрос по лучшей цене", shortTitle = "Общ. спрос")
    private BigDecimal askTotal;
    @QuikAttr(title = "Суммарный спрос", shortTitle = "Заявки. куп")
    private Integer askOrderQty;
    @QuikAttr(title = "Лучшая цена предложения", shortTitle = "Предл.")
    private BigDecimal bidBestPrice;
    @QuikAttr(title = "Предложение по лучшей цене", shortTitle = "Кол. предл.")
    private Integer bidBestPriceQty;
    @QuikAttr(title = "Суммарное предложение", shortTitle = "Общ. предл.")
    private BigDecimal bidTotal;
    @QuikAttr(title = "Количество заявок на продажу", shortTitle = "Заявки прод.")
    private Integer bidOrderQty;
}
