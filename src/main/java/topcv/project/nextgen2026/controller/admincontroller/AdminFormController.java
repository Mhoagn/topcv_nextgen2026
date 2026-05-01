package topcv.project.nextgen2026.controller.admincontroller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.AuthRequest;
import topcv.project.nextgen2026.dto.PageResponse;
import topcv.project.nextgen2026.dto.form.DetailedFormResponse;
import topcv.project.nextgen2026.dto.form.FormRequest;
import topcv.project.nextgen2026.dto.form.FormResponse;
import topcv.project.nextgen2026.service.interf.admininterface.AdminFormInterface;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class AdminFormController {

    private final AdminFormInterface adminFormService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FormResponse>>> getAllForms(
            @Valid @RequestBody AuthRequest authRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        ApiResponse<PageResponse<FormResponse>> response = adminFormService.getAllForms(
                authRequest.getEmail(), pageable);
        
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ApiResponse<FormResponse>> createForm(
            @Valid @RequestBody FormRequest formRequest) {
        
        ApiResponse<FormResponse> response = adminFormService.createForm(formRequest);
        
        return ResponseEntity.status(201).body(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FormResponse>> updateForm(
            @PathVariable String id,
            @Valid @RequestBody FormRequest formRequest) {
        
        ApiResponse<FormResponse> response = adminFormService.updateForm(id, formRequest);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteForm(
            @PathVariable String id,
            @Valid @RequestBody AuthRequest authRequest) {
        
        ApiResponse<Void> response = adminFormService.deleteForm(
                authRequest.getEmail(), id);
        
        return ResponseEntity.ok(response);
    }

}

