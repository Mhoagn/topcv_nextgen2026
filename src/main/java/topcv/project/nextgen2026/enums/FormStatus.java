package topcv.project.nextgen2026.enums;

public enum FormStatus {
    ACTIVE("active"),
    DRAFT("draft");

    private final String value;

    FormStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}