package topcv.project.nextgen2026.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import topcv.project.nextgen2026.entity.Form;
import topcv.project.nextgen2026.enums.FormStatus;

import java.util.List;

public interface FormRepository extends JpaRepository<Form, String> {
    Page<Form> findAll(Pageable pageable);

    Page<Form> findByStatusOrderByDisplayOrderAsc(FormStatus status, Pageable pageable);

    @Query("SELECT f FROM Form f WHERE f.displayOrder >= :startOrder AND f.displayOrder <= :endOrder ORDER BY f.displayOrder ASC")
    List<Form> findByDisplayOrderBetween(Integer startOrder, Integer endOrder);

    @Query("SELECT f FROM Form f WHERE f.displayOrder >= :displayOrder ORDER BY f.displayOrder ASC")
    List<Form> findByDisplayOrderGreaterThanEqual(Integer displayOrder);
}
