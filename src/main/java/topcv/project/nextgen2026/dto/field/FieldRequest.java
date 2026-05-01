package topcv.project.nextgen2026.dto.field;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import topcv.project.nextgen2026.enums.FieldType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldRequest {
    
    @NotBlank(message = "Email không được để trống")
    private String email;
    
    @NotBlank(message = "Label không được để trống")
    private String label;
    
    @NotNull(message = "Type không được để trống")
    private FieldType type;

    @Min(value = 1, message = "Thứ tự hiển thị phải >= 1")
    private Integer displayOrder;
    
    @NotNull(message = "Required không được để trống")
    private Boolean required;
    
    private String options;
}
