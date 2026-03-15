package rokaly.sca.utils.mappers;

import rokaly.sca.entity.MovementStock;
import rokaly.sca.utils.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovementStockMapper {

    public static MovementStock createIn(String productCode, String productName, String positionCode, BigDecimal amount, String name, String reason) {
        MovementStock movementStock = new MovementStock();
        movementStock.setProductCode(productCode);
        movementStock.setProductName(productName);
        movementStock.setToPosition(positionCode);
        movementStock.setRequestedBy(name);
        movementStock.setAmount(amount);
        movementStock.setDateTime(LocalDateTime.now());
        movementStock.setMovementType(MovementType.IN);
        movementStock.setResponsible(name);
        movementStock.setReason(reason);
        return movementStock;
    }

    public static MovementStock createOut(String productCode, String productName, String positionCode, BigDecimal amount, String name) {
        MovementStock movementStock = new MovementStock();
        movementStock.setProductCode(productCode);
        movementStock.setProductName(productName);
        movementStock.setFromPosition(positionCode);
        movementStock.setRequestedBy(name);
        movementStock.setAmount(amount);
        movementStock.setDateTime(LocalDateTime.now());
        movementStock.setMovementType(MovementType.OUT);
        movementStock.setResponsible(name);
        movementStock.setReason("");
        return movementStock;
    }

    public static MovementStock createAjustment(String productCode, String productName, String positionCode, BigDecimal amount, String name, String reason) {
        MovementStock movementStock = new MovementStock();
        movementStock.setProductCode(productCode);
        movementStock.setProductName(productName);
        movementStock.setFromPosition(positionCode);
        movementStock.setRequestedBy(name);
        movementStock.setAmount(amount);
        movementStock.setDateTime(LocalDateTime.now());
        movementStock.setMovementType(MovementType.ADJUSTMENT);
        movementStock.setResponsible(name);
        movementStock.setReason(reason);
        return movementStock;
    }
}
