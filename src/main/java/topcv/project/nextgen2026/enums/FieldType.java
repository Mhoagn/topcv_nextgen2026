package topcv.project.nextgen2026.enums;

public enum FieldType {
    TEXT("text"),
    NUMBER("number"),
    DATE("date"),
    COLOR("color"),
    SELECT("select");

    private final String value;

    FieldType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
