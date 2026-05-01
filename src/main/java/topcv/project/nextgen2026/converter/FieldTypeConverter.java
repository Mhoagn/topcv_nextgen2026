package topcv.project.nextgen2026.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import topcv.project.nextgen2026.enums.FieldType;

@Converter(autoApply = true)
public class FieldTypeConverter implements AttributeConverter<FieldType, String> {

    @Override
    public String convertToDatabaseColumn(FieldType type) {
        if (type == null) {
            return null;
        }
        return type.getValue();
    }

    @Override
    public FieldType convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        
        for (FieldType type : FieldType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Unknown FieldType value: " + value);
    }
}
