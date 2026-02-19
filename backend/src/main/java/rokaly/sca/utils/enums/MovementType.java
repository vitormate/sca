package rokaly.sca.utils.enums;

public enum MovementType {

    IN("ENTRADA"),
    OUT("SAÍDA"),
    MOVEMENT("MOVIMENTAÇÃO");

    private final String type;

    MovementType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
