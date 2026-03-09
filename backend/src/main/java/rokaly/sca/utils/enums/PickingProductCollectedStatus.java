package rokaly.sca.utils.enums;

public enum PickingProductCollectedStatus {

    WAITING("Aguardando"),
    IN_PROGRESS("Em progresso"),
    PARTIAL("Parcial"),
    COMPLETED("Completo");

    private final String statusCollected;

    PickingProductCollectedStatus(String statusCollected) {
        this.statusCollected = statusCollected;
    }

    public String getStatusCollectedPT() {
        return statusCollected;
    }
}
