package rokaly.sca.utils.enums;

public enum PickingProductStatus {

    WAITING("Aguardando"),
    PICKING("Separando"),
    COLLECTED("Coletado");

    private final String status;

    PickingProductStatus(String status) {
        this.status = status;
    }

    public String getStatusPT() {
        return status;
    }
}
