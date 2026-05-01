package topcv.project.nextgen2026.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import topcv.project.nextgen2026.entity.Field;
import topcv.project.nextgen2026.repository.FieldRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class FieldDisplayOrderHelper {

    private final FieldRepository fieldRepository;

    /**
     * Tạo khoảng trống cho field mới trong form
     * @param formId ID của form chứa field
     * @param targetPosition Vị trí cần chèn field mới vào
     */
    public void makeRoomForNewField(String formId, Integer targetPosition) {
        List<Field> fieldsToShift = fieldRepository.findByFormIdAndDisplayOrderGreaterThanEqual(formId, targetPosition);
        if (!fieldsToShift.isEmpty()) {
            log.info("Shifting {} fields down to make room at position {} in form {}", 
                    fieldsToShift.size(), targetPosition, formId);
            for (Field field : fieldsToShift) {
                field.setDisplayOrder(field.getDisplayOrder() + 1);
                fieldRepository.save(field);
            }
        }
    }

    /**
     * Di chuyển field từ vị trí cũ sang vị trí mới, tự động shift các field khác
     * @param formId ID của form chứa field
     * @param fieldId ID của field cần di chuyển
     * @param oldPosition Vị trí cũ
     * @param newPosition Vị trí mới
     */
    public void moveField(String formId, String fieldId, Integer oldPosition, Integer newPosition) {
        if (oldPosition.equals(newPosition)) {
            return;
        }

        if (newPosition < oldPosition) {
            shiftFieldsDown(formId, fieldId, newPosition, oldPosition - 1);
        } else {
            shiftFieldsUp(formId, fieldId, oldPosition + 1, newPosition);
        }
    }

    /**
     * Shift các field xuống (tăng displayOrder) trong khoảng từ startPos đến endPos
     */
    private void shiftFieldsDown(String formId, String excludeFieldId, Integer startPos, Integer endPos) {
        List<Field> fieldsToShift = fieldRepository.findByFormIdAndDisplayOrderBetween(formId, startPos, endPos);
        for (Field field : fieldsToShift) {
            if (!field.getId().equals(excludeFieldId)) {
                field.setDisplayOrder(field.getDisplayOrder() + 1);
                fieldRepository.save(field);
            }
        }
        log.info("Shifted {} fields down (positions {}-{}) in form {}", 
                fieldsToShift.size(), startPos, endPos, formId);
    }

    /**
     * Shift các field lên (giảm displayOrder) trong khoảng từ startPos đến endPos
     */
    private void shiftFieldsUp(String formId, String excludeFieldId, Integer startPos, Integer endPos) {
        List<Field> fieldsToShift = fieldRepository.findByFormIdAndDisplayOrderBetween(formId, startPos, endPos);
        for (Field field : fieldsToShift) {
            if (!field.getId().equals(excludeFieldId)) {
                field.setDisplayOrder(field.getDisplayOrder() - 1);
                fieldRepository.save(field);
            }
        }
        log.info("Shifted {} fields up (positions {}-{}) in form {}", 
                fieldsToShift.size(), startPos, endPos, formId);
    }

    /**
     * Shift các field lên sau khi xóa 1 field
     * @param formId ID của form chứa field
     * @param deletedPosition Vị trí của field bị xóa
     */
    public void fillGapAfterFieldDelete(String formId, Integer deletedPosition) {
        List<Field> fieldsToShift = fieldRepository.findByFormIdAndDisplayOrderGreaterThanEqual(
                formId, deletedPosition + 1);
        if (!fieldsToShift.isEmpty()) {
            log.info("Shifting {} fields up to fill gap at position {} in form {}", 
                    fieldsToShift.size(), deletedPosition, formId);
            for (Field field : fieldsToShift) {
                field.setDisplayOrder(field.getDisplayOrder() - 1);
                fieldRepository.save(field);
            }
        }
    }
}
