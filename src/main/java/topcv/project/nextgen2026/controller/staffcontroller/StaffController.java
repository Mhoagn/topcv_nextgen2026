package topcv.project.nextgen2026.controller.staffcontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.AuthRequest;
import topcv.project.nextgen2026.dto.PageResponse;
import topcv.project.nextgen2026.dto.form.FormResponse;
import topcv.project.nextgen2026.dto.submission.SubmissionRequest;
import topcv.project.nextgen2026.dto.submission.SubmissionResponse;
import topcv.project.nextgen2026.service.interf.staffinterface.StaffInterface;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StaffController {
    private final StaffInterface staffService;

    @GetMapping("/forms/active")
    public ResponseEntity<ApiResponse<PageResponse<FormResponse>>> getAllActiveForms(
            @Valid @RequestBody AuthRequest authRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        ApiResponse<PageResponse<FormResponse>> response = staffService.getAllActiveForms(
                authRequest.getEmail(), pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forms/{id}/submit")
    public ResponseEntity<ApiResponse<SubmissionResponse>> submitForm(
            @PathVariable String id,
            @Valid @RequestBody SubmissionRequest submissionRequest) {

        ApiResponse<SubmissionResponse> response = staffService.submitForm(id, submissionRequest);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/submissions")
    public ResponseEntity<ApiResponse<PageResponse<SubmissionResponse>>> getMySubmissions(
            @Valid @RequestBody AuthRequest authRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        ApiResponse<PageResponse<SubmissionResponse>> response = staffService.getMySubmissions(
                authRequest.getEmail(), pageable);

        return ResponseEntity.ok(response);
    }

}
