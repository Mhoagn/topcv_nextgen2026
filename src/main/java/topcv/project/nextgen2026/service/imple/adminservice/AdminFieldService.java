package topcv.project.nextgen2026.service.imple.adminservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.field.FieldRequest;
import topcv.project.nextgen2026.dto.field.FieldResponse;
import topcv.project.nextgen2026.dto.field.UpdateFieldRequest;
import topcv.project.nextgen2026.entity.Field;
import topcv.project.nextgen2026.entity.Form;
import topcv.project.nextgen2026.entity.User;
import topcv.project.nextgen2026.enums.UserRole;
import topcv.project.nextgen2026.exception.NotFoundException;
import topcv.project.nextgen2026.exception.UnauthorizedException;
import topcv.project.nextgen2026.helper.FieldDisplayOrderHelper;
import topcv.project.nextgen2026.helper.FormDisplayOrderHelper;
import topcv.project.nextgen2026.mapper.FieldMapper;
import topcv.project.nextgen2026.mapper.FormMapper;
import topcv.project.nextgen2026.mapper.ResponseMapper;
import topcv.project.nextgen2026.repository.FieldRepository;
import topcv.project.nextgen2026.repository.FormRepository;
import topcv.project.nextgen2026.repository.UserRepository;
import topcv.project.nextgen2026.service.interf.admininterface.AdminFieldInterface;
import topcv.project.nextgen2026.validator.FieldValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminFieldService implements AdminFieldInterface {
    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;
    private final FieldMapper fieldMapper;
    private final ResponseMapper responseMapper;
    private final FieldDisplayOrderHelper fieldDisplayOrderHelper;
    private final FieldValidator fieldValidator;

    @Override
    public ApiResponse<FieldResponse> addFieldToForm(String formId, FieldRequest request) {
        String email = request.getEmail();
        log.info("Adding field to form {} by email: {}", formId, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.ADMIN) {
            log.warn("Unauthorized field creation attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ admin mới có quyền thêm field vào form");
        }

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException("Form không tồn tại với id: " + formId));

        fieldValidator.validate(request);

        Integer displayOrder = request.getDisplayOrder() != null
                ? request.getDisplayOrder()
                : form.getFields().size() + 1;

        fieldDisplayOrderHelper.makeRoomForNewField(formId, displayOrder);

        Field field = new Field();
        field.setForm(form);
        field.setLabel(request.getLabel());
        field.setType(request.getType());
        field.setRequired(request.getRequired());
        field.setOptions(request.getOptions());
        field.setDisplayOrder(displayOrder);

        Field savedField = fieldRepository.save(field);

        log.info("Field created successfully with id: {} for form: {}", savedField.getId(), formId);

        FieldResponse fieldResponse = fieldMapper.toFieldResponse(savedField);

        return responseMapper.toApiResponse(fieldResponse, "Thêm field vào form thành công", 201);
    }

    @Override
    public ApiResponse<FieldResponse> updateField(String formId, String fieldId, UpdateFieldRequest request) {
        String email = request.getEmail();
        log.info("Updating field {} in form {} by email: {}", fieldId, formId, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.ADMIN) {
            log.warn("Unauthorized field update attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ admin mới có quyền cập nhật field");
        }

        formRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException("Form không tồn tại với id: " + formId));

        Field field = fieldRepository.findByIdAndFormId(fieldId, formId)
                .orElseThrow(() -> new NotFoundException("Field không tồn tại hoặc không thuộc form này"));

        //fieldValidator.validate(request);

        Integer oldDisplayOrder = field.getDisplayOrder();

        if (request.getLabel() != null) {
            field.setLabel(request.getLabel());
        }
        if (request.getType() != null) {
            field.setType(request.getType());
        }
        if (request.getRequired() != null) {
            field.setRequired(request.getRequired());
        }
        if (request.getOptions() != null) {
            field.setOptions(request.getOptions());
        }
        if (request.getDisplayOrder() != null && !request.getDisplayOrder().equals(oldDisplayOrder)) {
            fieldDisplayOrderHelper.moveField(formId, fieldId, oldDisplayOrder, request.getDisplayOrder());
            field.setDisplayOrder(request.getDisplayOrder());
        }

        Field updatedField = fieldRepository.save(field);

        log.info("Field {} updated successfully in form {}", fieldId, formId);

        FieldResponse fieldResponse = fieldMapper.toFieldResponse(updatedField);

        return responseMapper.toApiResponse(fieldResponse, "Cập nhật field thành công");
    }

    @Override
    public ApiResponse<Void> deleteField(String email, String formId, String fieldId) {
        log.info("Deleting field {} by email: {}", fieldId, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.ADMIN) {
            log.warn("Unauthorized form deletion attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ admin mới có quyền xóa field");
        }

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException("Form không tồn tại với id: " + formId));

        Field field = fieldRepository.findByIdAndFormId(fieldId, formId)
                .orElseThrow(() -> new NotFoundException("Field không tồn tại hoặc không thuộc form này"));

        Integer deletedPosition = field.getDisplayOrder();

        fieldRepository.delete(field);
        log.info("Field {} deleted successfully", fieldId);
        fieldDisplayOrderHelper.fillGapAfterFieldDelete(formId,deletedPosition);

        return responseMapper.toSuccessResponse("Xóa field thành công");
    }
}
