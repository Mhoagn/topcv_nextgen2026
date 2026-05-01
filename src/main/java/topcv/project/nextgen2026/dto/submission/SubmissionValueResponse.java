package topcv.project.nextgen2026.dto.submission;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import topcv.project.nextgen2026.enums.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmissionValueResponse {

    private String fieldId;
    
    private String fieldLabel;
    
    private FieldType fieldType;
    
    private String value;
}
