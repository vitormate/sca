package rokaly.sca.utils.enums;

public enum PickingOrderStatus {

    CREATED("Criado"),
    ASSIGNED("Atribuído"),
    FINISHED("Finalizado"),
    CANCELED("Cancelado");

    private final String status;

    PickingOrderStatus(String status) {
        this.status = status;
    }

    public String getStatusPT() {
        return status;
    }
}
