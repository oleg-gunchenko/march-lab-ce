package ws.exon.cm.market.model.core.common;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ws.exon.cm.market.model.core.cep.PriceLong;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Instrument implements Comparable<Asset> {
    @EqualsAndHashCode.Include
    private UUID id;

    private Symbol symbol;
    private UUID providerId;
    private String client;

    private BigDecimal priceStepSize;
    private BigDecimal priceStepCost;
    private int pricePrecision;

    private BigDecimal minVolumeSize;
    private int volumePrecision;

    private int oiPrecision;

    public Price getPrice(final LocalDate date) {
        return null;
    }

    @Override
    public int compareTo(Asset o) {
        return id.compareTo(o.id);
    }
}
