package rokaly.sca.utils.enums;

public enum StatusProduct {

    ACTIVE("ATIVO"),
    INACTIVE("INATIVO");

    private final String status;

    StatusProduct(String status) {
        this.status = status;
    }

    public String getStatusPT() {
        return status;
    }
}
