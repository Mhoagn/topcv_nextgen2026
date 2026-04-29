package topcv.project.nextgen2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topcv.project.nextgen2026.entity.Form;

import java.util.UUID;

public interface FormRepository extends JpaRepository<Form, UUID> {
}
