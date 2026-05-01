package topcv.project.nextgen2026.service.interf;

import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.form.DetailedFormResponse;

public interface FormDetailInterface {
    ApiResponse<DetailedFormResponse> getFormById(String email, String formId);
}
