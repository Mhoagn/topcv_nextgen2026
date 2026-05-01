package topcv.project.nextgen2026.service.interf.staffinterface;

import org.springframework.data.domain.Pageable;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.PageResponse;
import topcv.project.nextgen2026.dto.form.FormResponse;
import topcv.project.nextgen2026.dto.submission.SubmissionRequest;
import topcv.project.nextgen2026.dto.submission.SubmissionResponse;

public interface StaffInterface {
    ApiResponse<PageResponse<FormResponse>> getAllActiveForms(String email, Pageable pageable);
    
    ApiResponse<SubmissionResponse> submitForm(String formId, SubmissionRequest request);
    
    ApiResponse<PageResponse<SubmissionResponse>> getMySubmissions(String email, Pageable pageable);
}
