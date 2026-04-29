package topcv.project.nextgen2026.enums;

public enum SubmissionStatus {
    VALID("valid"),
    INVALID("invalid");

    private final String value;

    SubmissionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
