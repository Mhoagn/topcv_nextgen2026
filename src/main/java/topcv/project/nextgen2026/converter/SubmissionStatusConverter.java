package topcv.project.nextgen2026.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import topcv.project.nextgen2026.enums.SubmissionStatus;

@Converter(autoApply = true)
public class SubmissionStatusConverter implements AttributeConverter<SubmissionStatus, String> {

    @Override
    public String convertToDatabaseColumn(SubmissionStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public SubmissionStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        
        for (SubmissionStatus status : SubmissionStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        
        throw new IllegalArgumentException("Unknown SubmissionStatus value: " + value);
    }
}
