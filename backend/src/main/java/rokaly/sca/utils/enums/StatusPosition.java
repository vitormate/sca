package rokaly.sca.utils.enums;

public enum StatusPosition {

    ACTIVE("ATIVA"),
    BLOCKED("BLOQUEADA"),
    INACTIVE("INATIVA");

    private final String status;

    StatusPosition(String status) {
        this.status = status;
    }

    public String getStatusPT() {
        return status;
    }
}
