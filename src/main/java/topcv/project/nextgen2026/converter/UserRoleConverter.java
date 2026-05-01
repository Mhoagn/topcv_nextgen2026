package topcv.project.nextgen2026.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import topcv.project.nextgen2026.enums.UserRole;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole userRole) {
        if (userRole == null) {
            return null;
        }
        return userRole.getValue();
    }

    @Override
    public UserRole convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        
        for (UserRole role : UserRole.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }
        
        throw new IllegalArgumentException("Unknown UserRole value: " + value);
    }
}
