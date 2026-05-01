package topcv.project.nextgen2026.dto.submission;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import topcv.project.nextgen2026.enums.SubmissionStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmissionResponse {

    private String id;
    
    private String formId;
    
    private String formTitle;
    
    private String submittedBy;
    
    private SubmissionStatus status;
    
    private LocalDateTime submittedAt;
    
    private Map<String, SubmissionValueResponse> values;
}
