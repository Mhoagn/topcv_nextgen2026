package topcv.project.nextgen2026.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import topcv.project.nextgen2026.entity.Field;
import topcv.project.nextgen2026.exception.ValidationException;
import topcv.project.nextgen2026.helper.FieldOptionsHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubmissionValidator {

    private static final Pattern COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final FieldOptionsHelper fieldOptionsHelper;

    public void validateSubmission(List<Field> fields, Map<String, String> values) {
        log.info("Validating submission with {} fields", fields.size());

        for (Field field : fields) {
            String fieldId = field.getId();
            String value = values.get(fieldId);

            if (field.getRequired() && (value == null || value.trim().isEmpty())) {
                throw new ValidationException("Field '" + field.getLabel() + "' là bắt buộc");
            }

            if (value != null && !value.trim().isEmpty()) {
                validateFieldValue(field, value);
            }
        }

        log.info("Submission validation passed");
    }

    private void validateFieldValue(Field field, String value) {
        switch (field.getType()) {
            case TEXT:
                validateText(field, value);
                break;
            case NUMBER:
                validateNumber(field, value);
                break;
            case DATE:
                validateDate(field, value);
                break;
            case COLOR:
                validateColor(field, value);
                break;
            case SELECT:
                validateSelect(field, value);
                break;
            default:
                throw new ValidationException("Loại field không hợp lệ: " + field.getType());
        }
    }

    private void validateText(Field field, String value) {
        int maxLength = fieldOptionsHelper.parseTextMaxLengthSafe(field.getOptions());
        
        if (value.length() > maxLength) {
            throw new ValidationException("Field '" + field.getLabel() + "' không được vượt quá " + maxLength + " ký tự");
        }
    }

    private void validateNumber(Field field, String value) {
        try {
            double number = Double.parseDouble(value);
            
            FieldOptionsHelper.NumberRange range = fieldOptionsHelper.parseNumberRangeSafe(field.getOptions());
            
            if (number < range.min || number > range.max) {
                throw new ValidationException("Field '" + field.getLabel() + "' phải có giá trị từ " 
                    + (int)range.min + " đến " + (int)range.max);
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Field '" + field.getLabel() + "' phải là số hợp lệ");
        }
    }

    private void validateDate(Field field, String value) {
        try {
            LocalDate date = LocalDate.parse(value, DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            
            if (date.isBefore(today)) {
                throw new ValidationException("Field '" + field.getLabel() + "' không được chọn ngày quá khứ");
            }
        } catch (DateTimeParseException e) {
            throw new ValidationException("Field '" + field.getLabel() + "' phải có định dạng yyyy-MM-dd");
        }
    }

    private void validateColor(Field field, String value) {
        if (!COLOR_PATTERN.matcher(value).matches()) {
            throw new ValidationException("Field '" + field.getLabel() + "' phải là mã màu hex hợp lệ (ví dụ: #FF5733)");
        }
    }

    private void validateSelect(Field field, String value) {
        try {
            List<String> options = fieldOptionsHelper.parseSelectOptions(field.getOptions());
            
            if (!options.contains(value)) {
                throw new ValidationException("Field '" + field.getLabel() + "' có giá trị không hợp lệ. Các giá trị cho phép: " + String.join(", ", options));
            }
        } catch (ValidationException e) {
            throw new ValidationException("Field '" + field.getLabel() + "' không có options được cấu hình hợp lệ");
        }
    }
}
