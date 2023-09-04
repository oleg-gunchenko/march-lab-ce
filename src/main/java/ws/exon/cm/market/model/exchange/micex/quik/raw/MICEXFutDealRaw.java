package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

public class MICEXFutDealRaw {
    @QuikAttr(title = "Номер", shortTitle = "Номер") private Long dealId;
    @QuikAttr(title = "Код инструмента", shortTitle = "Код инструмента") private String instrumentCode;
    @QuikAttr(title = "Код класса", shortTitle = "Код класса") private String instrumentClassCode;
    @QuikAttr(title = "Дата торгов", shortTitle = "Дата") private String tradingDate;
    @QuikAttr(title = "Время", shortTitle = "Время") private String time;
    @QuikAttr(title = "Время(мкс)", shortTitle = "Время(мкс)") private String mcs;
    @QuikAttr(title = "Операция", shortTitle = "Операция") private String operation;
    @QuikAttr(title = "Цена", shortTitle = "Цена") private BigDecimal price;
    @QuikAttr(title = "Количество", shortTitle = "Кол-во") private Integer qty;
    @QuikAttr(title = "Открытый интерес", shortTitle = "Откр. инт.") private Integer openInterest;
}
