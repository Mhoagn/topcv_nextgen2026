package topcv.project.nextgen2026.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import topcv.project.nextgen2026.dto.ErrorResponse;
import topcv.project.nextgen2026.mapper.ErrorResponseMapper;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorResponseMapper errorResponseMapper;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            NotFoundException ex, 
            HttpServletRequest request) {
        
        log.error("NotFoundException: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), request);
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex, 
            HttpServletRequest request) {
        
        log.error("BadRequestException: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex, 
            HttpServletRequest request) {
        
        log.error("UnauthorizedException: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(
                HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, 
            HttpServletRequest request) {
        
        log.error("ValidationException: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(
                HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), request);
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {
        
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.error("MethodArgumentNotValidException: {} - Path: {}", errorMessage, request.getRequestURI());
        
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(
                HttpStatus.BAD_REQUEST, errorMessage, request);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, 
            HttpServletRequest request) {
        
        log.error("RuntimeException: {} - Path: {} - Cause: {}", 
                ex.getMessage(), request.getRequestURI(), ex.getCause(), ex);
        
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Đã xảy ra lỗi hệ thống: " + ex.getMessage(), 
                request);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex, 
            HttpServletRequest request) {
        
        log.error("Exception: {} - Path: {} - Cause: {}", 
                ex.getMessage(), request.getRequestURI(), ex.getCause(), ex);
        
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Lỗi không xác định: " + ex.getMessage(), 
                request);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
