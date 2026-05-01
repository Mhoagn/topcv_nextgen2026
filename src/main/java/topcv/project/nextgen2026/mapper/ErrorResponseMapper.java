package topcv.project.nextgen2026.mapper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import topcv.project.nextgen2026.dto.ErrorResponse;

import java.time.LocalDateTime;

@Component
public class ErrorResponseMapper {

    public ErrorResponse toErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {
        
        return ErrorResponse.builder()
                .statusCode(status.value())
                .message(message)
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .build();
    }
}
