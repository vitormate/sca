package rokaly.sca.dto.response;

import rokaly.sca.entity.Stock;

import java.math.BigDecimal;

public record StockEntryResponse(Long id, String productCode, String productName, String positionCode, BigDecimal amount) {
    public StockEntryResponse(Stock s) {
        this(s.getId(), s.getProduct().getCode(), s.getProduct().getName(), s.getPosition().getCode(), s.getAmount());
    }
}
