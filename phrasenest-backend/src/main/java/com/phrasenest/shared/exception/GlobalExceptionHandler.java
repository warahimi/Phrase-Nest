package com.phrasenest.shared.exception;

import com.phrasenest.shared.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converts Java exceptions into predictable HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles @Valid request failures.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>>
    handleValidation(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(
                        "Request validation failed.",
                        errors
                ));
    }

    /**
     * Returns HTTP 404 when a record does not exist.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleNotFound(ResourceNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(
                        exception.getMessage(),
                        null
                ));
    }

    /**
     * Returns HTTP 409 for an application-detected duplicate.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleDuplicate(DuplicateResourceException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure(
                        exception.getMessage(),
                        null
                ));
    }

    /**
     * Also catches uniqueness or constraint errors raised by PostgreSQL.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleDataIntegrity(DataIntegrityViolationException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure(
                        "The operation violates a database constraint.",
                        null
                ));
    }

    /**
//     * Final fallback for unexpected errors.
//     *
//     * We do not expose exception.getMessage() because it might contain
//     * database, server, or security details.
//     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>>
//    handleUnexpected(Exception exception) {
//
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiResponse.failure(
//                        "An unexpected server error occurred.",
//                        null
//                ));
//    }

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Final fallback for unexpected errors.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(
            Exception exception) {

        log.error(
                "Unexpected exception. Type: {}, Message: {}",
                exception.getClass().getName(),
                exception.getMessage(),
                exception
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(
                        "An unexpected server error occurred.",
                        null
                ));
    }
    /**
     * Handles validation errors on request parameters and path variables.
     *
     * Example:
     * GET /resolve?query=
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>>
    handleConstraintViolation(ConstraintViolationException exception)
    {
        Map<String, String> errors = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation :
                exception.getConstraintViolations()) {

            errors.put(
                    violation.getPropertyPath().toString(),
                    violation.getMessage()
            );
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(
                        "Request validation failed.",
                        errors
                ));
    }
    /**
     * Handles invalid business input.
     *
     * Examples:
     * - A category cannot be its own parent.
     * - A parent assignment would create a circular hierarchy.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleIllegalArgument(
            IllegalArgumentException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(
                        exception.getMessage(),
                        null
                ));
    }

    /**
     * Handles operations that are valid in general but not allowed in the
     * resource's current state.
     *
     * Examples:
     * - Deleting a category that still has child categories.
     * - Deleting a category assigned to expressions.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleIllegalState(
            IllegalStateException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure(
                        exception.getMessage(),
                        null
                ));
    }
}