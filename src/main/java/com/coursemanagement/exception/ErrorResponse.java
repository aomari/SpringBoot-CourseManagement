package com.coursemanagement.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response format for API errors.
 */
@Schema(description = "Error response model")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error type", example = "NOT_FOUND")
    private String error;

    @Schema(description = "Error message", example = "Instructor not found with id: 123e4567-e89b-12d3-a456-426614174000")
    private String message;

    @Schema(description = "API path where error occurred", example = "/api/v1/instructors/123e4567-e89b-12d3-a456-426614174000")
    private String path;

    @Schema(description = "Timestamp when error occurred", example = "2023-12-01T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "List of validation errors (if applicable)")
    private List<ValidationError> validationErrors;

    // Default constructor
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor
    public ErrorResponse(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Constructor with validation errors
    public ErrorResponse(int status, String error, String message, String path, List<ValidationError> validationErrors) {
        this(status, error, message, path);
        this.validationErrors = validationErrors;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    /**
     * Nested class for validation error details.
     */
    @Schema(description = "Validation error details")
    public static class ValidationError {

        @Schema(description = "Field that failed validation", example = "email")
        private String field;

        @Schema(description = "Rejected value", example = "invalid-email")
        private Object rejectedValue;

        @Schema(description = "Validation error message", example = "Email should be valid")
        private String message;

        public ValidationError() {}

        public ValidationError(String field, Object rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        // Getters and Setters
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}