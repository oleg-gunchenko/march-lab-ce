package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

public class MICEXClientAccountLimit {
    @QuikAttr(title = "Фирма", shortTitle = "Фирма")
    private String firm;
    @QuikAttr(title = "Торговый счет", shortTitle = "Торговый счет")
    private String account;
    @QuikAttr(title = "Тип лимита", shortTitle = "Тип лимита")
    private String limitType;
    @QuikAttr(title = "Коэффициент ликвидности", shortTitle = "Коэф. ликвидн.")
    private BigDecimal liquidityRatio;
    @QuikAttr(title = "Предыдущий лимит открытых позиций", shortTitle = "Предыд. лимит откр. поз.")
    private BigDecimal prevOpenPositionsLimit;
    @QuikAttr(title = "Лимит открытых позиций", shortTitle = "Лимит откр. поз.")
    private BigDecimal openPositionsLimit;
    @QuikAttr(title = "Текущая чистая позиция", shortTitle = "Тек. чист. поз.")
    private BigDecimal currentNetPosition;
    @QuikAttr(title = "Текущая чистая позиция(под заявки)", shortTitle = "Тек. чист. поз.(под заявки)")
    private BigDecimal currentNetOrdersPosition;
    @QuikAttr(title = "Текущая чистая позиция(под открытые позиции)", shortTitle = "Тек. чист. поз.(под открытые позиции)")
    private BigDecimal currentNetOpenPosition;
    @QuikAttr(title = "Плановая чистая позиция", shortTitle = "План. чист. поз.")
    private BigDecimal plannedNetPosition;
    @QuikAttr(title = "Вариационная маржа", shortTitle = "Вариац. маржа")
    private BigDecimal variationMargin;
    @QuikAttr(title = "Накопленный доход", shortTitle = "Накоплен. доход")
    private BigDecimal accumulatedIncome;
    @QuikAttr(title = "Премия по опционам", shortTitle = "Премия по опционам")
    private BigDecimal optionsPremium;
    @QuikAttr(title = "Биржевые сборы", shortTitle = "Биржевые сборы")
    private BigDecimal exchangeFees;
    @QuikAttr(title = "Коэффициент клирингового ГО", shortTitle = "Коэф. кл-го ГО")
    private BigDecimal clearingWCRatio;
    @QuikAttr(title = "Валюта позиции", shortTitle = "Валюта позиции")
    private String positionCurrency;
    @QuikAttr(title = "Реальная вариационная маржа", shortTitle = "Реальная вар. маржа")
    private BigDecimal realVariationMargin;
}
