package topcv.project.nextgen2026.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import topcv.project.nextgen2026.dto.field.FieldResponse;
import topcv.project.nextgen2026.enums.FormStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailedFormResponse {
    private String id;
    private String title;
    private String description;
    private FormStatus status;
    private List<FieldResponse> fieldResponseList;
}
