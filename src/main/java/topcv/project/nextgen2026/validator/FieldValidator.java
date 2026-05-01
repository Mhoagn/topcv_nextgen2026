package topcv.project.nextgen2026.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import topcv.project.nextgen2026.dto.field.FieldRequest;
import topcv.project.nextgen2026.enums.FieldType;
import topcv.project.nextgen2026.exception.ValidationException;
import topcv.project.nextgen2026.helper.FieldOptionsHelper;

@Component
@RequiredArgsConstructor
public class FieldValidator {
    
    private final FieldOptionsHelper fieldOptionsHelper;

    public void validate(FieldRequest request) {
        validateLabel(request.getLabel());
        validateType(request.getType());
        validateOptions(request.getType(), request.getOptions());
        validateRequired(request.getRequired());
    }

    private void validateLabel(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new ValidationException("Label không được để trống");
        }

        if (label.length() > 200) {
            throw new ValidationException("Label không được quá 200 ký tự");
        }
    }

    private void validateType(FieldType type) {
        if (type == null) {
            throw new ValidationException("Type không được để trống");
        }
    }

    private void validateOptions(FieldType type, String options) {
        if (type == null) {
            return;
        }

        switch (type) {
            case TEXT -> validateTextOptions(options);
            case NUMBER -> validateNumberOptions(options);
            case COLOR -> validateColorOptions(options);
            case SELECT -> validateSelectOptions(options);
            case DATE -> {}
        }
    }

    private void validateTextOptions(String options) {
        if (options != null && !options.trim().isEmpty()) {
            fieldOptionsHelper.parseTextMaxLength(options);
        }
    }

    private void validateNumberOptions(String options) {
        if (options != null && !options.trim().isEmpty()) {
            fieldOptionsHelper.parseNumberRange(options);
        }
    }

    private void validateColorOptions(String options) {
        if (options != null) {
            String trimmed = options.trim();
            if (!trimmed.isEmpty() && !trimmed.matches("^#[0-9A-Fa-f]{6}$")) {
                throw new ValidationException("Options cho color phải là mã HEX hợp lệ (#RRGGBB)");
            }
        }
    }

    private void validateSelectOptions(String options) {
        fieldOptionsHelper.parseSelectOptions(options);
    }

    private void validateRequired(Boolean required) {
        if (required == null) {
            throw new ValidationException("Required không được để trống");
        }
    }
}
