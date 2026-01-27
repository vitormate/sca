package rokaly.sca.utils.enums;

public enum StatusPosition {

    ACTIVA("ATIVA"),
    BLOCKED("BLOQUEADA");

    private String status;

    StatusPosition(String status) {
        this.status = status;
    }

    public String getStatusPT() {
        return status;
    }
}
