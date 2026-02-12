package rokaly.sca.dto;

import java.math.BigDecimal;

public record StockEntryResponse(String productName, String positionCode, BigDecimal amount) {
}
