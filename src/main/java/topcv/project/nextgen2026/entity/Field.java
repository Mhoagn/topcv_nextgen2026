package topcv.project.nextgen2026.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import topcv.project.nextgen2026.enums.FieldType;

@Entity
@Table(name = "fields", indexes = {
    @Index(name = "idx_fields_form_order", columnList = "form_id, display_order")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Field {

    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fields_form"))
    private Form form;

    @Column(name = "label", length = 255, nullable = false)
    private String label;

    @Column(name = "type", nullable = false, columnDefinition = "ENUM('text', 'number', 'date', 'color', 'select')")
    private FieldType type;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 1;

    @Column(name = "required", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean required = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "options", columnDefinition = "JSON")
    private String options;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubmissionValue> submissionValues = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
