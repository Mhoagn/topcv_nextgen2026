package topcv.project.nextgen2026.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import topcv.project.nextgen2026.entity.Field;


public interface FieldRepository extends JpaRepository<Field, String> {
    
    List<Field> findByFormIdAndDisplayOrderGreaterThanEqual(String formId, Integer displayOrder);
    
    List<Field> findByFormIdAndDisplayOrderBetween(String formId, Integer startOrder, Integer endOrder);
    
    Optional<Field> findByIdAndFormId(String fieldId, String formId);
}
