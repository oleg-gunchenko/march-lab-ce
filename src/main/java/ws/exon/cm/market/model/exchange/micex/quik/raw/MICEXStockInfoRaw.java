package ws.exon.cm.market.model.exchange.micex.quik.raw;

import ws.exon.cm.market.model.exchange.micex.quik.meta.QuikAttr;

import java.math.BigDecimal;

public class MICEXStockInfoRaw extends MICEXCommonInfoRaw {
    @QuikAttr(title = "ISIN", shortTitle = "ISIN") private String isin;
    @QuikAttr(title = "Регистрационный номер", shortTitle = "Рег. номер") private String registrationNumber;
    @QuikAttr(title = "Уровень листинга", shortTitle = "Листинг") private Integer quotationTier;
    @QuikAttr(title = "Дата закрытия реестра", shortTitle = "Дата з.р.") private String registryClosingDate;
    @QuikAttr(title = "Величина дивидендов", shortTitle = "Дивиденды") private BigDecimal dividendAmount;
    @QuikAttr(title = "Сопряженная валюта", shortTitle = "Сопр. валюта") private String conjugateCurrency;
    @QuikAttr(title = "ID аукциона", shortTitle = "ID аукциона") private String auctionID;
    @QuikAttr(title = "Время начала аукциона план", shortTitle = "Начало аук. план") private String scheduledAuctionStartTime;
    @QuikAttr(title = "Базис РЕПО", shortTitle = "Базис РЕПО") private String basisREPO;
}
