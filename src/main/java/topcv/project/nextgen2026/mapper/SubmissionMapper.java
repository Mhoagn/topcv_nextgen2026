package topcv.project.nextgen2026.mapper;

import org.springframework.stereotype.Component;
import topcv.project.nextgen2026.dto.submission.SubmissionResponse;
import topcv.project.nextgen2026.dto.submission.SubmissionValueResponse;
import topcv.project.nextgen2026.entity.Submission;
import topcv.project.nextgen2026.entity.SubmissionValue;

import java.util.HashMap;
import java.util.Map;

@Component
public class SubmissionMapper {

    public SubmissionResponse toSubmissionResponse(Submission submission) {
        Map<String, SubmissionValueResponse> valuesMap = new HashMap<>();
        
        for (SubmissionValue sv : submission.getSubmissionValues()) {
            SubmissionValueResponse valueResponse = SubmissionValueResponse.builder()
                    .fieldId(sv.getField().getId())
                    .fieldLabel(sv.getField().getLabel())
                    .fieldType(sv.getField().getType())
                    .value(sv.getValue())
                    .build();
            
            valuesMap.put(sv.getField().getId(), valueResponse);
        }
        
        return SubmissionResponse.builder()
                .id(submission.getId())
                .formId(submission.getForm().getId())
                .formTitle(submission.getForm().getTitle())
                .submittedBy(submission.getSubmittedBy().getEmail())
                .status(submission.getStatus())
                .submittedAt(submission.getSubmittedAt())
                .values(valuesMap)
                .build();
    }
}
