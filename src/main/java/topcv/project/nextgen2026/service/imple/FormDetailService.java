package topcv.project.nextgen2026.service.imple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.form.DetailedFormResponse;
import topcv.project.nextgen2026.entity.Form;
import topcv.project.nextgen2026.entity.User;
import topcv.project.nextgen2026.enums.FormStatus;
import topcv.project.nextgen2026.enums.UserRole;
import topcv.project.nextgen2026.exception.NotFoundException;
import topcv.project.nextgen2026.exception.UnauthorizedException;
import topcv.project.nextgen2026.helper.FormDisplayOrderHelper;
import topcv.project.nextgen2026.mapper.FormMapper;
import topcv.project.nextgen2026.mapper.ResponseMapper;
import topcv.project.nextgen2026.repository.FormRepository;
import topcv.project.nextgen2026.repository.UserRepository;
import topcv.project.nextgen2026.service.interf.FormDetailInterface;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormDetailService implements FormDetailInterface {
    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final FormMapper formMapper;
    private final ResponseMapper responseMapper;

    @Override
    public ApiResponse<DetailedFormResponse> getFormById(String email, String formId) {
        log.info("Getting form detail for id: {} by email: {}", formId, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại với email: " + email));

        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.STAFF) {
            log.warn("Unauthorized form access attempt by user: {} with role: {}", email, user.getRole());
            throw new UnauthorizedException("Chỉ admin và staff mới có quyền xem chi tiết form");
        }

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException("Form không tồn tại với id: " + formId));

        if (user.getRole() == UserRole.STAFF && form.getStatus() == FormStatus.DRAFT) {
            log.warn("Staff {} attempted to access draft form {}", email, formId);
            throw new UnauthorizedException("Staff chỉ có quyền xem chi tiết form đang ở trạng thái ACTIVE");
        }

        log.info("Retrieved form {} with {} fields by {} (role: {})",
                formId, form.getFields().size(), email, user.getRole());

        DetailedFormResponse detailedFormResponse = formMapper.toDetailedFormResponse(form);

        return responseMapper.toApiResponse(detailedFormResponse, "Lấy chi tiết form thành công");
    }
}
