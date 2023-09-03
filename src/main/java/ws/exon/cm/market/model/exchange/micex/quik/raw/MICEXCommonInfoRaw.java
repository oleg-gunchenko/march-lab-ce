package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

@Data
public class MICEXCommonInfoRaw {
    @QuikAttr(title = "Полное название инструмента", shortTitle = "Инструмент")
    private String instrumentTitle;
    @QuikAttr(title = "Краткое название инструмента", shortTitle = "Инструмент сокр.")
    private String instrumentTitleShort;
    @QuikAttr(title = "Код инструмента", shortTitle = "Код инструмента")
    private String instrumentCode;
    @QuikAttr(title = "Название класса", shortTitle = "Класс")
    private String instrumentClass;
    @QuikAttr(title = "Код класса", shortTitle = "Код класса")
    private String instrumentClassCode;
    @QuikAttr(title = "Дата торгов", shortTitle = "Дата торгов")
    private String tradingDate;
    @QuikAttr(title = "Тип инструмента", shortTitle = "Тип инстр-та")
    private String instrumentType;
    @QuikAttr(title = "Подтип инструмента", shortTitle = "Подтип инстр-та")
    private String instrumentSubType;
    @QuikAttr(title = "Класс базового актива", shortTitle = "Класс баз. актива")
    private String baseInstrumentClass;
    @QuikAttr(title = "Базовый актив", shortTitle = "Баз. актив")
    private String baseInstrumentCode;
    @QuikAttr(title = "Квалифицированный инвестор", shortTitle = "Квалиф. инвестор")
    private String qualifiedInvestor;
    @QuikAttr(title = "Минимально возможная цена", shortTitle = "Мин. возм. цена")
    private BigDecimal minPossiblePrice;
    @QuikAttr(title = "Максимально возможная цена", shortTitle = "Макс. возм. цена")
    private BigDecimal maxPossiblePrice;
    @QuikAttr(title = "Признак ограничения отрицательных цен", shortTitle = "Огран. отриц. цен")
    private String restrictNegPrice;
    @QuikAttr(title = "Точность количества", shortTitle = "Точн. кол-ва")
    private Integer qtyPrecision;
    @QuikAttr(title = "Макс. актуальная точность количества", shortTitle = "Макс. акт. точн. кол-ва")
    private Integer maxActualQtyPrecision;
    @QuikAttr(title = "Точность цены", shortTitle = "Точность")
    private Integer pricePrecision;
    @QuikAttr(title = "Минимальный шаг цены", shortTitle = "Шаг цены")
    private BigDecimal priceStep;
    @QuikAttr(title = "Стоимость шага цены", shortTitle = "Ст. шага цены")
    private BigDecimal priceStepCost;
    @QuikAttr(title = "Стоимость шага цены в валюте", shortTitle = "Ст. шага в валюте")
    private BigDecimal priceStepCostCurrency;
    @QuikAttr(title = "Валюта шага цены", shortTitle = "Валюта шага цены")
    private String priceStepCurrency;
    @QuikAttr(title = "Размер лота", shortTitle = "Лот")
    private BigDecimal lotSize;
    @QuikAttr(title = "Кратность лота", shortTitle = "Кратность лота")
    private BigDecimal lotMultiplier;
    @QuikAttr(title = "Валюта номинала", shortTitle = "Валюта")
    private String instrumentCurrency;
    @QuikAttr(title = "Статус торговли инструментом", shortTitle = "Статус")
    private String instrumentStatus;
    @QuikAttr(title = "Состояние сессии", shortTitle = "Сессия")
    private String tradeSession;
    @QuikAttr(title = "Биржевой статус торговой сессии", shortTitle = "Бирж. сессия")
    private String exchangeSession;
    @QuikAttr(title = "Доступность в утренней сессии", shortTitle = "Утр. сессия")
    private String morningSession;
    @QuikAttr(title = "Начало основной сессии", shortTitle = "Начало")
    private String daySessionBegin;
    @QuikAttr(title = "Окончание основной сессии", shortTitle = "Окончание")
    private String daySessionEnd;
    @QuikAttr(title = "Начало вечерней сессии", shortTitle = "Начало веч.")
    private String eveningSessionBegin;
    @QuikAttr(title = "Окончание вечерней сессии", shortTitle = "Окончание. веч")
    private String eveningSessionEnd;
    @QuikAttr(title = "Допуск к вечерней сессии", shortTitle = "Доп. сессия")
    private String extendedSession;
    @QuikAttr(title = "Дата расчетов", shortTitle = "Дата расч.")
    private String settlementDate;
    @QuikAttr(title = "Дата расчетов 1", shortTitle = "Дата расч. 1")
    private String settlementDate1;
    @QuikAttr(title = "Код расчетов по умолчанию", shortTitle = "Код расч.")
    private String settlementCode;
    @QuikAttr(title = "Статус клиринга", shortTitle = "Статус кл.")
    private String clearingStatus;
}
