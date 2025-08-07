package com.coursemanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Simple Tests")
class GlobalExceptionHandlerSimpleTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException validationException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/api/v1/test");
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException correctly")
    void shouldHandleResourceNotFoundException() {
        // Given
        String errorMessage = "Student not found with id: 123";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(404);
        assertThat(errorResponse.getError()).isEqualTo("NOT_FOUND");
        assertThat(errorResponse.getMessage()).isEqualTo(errorMessage);
        assertThat(errorResponse.getPath()).isEqualTo("/api/v1/test");
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle ResourceAlreadyExistsException correctly")
    void shouldHandleResourceAlreadyExistsException() {
        // Given
        String errorMessage = "Student already exists with email: john@example.com";
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceAlreadyExistsException(exception, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(409);
        assertThat(errorResponse.getError()).isEqualTo("CONFLICT");
        assertThat(errorResponse.getMessage()).isEqualTo(errorMessage);
        assertThat(errorResponse.getPath()).isEqualTo("/api/v1/test");
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException correctly")
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        FieldError fieldError = new FieldError("studentRequest", "firstName", "John", false, null, null, "First name is required");
        
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(validationException, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(400);
        assertThat(errorResponse.getError()).isEqualTo("VALIDATION_FAILED");
        assertThat(errorResponse.getMessage()).isEqualTo("Validation failed for one or more fields");
        assertThat(errorResponse.getPath()).isEqualTo("/api/v1/test");
        assertThat(errorResponse.getTimestamp()).isNotNull();
        
        List<ErrorResponse.ValidationError> validationErrors = errorResponse.getValidationErrors();
        assertThat(validationErrors).isNotNull();
        assertThat(validationErrors).hasSize(1);
        
        ErrorResponse.ValidationError error = validationErrors.get(0);
        assertThat(error.getField()).isEqualTo("firstName");
        assertThat(error.getRejectedValue()).isEqualTo("John");
        assertThat(error.getMessage()).isEqualTo("First name is required");
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with email constraint")
    void shouldHandleDataIntegrityViolationExceptionWithEmailConstraint() {
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Duplicate entry 'test@example.com' for key 'email'");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolationException(exception, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(409);
        assertThat(errorResponse.getError()).isEqualTo("DATA_INTEGRITY_VIOLATION");
        assertThat(errorResponse.getMessage()).isEqualTo("Email address already exists");
        assertThat(errorResponse.getPath()).isEqualTo("/api/v1/test");
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with unique constraint")
    void shouldHandleDataIntegrityViolationExceptionWithUniqueConstraint() {
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Duplicate entry for unique constraint");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolationException(exception, request);

        // Then
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getMessage()).isEqualTo("Unique constraint violation");
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with generic message")
    void shouldHandleDataIntegrityViolationExceptionWithGenericMessage() {
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Foreign key constraint failed");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolationException(exception, request);

        // Then
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getMessage()).isEqualTo("Data integrity violation occurred");
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException correctly")
    void shouldHandleIllegalArgumentException() {
        // Given
        String errorMessage = "Invalid argument provided";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(exception, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(400);
        assertThat(errorResponse.getError()).isEqualTo("BAD_REQUEST");
        assertThat(errorResponse.getMessage()).isEqualTo(errorMessage);
        assertThat(errorResponse.getPath()).isEqualTo("/api/v1/test");
    }

    @Test
    @DisplayName("Should handle generic Exception correctly")
    void shouldHandleGenericException() {
        // Given
        Exception exception = new Exception("Unexpected error occurred");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(500);
        assertThat(errorResponse.getError()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(errorResponse.getMessage()).isEqualTo("An unexpected error occurred");
        assertThat(errorResponse.getPath()).isEqualTo("/api/v1/test");
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle RuntimeException as generic exception")
    void shouldHandleRuntimeExceptionAsGenericException() {
        // Given
        RuntimeException exception = new RuntimeException("Runtime error");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getStatus()).isEqualTo(500);
        assertThat(errorResponse.getError()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(errorResponse.getMessage()).isEqualTo("An unexpected error occurred");
    }

    @Test
    @DisplayName("Should handle different request URIs correctly")
    void shouldHandleDifferentRequestURIs() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/v1/students/123");
        ResourceNotFoundException exception = new ResourceNotFoundException("Student not found");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, request);

        // Then
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getPath()).isEqualTo("/api/v1/students/123");
    }

    @Test
    @DisplayName("Should handle null messages gracefully")
    void shouldHandleNullMessagesGracefully() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException((String) null);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getMessage()).isNull();
    }
}