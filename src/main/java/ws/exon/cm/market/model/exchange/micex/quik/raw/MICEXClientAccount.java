package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

public class MICEXClientAccount {
    @QuikAttr(title = "Фирма", shortTitle = "Фирма")
    private String firm;
    @QuikAttr(title = "Счет", shortTitle = "Счет")
    private String account;
    @QuikAttr(title = "Основной счет", shortTitle = "Основной счет")
    private String limitType;
    @QuikAttr(title = "Счет депо", shortTitle = "Счет депо")
    private String limitType;
    @QuikAttr(title = "Код позиции", shortTitle = "Код позиции")
    private String limitType;
    @QuikAttr(title = "Описан   ие", shortTitle = "Описание")
    private String limitType;
    @QuikAttr(title = "Тип торгового счета", shortTitle = "Тип торгового счета")
    private String limitType;
    @QuikAttr(title = "Тип счета депо", shortTitle = "Тип счета депо")
    private String limitType;
    @QuikAttr(title = "Статус", shortTitle = "Статус")
    private String limitType;
    @QuikAttr(title = "Тип раздела", shortTitle = "Тип раздела")
    private String limitType;
    @QuikAttr(title = "Запрет необеспеченных продаж", shortTitle = "Запрет необеспеч. продаж")
    private String limitType;
    @QuikAttr(title = "Расчетная организация по T0", shortTitle = "Расч. орг. по T0")
    private String limitType;
    @QuikAttr(title = "Расчетная организация по T+", shortTitle = "Расч. орг. по T+")
    private String limitType;
    @QuikAttr(title = "Раздел счета депо", shortTitle = "Раздел счета депо")
    private String limitType;
}
