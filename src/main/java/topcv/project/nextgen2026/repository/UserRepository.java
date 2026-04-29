package topcv.project.nextgen2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topcv.project.nextgen2026.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
