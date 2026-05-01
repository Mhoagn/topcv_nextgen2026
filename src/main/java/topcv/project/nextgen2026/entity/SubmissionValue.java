package topcv.project.nextgen2026.entity;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
