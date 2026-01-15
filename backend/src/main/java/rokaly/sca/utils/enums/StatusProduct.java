package rokaly.sca.utils.enums;

public enum StatusProduct {

    ACTIVE("ATIVO"),
    INACTIVE("INATIVO");

    private String status;

    StatusProduct(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
