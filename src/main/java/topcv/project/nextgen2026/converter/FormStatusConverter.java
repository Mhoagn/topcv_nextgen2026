package topcv.project.nextgen2026.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import topcv.project.nextgen2026.enums.FormStatus;

@Converter(autoApply = true)
public class FormStatusConverter implements AttributeConverter<FormStatus, String> {

    @Override
    public String convertToDatabaseColumn(FormStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public FormStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        
        for (FormStatus status : FormStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        
        throw new IllegalArgumentException("Unknown FormStatus value: " + value);
    }
}
