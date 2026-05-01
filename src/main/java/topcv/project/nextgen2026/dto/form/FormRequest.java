package topcv.project.nextgen2026.dto.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import topcv.project.nextgen2026.enums.FormStatus;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormRequest {
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
    
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;
    
    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;
    
    @Min(value = 1, message = "Thứ tự hiển thị phải >= 1")
    private Integer displayOrder;
    
    private FormStatus status;
}
