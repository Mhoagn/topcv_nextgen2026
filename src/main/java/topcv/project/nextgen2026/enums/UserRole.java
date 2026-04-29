package topcv.project.nextgen2026.enums;

public enum UserRole {
    ADMIN("admin"),
    STAFF("staff");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}