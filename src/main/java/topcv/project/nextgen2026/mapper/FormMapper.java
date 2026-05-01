package topcv.project.nextgen2026.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import topcv.project.nextgen2026.dto.field.FieldResponse;
import topcv.project.nextgen2026.dto.form.DetailedFormResponse;
import topcv.project.nextgen2026.dto.form.FormResponse;
import topcv.project.nextgen2026.entity.Form;

@Component
@RequiredArgsConstructor
public class FormMapper {

    private final FieldMapper fieldMapper;

    public FormResponse toFormResponse(Form form) {
        return FormResponse.builder()
                .id(form.getId())
                .title(form.getTitle())
                .build();
    }

    public DetailedFormResponse toDetailedFormResponse(Form form) {
        List<FieldResponse> fieldResponses = form.getFields().stream()
                .sorted((f1, f2) -> f1.getDisplayOrder().compareTo(f2.getDisplayOrder()))
                .map(fieldMapper::toFieldResponse)
                .collect(Collectors.toList());

        return DetailedFormResponse.builder()
                .id(form.getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .status(form.getStatus())
                .fieldResponseList(fieldResponses)
                .build();
    }
}
