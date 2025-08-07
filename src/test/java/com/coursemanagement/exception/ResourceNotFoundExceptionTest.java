package com.coursemanagement.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ResourceNotFoundException Tests")
class ResourceNotFoundExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message constructor")
        void shouldCreateExceptionWithMessageConstructor() {
            // Given
            String message = "Resource not found";

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(message);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isNull();
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should create exception with formatted message constructor")
        void shouldCreateExceptionWithFormattedMessageConstructor() {
            // Given
            String resourceName = "Student";
            String fieldName = "id";
            UUID fieldValue = UUID.randomUUID();

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
            assertThat(exception.getCause()).isNull();
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should create exception with message and cause constructor")
        void shouldCreateExceptionWithMessageAndCauseConstructor() {
            // Given
            String message = "Database connection failed";
            Exception cause = new RuntimeException("Connection timeout");

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Message Formatting Tests")
    class MessageFormattingTests {

        @Test
        @DisplayName("Should format message with string field value")
        void shouldFormatMessageWithStringFieldValue() {
            // Given
            String resourceName = "Instructor";
            String fieldName = "email";
            String fieldValue = "john.doe@example.com";

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Instructor not found with email: john.doe@example.com");
        }

        @Test
        @DisplayName("Should format message with integer field value")
        void shouldFormatMessageWithIntegerFieldValue() {
            // Given
            String resourceName = "Course";
            String fieldName = "id";
            Integer fieldValue = 123;

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Course not found with id: 123");
        }

        @Test
        @DisplayName("Should format message with null field value")
        void shouldFormatMessageWithNullFieldValue() {
            // Given
            String resourceName = "Review";
            String fieldName = "id";
            Object fieldValue = null;

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Review not found with id: null");
        }

        @Test
        @DisplayName("Should format message with complex object field value")
        void shouldFormatMessageWithComplexObjectFieldValue() {
            // Given
            String resourceName = "Student";
            String fieldName = "details";
            Object fieldValue = new Object() {
                @Override
                public String toString() {
                    return "StudentDetails{name='John', age=25}";
                }
            };

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Student not found with details: StudentDetails{name='John', age=25}");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null message")
        void shouldHandleNullMessage() {
            // When
            ResourceNotFoundException exception = new ResourceNotFoundException((String) null);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isNull();
        }

        @Test
        @DisplayName("Should handle empty message")
        void shouldHandleEmptyMessage() {
            // Given
            String message = "";

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(message);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle null resource name")
        void shouldHandleNullResourceName() {
            // Given
            String resourceName = null;
            String fieldName = "id";
            String fieldValue = "123";

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("null not found with id: 123");
        }

        @Test
        @DisplayName("Should handle null field name")
        void shouldHandleNullFieldName() {
            // Given
            String resourceName = "Student";
            String fieldName = null;
            String fieldValue = "123";

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Student not found with null: 123");
        }

        @Test
        @DisplayName("Should handle null cause")
        void shouldHandleNullCause() {
            // Given
            String message = "Error occurred";

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(message, null);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should handle special characters in message")
        void shouldHandleSpecialCharactersInMessage() {
            // Given
            String message = "Resource not found: 特殊文字 & symbols @#$%^&*()";

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(message);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should handle very long message")
        void shouldHandleVeryLongMessage() {
            // Given
            String longMessage = "A".repeat(1000);

            // When
            ResourceNotFoundException exception = new ResourceNotFoundException(longMessage);

            // Then
            assertThat(exception.getMessage()).isEqualTo(longMessage);
            assertThat(exception.getMessage()).hasSize(1000);
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // When
            ResourceNotFoundException exception = new ResourceNotFoundException("Test message");

            // Then
            assertThat(exception).isInstanceOf(RuntimeException.class);
            assertThat(exception).isInstanceOf(Exception.class);
            assertThat(exception).isInstanceOf(Throwable.class);
        }

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            // Given
            ResourceNotFoundException exception = new ResourceNotFoundException("Test error");

            // When & Then
            assertThatThrownBy(() -> {
                throw exception;
            }).isInstanceOf(ResourceNotFoundException.class)
              .hasMessage("Test error");
        }

        @Test
        @DisplayName("Should maintain stack trace")
        void shouldMaintainStackTrace() {
            // When
            ResourceNotFoundException exception = new ResourceNotFoundException("Test message");

            // Then
            assertThat(exception.getStackTrace()).isNotEmpty();
            assertThat(exception.getStackTrace()[0].getClassName()).isEqualTo(this.getClass().getName());
            assertThat(exception.getStackTrace()[0].getMethodName()).isEqualTo("shouldMaintainStackTrace");
        }
    }
}