package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

public class MICEXClientAccountFut {
    @QuikAttr(title = "Фирма", shortTitle = "Фирма")
    private String firm;
    @QuikAttr(title = "Торговый счет", shortTitle = "Торговый счет")
    private String account;
    @QuikAttr(title = "Код инструмента", shortTitle = "Код инструмента")
    private String limitType;
    @QuikAttr(title = "Дата погашения", shortTitle = "Дата погашения")
    private String limitType;
    @QuikAttr(title = "Тип", shortTitle = "Тип")
    private String limitType;
    @QuikAttr(title = "Входящие длинные позиции", shortTitle = "Вход. длин. поз.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Входящие короткие позиции", shortTitle = "Вход. кор. поз.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Входящие чистые позиции", shortTitle = "Вход. чист. поз.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Текущие длинные позиции", shortTitle = "Тек. длин. поз.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Текущие короткие позиции", shortTitle = "Тек. кор. поз.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Текущие чистые позиции", shortTitle = "Тек. чист. поз.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Актуальная покупка", shortTitle = "Акт. покупка")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Актуальная продажа", shortTitle = "Акт. продажа")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Оценка текущей чистой позиции", shortTitle = "Оценка тек. чист. поз.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Плановая чистая позиция", shortTitle = "План. чист. поз.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Вариационная маржа", shortTitle = "Вариац. маржа")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Эффективная цена позиции", shortTitle = "Эффект. цена поз.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Стоимость позиций", shortTitle = "Стоимость позиций")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Реальная вариационная маржа", shortTitle = "Реальн. вар. маржа")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Суммарная вариационная маржа", shortTitle = "Сумм. вар. маржа")
    private BigDecimal liquidityRatio;
}
