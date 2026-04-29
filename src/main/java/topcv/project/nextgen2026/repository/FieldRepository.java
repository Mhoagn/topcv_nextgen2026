package topcv.project.nextgen2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topcv.project.nextgen2026.entity.Field;

import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {
}
