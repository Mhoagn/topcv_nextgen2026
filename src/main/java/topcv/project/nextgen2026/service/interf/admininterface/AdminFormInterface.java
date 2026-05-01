package topcv.project.nextgen2026.service.interf.admininterface;

import org.springframework.data.domain.Pageable;

import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.PageResponse;
import topcv.project.nextgen2026.dto.form.DetailedFormResponse;
import topcv.project.nextgen2026.dto.form.FormRequest;
import topcv.project.nextgen2026.dto.form.FormResponse;

public interface AdminFormInterface {
    ApiResponse<PageResponse<FormResponse>> getAllForms(String email, Pageable pageable);
    ApiResponse<FormResponse> createForm(FormRequest request);
    ApiResponse<FormResponse> updateForm(String formId, FormRequest request);
    ApiResponse<Void> deleteForm(String email, String formId);
}
