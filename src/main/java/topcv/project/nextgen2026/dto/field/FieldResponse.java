package topcv.project.nextgen2026.dto.field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import topcv.project.nextgen2026.enums.FieldType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldResponse {
    private String id;
    private String label;
    private FieldType type;
    private Integer displayOrder;
    private Boolean required;
    private String options;
}
