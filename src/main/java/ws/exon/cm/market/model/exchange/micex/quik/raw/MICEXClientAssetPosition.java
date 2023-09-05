package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

public class MICEXClientAssetPosition {
    @QuikAttr(title = "Фирма", shortTitle = "Фирма")
    private String firm;
    @QuikAttr(title = "Счет депо", shortTitle = "Счет депо")
    private String account;
    @QuikAttr(title = "Код инструмента", shortTitle = "Код инструмента")
    private String limitType;
    @QuikAttr(title = "ISIN", shortTitle = "ISIN")
    private String limitType;
    @QuikAttr(title = "Счет депо", shortTitle = "Счет депо")
    private String limitType;
    @QuikAttr(title = "Код клиента", shortTitle = "Код клиента")
    private String limitType;
    @QuikAttr(title = "Срок расчетов", shortTitle = "Срок расчетов")
    private String limitType;
    @QuikAttr(title = "Дата расчетов", shortTitle = "Дата расчетов")
    private String limitType;
    @QuikAttr(title = "Входящий остаток", shortTitle = "Вход. ост.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Входящий лимит", shortTitle = "Вход. лим.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Текущий остаток", shortTitle = "Тек. ост.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Текущий лимит", shortTitle = "Тек. лим.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "В продаже", shortTitle = "В продаже")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "В покупке", shortTitle = "В покупке")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Всего", shortTitle = "Всего")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Цена приобретения", shortTitle = "Цена приобретения")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Валюта приобретения", shortTitle = "Валюта приобретения")
    private String limitType;

}
