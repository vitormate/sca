package rokaly.sca.utils.enums;

public enum PickingProductCollectedStatus {

    WAITING("Aguardando"),
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
