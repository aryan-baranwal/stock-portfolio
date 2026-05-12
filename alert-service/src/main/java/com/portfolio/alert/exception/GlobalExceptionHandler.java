package com.portfolio.alert.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Handles validation errors (e.g. @NotNull, @Positive failures)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage));

        return ResponseEntity.badRequest().body(
                ErrorResponseDto.builder()
                        .status(400)
                        .message("Validation failed")
                        .errors(errors)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // Handles alert not found errors
    @ExceptionHandler(AlertNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(
            AlertNotFoundException ex) {

        return ResponseEntity.status(404).body(
                ErrorResponseDto.builder()
                        .status(404)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // Handles any other unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
        log.error("Unhandled exception in alert-service", ex);

        return ResponseEntity.internalServerError().body(
                ErrorResponseDto.builder()
                        .status(500)
                        .message("Internal server error")
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}