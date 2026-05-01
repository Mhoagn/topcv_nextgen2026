package topcv.project.nextgen2026.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import topcv.project.nextgen2026.entity.Submission;
import topcv.project.nextgen2026.entity.User;


public interface SubmissionRepository extends JpaRepository<Submission, String> {
    
    Page<Submission> findBySubmittedByOrderBySubmittedAtDesc(User user, Pageable pageable);
}
