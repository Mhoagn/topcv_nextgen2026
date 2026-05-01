package topcv.project.nextgen2026.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import topcv.project.nextgen2026.dto.ApiResponse;
import topcv.project.nextgen2026.dto.PageResponse;

import java.util.List;

@Component
public class ResponseMapper {

    public <T> PageResponse<T> toPageResponse(Page<T> page) {
        return PageResponse.<T>builder()
                .data(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    public <T> PageResponse<T> toPageResponse(List<T> data, Page<?> page) {
        return PageResponse.<T>builder()
                .data(data)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    public <T> ApiResponse<T> toApiResponse(T data, String message, int statusCode) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public <T> ApiResponse<T> toApiResponse(T data, String message) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public <T> ApiResponse<T> toApiResponse(T data) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .success(true)
                .data(data)
                .build();
    }

    public ApiResponse<Void> toSuccessResponse(String message) {
        return ApiResponse.<Void>builder()
                .statusCode(200)
                .success(true)
                .message(message)
                .build();
    }
    
    public ApiResponse<Void> toSuccessResponse(String message, int statusCode) {
        return ApiResponse.<Void>builder()
                .statusCode(statusCode)
                .success(true)
                .message(message)
                .build();
    }
}
