package topcv.project.nextgen2026.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.AuthRequest;
import topcv.project.nextgen2026.dto.form.DetailedFormResponse;
import topcv.project.nextgen2026.service.interf.FormDetailInterface;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormDetailController {
    private final FormDetailInterface formDetailService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DetailedFormResponse>> getFormById(
            @PathVariable String id,
            @Valid @RequestBody AuthRequest authRequest) {

        ApiResponse<DetailedFormResponse> response = formDetailService.getFormById(
                authRequest.getEmail(), id);

        return ResponseEntity.ok(response);
    }
}
