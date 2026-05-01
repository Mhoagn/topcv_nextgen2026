package topcv.project.nextgen2026.mapper;

import org.springframework.stereotype.Component;
import topcv.project.nextgen2026.dto.field.FieldResponse;
import topcv.project.nextgen2026.entity.Field;

@Component
public class FieldMapper {

    public FieldResponse toFieldResponse(Field field) {
        return FieldResponse.builder()
                .id(field.getId())
                .label(field.getLabel())
                .type(field.getType())
                .displayOrder(field.getDisplayOrder())
                .required(field.getRequired())
                .options(field.getOptions())
                .build();
    }
}
