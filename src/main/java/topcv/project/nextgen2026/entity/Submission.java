package topcv.project.nextgen2026.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import topcv.project.nextgen2026.enums.SubmissionStatus;

@Entity
@Table(name = "submissions", indexes = {
    @Index(name = "idx_submissions_form", columnList = "form_id, submitted_at"),
    @Index(name = "idx_submissions_user", columnList = "submitted_by, submitted_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false, foreignKey = @ForeignKey(name = "fk_submissions_form"))
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by", nullable = false, foreignKey = @ForeignKey(name = "fk_submissions_user"))
    private User submittedBy;

    @Column(name = "status", nullable = false, columnDefinition = "ENUM('valid', 'invalid')")
    private SubmissionStatus status = SubmissionStatus.VALID;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubmissionValue> submissionValues = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }


}
