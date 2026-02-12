package rokaly.sca.dto;

import rokaly.sca.entity.Stock;

import java.math.BigDecimal;

public record StockEntryResponse(String productCode, String productName, String positionCode, BigDecimal amount) {
    public StockEntryResponse(Stock s) {
        this(s.getProduct().getCode(), s.getProduct().getName(), s.getPosition().getCode(), s.getAmount());
    }
}
