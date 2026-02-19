package rokaly.sca.utils.enums;

public enum PickingOrderStatus {

    CREATED("Criado"),
    ASSIGNED("Atribuído"),
    IN_PROGRESS("Em progresso"),
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
