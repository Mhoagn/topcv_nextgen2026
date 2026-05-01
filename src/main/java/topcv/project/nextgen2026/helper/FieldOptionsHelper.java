package topcv.project.nextgen2026.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import topcv.project.nextgen2026.exception.ValidationException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FieldOptionsHelper {

    private final ObjectMapper objectMapper;

    public int parseTextMaxLength(String options) {
        if (options == null || options.trim().isEmpty()) {
            return 200;
        }
        
        try {
            int maxLength = Integer.parseInt(options.trim());
            if (maxLength <= 0 || maxLength > 200) {
                throw new ValidationException("Độ dài text phải từ 1 đến 200 ký tự");
            }
            return maxLength;
        } catch (NumberFormatException e) {
            throw new ValidationException("Options cho text phải là số nguyên");
        }
    }

    public int parseTextMaxLengthSafe(String options) {
        if (options == null || options.trim().isEmpty()) {
            return 200;
        }
        
        try {
            int maxLength = Integer.parseInt(options.trim());
            if (maxLength <= 0 || maxLength > 200) {
                return 200;
            }
            return maxLength;
        } catch (NumberFormatException e) {
            log.warn("Invalid text options, using default 200");
            return 200;
        }
    }

    public NumberRange parseNumberRange(String options) {
        if (options == null || options.trim().isEmpty()) {
            return new NumberRange(0, 100);
        }
        
        String[] parts = options.trim().split(",");
        if (parts.length != 2) {
            throw new ValidationException("Options cho number phải có định dạng 'min,max'");
        }
        
        try {
            double min = Double.parseDouble(parts[0].trim());
            double max = Double.parseDouble(parts[1].trim());
            
            if (min > max) {
                throw new ValidationException("Giá trị min phải nhỏ hơn hoặc bằng max");
            }
            if (min < 0 || max > 100) {
                throw new ValidationException("Giá trị phải từ 0 đến 100");
            }
            
            return new NumberRange(min, max);
        } catch (NumberFormatException e) {
            throw new ValidationException("Options cho number phải có định dạng 'min,max' với min và max là số");
        }
    }

    public NumberRange parseNumberRangeSafe(String options) {
        if (options == null || options.trim().isEmpty()) {
            return new NumberRange(0, 100);
        }
        
        String[] parts = options.trim().split(",");
        if (parts.length != 2) {
            log.warn("Invalid number options format, using default 0-100");
            return new NumberRange(0, 100);
        }
        
        try {
            double min = Double.parseDouble(parts[0].trim());
            double max = Double.parseDouble(parts[1].trim());
            return new NumberRange(min, max);
        } catch (NumberFormatException e) {
            log.warn("Invalid number options, using default 0-100");
            return new NumberRange(0, 100);
        }
    }

    public List<String> parseSelectOptions(String options) {
        if (options == null || options.trim().isEmpty()) {
            throw new ValidationException("Options không được để trống cho field type select");
        }
        
        try {
            List<String> optionList = objectMapper.readValue(options, new TypeReference<List<String>>() {});
            if (optionList.isEmpty()) {
                throw new ValidationException("Select phải có ít nhất 1 option");
            }
            return optionList;
        } catch (Exception e) {
            log.error("Error parsing select options: {}", e.getMessage());
            throw new ValidationException("Options cho select phải là JSON array hợp lệ");
        }
    }

    public static class NumberRange {
        public final double min;
        public final double max;
        
        public NumberRange(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }
}
