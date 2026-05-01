package topcv.project.nextgen2026.service.imple.adminservice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.PageResponse;
import topcv.project.nextgen2026.dto.form.DetailedFormResponse;
import topcv.project.nextgen2026.dto.form.FormRequest;
import topcv.project.nextgen2026.dto.form.FormResponse;
import topcv.project.nextgen2026.entity.Form;
import topcv.project.nextgen2026.entity.User;
import topcv.project.nextgen2026.enums.FormStatus;
import topcv.project.nextgen2026.enums.UserRole;
import topcv.project.nextgen2026.exception.NotFoundException;
import topcv.project.nextgen2026.exception.UnauthorizedException;
import topcv.project.nextgen2026.exception.ValidationException;
import topcv.project.nextgen2026.helper.FieldDisplayOrderHelper;
import topcv.project.nextgen2026.helper.FormDisplayOrderHelper;
import topcv.project.nextgen2026.mapper.FieldMapper;
import topcv.project.nextgen2026.mapper.FormMapper;
import topcv.project.nextgen2026.mapper.ResponseMapper;
import topcv.project.nextgen2026.repository.FieldRepository;
import topcv.project.nextgen2026.repository.FormRepository;
import topcv.project.nextgen2026.repository.UserRepository;
import topcv.project.nextgen2026.service.interf.admininterface.AdminFormInterface;
import topcv.project.nextgen2026.validator.FieldValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminFormService implements AdminFormInterface {

    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final FormMapper formMapper;
    private final ResponseMapper responseMapper;
    private final FormDisplayOrderHelper formDisplayOrderHelper;

    @Override
    public ApiResponse<PageResponse<FormResponse>> getAllForms(String email, Pageable pageable) {
        log.info("Getting all forms for email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.ADMIN) {
            log.warn("Unauthorized access attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ admin mới có quyền xem danh sách form");
        }
        
        Page<Form> formPage = formRepository.findAll(pageable);
        
        List<FormResponse> formResponses = formPage.getContent().stream()
                .map(formMapper::toFormResponse)
                .collect(Collectors.toList());
        
        log.info("Retrieved {} forms for admin: {}", formResponses.size(), email);
        
        PageResponse<FormResponse> pageResponse = responseMapper.toPageResponse(formResponses, formPage);
        
        return responseMapper.toApiResponse(pageResponse, "Lấy danh sách form thành công");
    }



    @Override
    public ApiResponse<FormResponse> createForm(FormRequest request) {
        String email = request.getEmail();
        log.info("Creating new form for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.ADMIN) {
            log.warn("Unauthorized form creation attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ admin mới có quyền tạo form");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new ValidationException("Tiêu đề không được để trống khi tạo form");
        }

        Integer displayOrder = request.getDisplayOrder() != null ? request.getDisplayOrder() : 1;
        formDisplayOrderHelper.makeRoomForNewForm(displayOrder);

        Form form = new Form();
        form.setTitle(request.getTitle());
        form.setDescription(request.getDescription());
        form.setDisplayOrder(displayOrder);
        form.setStatus(request.getStatus() != null ? request.getStatus() : FormStatus.DRAFT);
        form.setCreatedBy(user);

        Form savedForm = formRepository.save(form);
        
        log.info("Form created successfully with id: {} by admin: {}", savedForm.getId(), email);

        FormResponse formResponse = formMapper.toFormResponse(savedForm);

        return responseMapper.toApiResponse(formResponse, "Tạo form mới thành công", 201);
    }


    @Override
    public ApiResponse<FormResponse> updateForm(String formId, FormRequest request) {
        String email = request.getEmail();
        log.info("Updating form {} by email: {}", formId, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.ADMIN) {
            log.warn("Unauthorized form update attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ admin mới có quyền cập nhật form");
        }

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException("Form không tồn tại với id: " + formId));

        Integer oldDisplayOrder = form.getDisplayOrder();

        if (request.getTitle() != null) {
            form.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            form.setDescription(request.getDescription());
        }
        if (request.getDisplayOrder() != null && !request.getDisplayOrder().equals(oldDisplayOrder)) {
            formDisplayOrderHelper.moveForm(formId, oldDisplayOrder, request.getDisplayOrder());
            form.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getStatus() != null) {
            form.setStatus(request.getStatus());
        }

        Form updatedForm = formRepository.save(form);
        
        log.info("Form {} updated successfully by admin: {}", formId, email);

        FormResponse formResponse = formMapper.toFormResponse(updatedForm);

        return responseMapper.toApiResponse(formResponse, "Cập nhật form thành công");
    }

    @Override
    public ApiResponse<Void> deleteForm(String email, String formId) {
        log.info("Deleting form {} by email: {}", formId, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.ADMIN) {
            log.warn("Unauthorized form deletion attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ admin mới có quyền xóa form");
        }

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException("Form không tồn tại với id: " + formId));

        Integer deletedPosition = form.getDisplayOrder();
        
        formRepository.delete(form);
        log.info("Form {} deleted successfully", formId);

        formDisplayOrderHelper.fillGapAfterDelete(deletedPosition);

        return responseMapper.toSuccessResponse("Xóa form thành công");
    }
}

