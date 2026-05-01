package topcv.project.nextgen2026.service.interf.admininterface;

import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.field.FieldRequest;
import topcv.project.nextgen2026.dto.field.FieldResponse;
import topcv.project.nextgen2026.dto.field.UpdateFieldRequest;

public interface AdminFieldInterface {
    ApiResponse<FieldResponse> addFieldToForm(String formId, FieldRequest request);
    ApiResponse<FieldResponse> updateField(String formId, String fieldId, UpdateFieldRequest request);
    ApiResponse<Void> deleteField(String email, String formId,String fieldId);
}
