package topcv.project.nextgen2026.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "submission_values", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_submission_field", columnNames = {"submission_id", "field_id"})
    },
    indexes = {
        @Index(name = "idx_sv_field", columnList = "field_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionValue {

    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sv_submission"))
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sv_field"))
    private Field field;

    @Column(name = "value", columnDefinition = "TEXT")
    private String value;
}
