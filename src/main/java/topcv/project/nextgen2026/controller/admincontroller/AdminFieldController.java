package topcv.project.nextgen2026.controller.admincontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.AuthRequest;
import topcv.project.nextgen2026.dto.field.FieldRequest;
import topcv.project.nextgen2026.dto.field.FieldResponse;
import topcv.project.nextgen2026.dto.field.UpdateFieldRequest;
import topcv.project.nextgen2026.service.interf.admininterface.AdminFieldInterface;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class AdminFieldController {
    private final AdminFieldInterface adminFieldService;

    @PostMapping("/{id}/fields")
    public ResponseEntity<ApiResponse<FieldResponse>> addFieldToForm(
            @PathVariable String id,
            @Valid @RequestBody FieldRequest fieldRequest) {

        ApiResponse<FieldResponse> response = adminFieldService.addFieldToForm(id, fieldRequest);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}/fields/{fid}")
    public ResponseEntity<ApiResponse<FieldResponse>> updateField(
            @PathVariable String id,
            @PathVariable String fid,
            @Valid @RequestBody UpdateFieldRequest fieldRequest) {

        ApiResponse<FieldResponse> response = adminFieldService.updateField(id, fid, fieldRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/fields/{fid}")
    public ResponseEntity<ApiResponse<Void>> deleteField(
            @PathVariable String id,
            @PathVariable String fid,
            @Valid @RequestBody AuthRequest authRequest) {

        ApiResponse<Void> response = adminFieldService.deleteField(
                authRequest.getEmail(), id,fid);

        return ResponseEntity.ok(response);
    }
}
