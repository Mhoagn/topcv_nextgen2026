package topcv.project.nextgen2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topcv.project.nextgen2026.entity.SubmissionValue;

import java.util.UUID;

public interface SubmissionValueRepository extends JpaRepository<SubmissionValue, UUID> {
}
