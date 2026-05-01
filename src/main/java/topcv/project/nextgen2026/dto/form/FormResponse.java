package topcv.project.nextgen2026.dto.form;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Data
public class FormResponse {
    private String id;
    private String title;
}
