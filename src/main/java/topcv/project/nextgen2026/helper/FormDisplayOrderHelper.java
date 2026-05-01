package topcv.project.nextgen2026.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import topcv.project.nextgen2026.entity.Form;
import topcv.project.nextgen2026.repository.FormRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormDisplayOrderHelper {

    private final FormRepository formRepository;

    /**
     * Tạo khoảng trống cho form mới
     * @param targetPosition Vị trí cần chèn form mới vào
     */
    public void makeRoomForNewForm(Integer targetPosition) {
        List<Form> formsToShift = formRepository.findByDisplayOrderGreaterThanEqual(targetPosition);
        if (!formsToShift.isEmpty()) {
            log.info("Shifting {} forms down to make room at position {}", formsToShift.size(), targetPosition);
            for (Form form : formsToShift) {
                form.setDisplayOrder(form.getDisplayOrder() + 1);
                formRepository.save(form);
            }
        }
    }

    /**
     * Di chuyển form từ vị trí cũ sang vị trí mới, tự động shift các form khác
     * @param formId ID của form cần di chuyển
     * @param oldPosition Vị trí cũ
     * @param newPosition Vị trí mới
     */
    public void moveForm(String formId, Integer oldPosition, Integer newPosition) {
        if (oldPosition.equals(newPosition)) {
            return;
        }

        if (newPosition < oldPosition) {
            shiftFormsDown(formId, newPosition, oldPosition - 1);
        } else {
            shiftFormsUp(formId, oldPosition + 1, newPosition);
        }
    }

    /**
     * Shift các form xuống (tăng displayOrder) trong khoảng từ startPos đến endPos
     * Dùng khi di chuyển form lên trên (giảm displayOrder)
     */
    private void shiftFormsDown(String excludeFormId, Integer startPos, Integer endPos) {
        List<Form> formsToShift = formRepository.findByDisplayOrderBetween(startPos, endPos);
        for (Form form : formsToShift) {
            if (!form.getId().equals(excludeFormId)) {
                form.setDisplayOrder(form.getDisplayOrder() + 1);
                formRepository.save(form);
            }
        }
        log.info("Shifted {} forms down (positions {}-{})", formsToShift.size(), startPos, endPos);
    }

    /**
     * Shift các form lên (giảm displayOrder) trong khoảng từ startPos đến endPos
     * Dùng khi di chuyển form xuống dưới (tăng displayOrder)
     */
    private void shiftFormsUp(String excludeFormId, Integer startPos, Integer endPos) {
        List<Form> formsToShift = formRepository.findByDisplayOrderBetween(startPos, endPos);
        for (Form form : formsToShift) {
            if (!form.getId().equals(excludeFormId)) {
                form.setDisplayOrder(form.getDisplayOrder() - 1);
                formRepository.save(form);
            }
        }
        log.info("Shifted {} forms up (positions {}-{})", formsToShift.size(), startPos, endPos);
    }

    /**
     * Shift các form lên sau khi xóa 1 form
     * Các form phía sau vị trí bị xóa sẽ giảm displayOrder đi 1
     * @param deletedPosition Vị trí của form bị xóa
     */
    public void fillGapAfterDelete(Integer deletedPosition) {
        List<Form> formsToShift = formRepository.findByDisplayOrderGreaterThanEqual(deletedPosition + 1);
        if (!formsToShift.isEmpty()) {
            log.info("Shifting {} forms up to fill gap at position {}", formsToShift.size(), deletedPosition);
            for (Form form : formsToShift) {
                form.setDisplayOrder(form.getDisplayOrder() - 1);
                formRepository.save(form);
            }
        }
    }
}
