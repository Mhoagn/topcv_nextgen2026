package topcv.project.nextgen2026.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import topcv.project.nextgen2026.enums.UserRole;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "role", nullable = false, columnDefinition = "ENUM('admin', 'staff')")
    private UserRole role = UserRole.STAFF;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Form> createdForms = new ArrayList<>();

    @OneToMany(mappedBy = "submittedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Submission> submissions = new ArrayList<>();


}
