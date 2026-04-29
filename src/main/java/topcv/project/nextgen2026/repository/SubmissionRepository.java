package topcv.project.nextgen2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topcv.project.nextgen2026.entity.Submission;

import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
}
