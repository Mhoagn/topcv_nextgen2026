package topcv.project.nextgen2026.service.imple.staffservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.PageResponse;
import topcv.project.nextgen2026.dto.form.FormResponse;
import topcv.project.nextgen2026.dto.submission.SubmissionRequest;
import topcv.project.nextgen2026.dto.submission.SubmissionResponse;
import topcv.project.nextgen2026.entity.Field;
import topcv.project.nextgen2026.entity.Form;
import topcv.project.nextgen2026.entity.Submission;
import topcv.project.nextgen2026.entity.SubmissionValue;
import topcv.project.nextgen2026.entity.User;
import topcv.project.nextgen2026.enums.FormStatus;
import topcv.project.nextgen2026.enums.SubmissionStatus;
import topcv.project.nextgen2026.enums.UserRole;
import topcv.project.nextgen2026.exception.BadRequestException;
import topcv.project.nextgen2026.exception.NotFoundException;
import topcv.project.nextgen2026.exception.UnauthorizedException;
import topcv.project.nextgen2026.mapper.FormMapper;
import topcv.project.nextgen2026.mapper.ResponseMapper;
import topcv.project.nextgen2026.mapper.SubmissionMapper;
import topcv.project.nextgen2026.repository.FormRepository;
import topcv.project.nextgen2026.repository.SubmissionRepository;
import topcv.project.nextgen2026.repository.UserRepository;
import topcv.project.nextgen2026.service.interf.staffinterface.StaffInterface;
import topcv.project.nextgen2026.validator.SubmissionValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffService implements StaffInterface {
    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final FormMapper formMapper;
    private final ResponseMapper responseMapper;
    private final SubmissionMapper submissionMapper;
    private final SubmissionValidator submissionValidator;


    @Override
    public ApiResponse<PageResponse<FormResponse>> getAllActiveForms(String email, Pageable pageable) {
        log.info("Getting all active forms for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.STAFF) {
            log.warn("Unauthorized access attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ nhân viên mới có quyền xem danh sách active form");
        }

        Page<Form> formPage = formRepository.findByStatusOrderByDisplayOrderAsc(
                FormStatus.ACTIVE,
                pageable
        );

        List<FormResponse> formResponses = formPage.getContent().stream()
                .map(formMapper::toFormResponse)
                .collect(Collectors.toList());

        log.info("Retrieved {} active forms for staff: {}", formResponses.size(), email);

        PageResponse<FormResponse> pageResponse = responseMapper.toPageResponse(formResponses, formPage);

        return responseMapper.toApiResponse(pageResponse, "Lấy danh sách active form thành công");
    }

    @Override
    @Transactional
    public ApiResponse<SubmissionResponse> submitForm(String formId, SubmissionRequest request) {
        String email = request.getEmail();
        log.info("Staff {} submitting form {}", email, formId);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.STAFF) {
            log.warn("Unauthorized submission attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ nhân viên mới có quyền submit form");
        }

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException("Form không tồn tại với id: " + formId));

        if (form.getStatus() != FormStatus.ACTIVE) {
            throw new BadRequestException("Chỉ có thể submit form đang ở trạng thái ACTIVE");
        }

        List<Field> fields = form.getFields();
        if (fields.isEmpty()) {
            throw new BadRequestException("Form không có field nào để submit");
        }

        submissionValidator.validateSubmission(fields, request.getValues());

        Submission submission = new Submission();
        submission.setForm(form);
        submission.setSubmittedBy(user);
        submission.setStatus(SubmissionStatus.VALID);

        List<SubmissionValue> submissionValues = new ArrayList<>();
        for (Field field : fields) {
            String value = request.getValues().get(field.getId());
            
            if (value != null && !value.trim().isEmpty()) {
                SubmissionValue submissionValue = new SubmissionValue();
                submissionValue.setSubmission(submission);
                submissionValue.setField(field);
                submissionValue.setValue(value.trim());
                submissionValues.add(submissionValue);
            }
        }

        submission.setSubmissionValues(submissionValues);
        Submission savedSubmission = submissionRepository.save(submission);

        log.info("Submission {} created successfully for form {} by staff {}", 
                savedSubmission.getId(), formId, email);

        SubmissionResponse submissionResponse = submissionMapper.toSubmissionResponse(savedSubmission);

        return responseMapper.toApiResponse(submissionResponse, "Submit form thành công", 201);
    }

    @Override
    public ApiResponse<PageResponse<SubmissionResponse>> getMySubmissions(String email, Pageable pageable) {
        log.info("Getting submissions for staff: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.STAFF) {
            log.warn("Unauthorized access attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ nhân viên mới có quyền xem danh sách submission của mình");
        }

        Page<Submission> submissionPage = submissionRepository.findBySubmittedByOrderBySubmittedAtDesc(user, pageable);

        List<SubmissionResponse> submissionResponses = submissionPage.getContent().stream()
                .map(submissionMapper::toSubmissionResponse)
                .collect(Collectors.toList());

        log.info("Retrieved {} submissions for staff: {}", submissionResponses.size(), email);

        PageResponse<SubmissionResponse> pageResponse = responseMapper.toPageResponse(submissionResponses, submissionPage);

        return responseMapper.toApiResponse(pageResponse, "Lấy danh sách submission thành công");
    }
}
