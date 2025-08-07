package com.coursemanagement.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ResourceAlreadyExistsException Tests")
class ResourceAlreadyExistsExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message constructor")
        void shouldCreateExceptionWithMessageConstructor() {
            // Given
            String message = "Resource already exists";

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(message);

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
            String fieldName = "email";
            String fieldValue = "john.doe@example.com";

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(String.format("%s already exists with %s: %s", resourceName, fieldName, fieldValue));
            assertThat(exception.getCause()).isNull();
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should create exception with message and cause constructor")
        void shouldCreateExceptionWithMessageAndCauseConstructor() {
            // Given
            String message = "Duplicate key violation";
            Exception cause = new RuntimeException("Unique constraint failed");

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(message, cause);

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
            String fieldValue = "jane.doe@example.com";

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Instructor already exists with email: jane.doe@example.com");
        }

        @Test
        @DisplayName("Should format message with UUID field value")
        void shouldFormatMessageWithUUIDFieldValue() {
            // Given
            String resourceName = "Course";
            String fieldName = "id";
            UUID fieldValue = UUID.randomUUID();

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo(String.format("Course already exists with id: %s", fieldValue));
        }

        @Test
        @DisplayName("Should format message with integer field value")
        void shouldFormatMessageWithIntegerFieldValue() {
            // Given
            String resourceName = "Review";
            String fieldName = "rating";
            Integer fieldValue = 5;

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Review already exists with rating: 5");
        }

        @Test
        @DisplayName("Should format message with null field value")
        void shouldFormatMessageWithNullFieldValue() {
            // Given
            String resourceName = "Student";
            String fieldName = "id";
            Object fieldValue = null;

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Student already exists with id: null");
        }

        @Test
        @DisplayName("Should format message with complex object field value")
        void shouldFormatMessageWithComplexObjectFieldValue() {
            // Given
            String resourceName = "InstructorDetails";
            String fieldName = "profile";
            Object fieldValue = new Object() {
                @Override
                public String toString() {
                    return "Profile{youtube='@channel', hoppy='coding'}";
                }
            };

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("InstructorDetails already exists with profile: Profile{youtube='@channel', hoppy='coding'}");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null message")
        void shouldHandleNullMessage() {
            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException((String) null);

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
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(message);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle null resource name")
        void shouldHandleNullResourceName() {
            // Given
            String resourceName = null;
            String fieldName = "email";
            String fieldValue = "test@example.com";

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("null already exists with email: test@example.com");
        }

        @Test
        @DisplayName("Should handle null field name")
        void shouldHandleNullFieldName() {
            // Given
            String resourceName = "Course";
            String fieldName = null;
            String fieldValue = "Java 101";

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Course already exists with null: Java 101");
        }

        @Test
        @DisplayName("Should handle null cause")
        void shouldHandleNullCause() {
            // Given
            String message = "Conflict occurred";

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(message, null);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should handle special characters in message")
        void shouldHandleSpecialCharactersInMessage() {
            // Given
            String message = "Resource exists: 特殊文字 & symbols @#$%^&*()";

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(message);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should handle very long message")
        void shouldHandleVeryLongMessage() {
            // Given
            String longMessage = "B".repeat(1000);

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(longMessage);

            // Then
            assertThat(exception.getMessage()).isEqualTo(longMessage);
            assertThat(exception.getMessage()).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle special characters in field values")
        void shouldHandleSpecialCharactersInFieldValues() {
            // Given
            String resourceName = "User";
            String fieldName = "username";
            String fieldValue = "user@domain.com & special chars 特殊文字";

            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(exception.getMessage()).isEqualTo("User already exists with username: user@domain.com & special chars 特殊文字");
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException("Test message");

            // Then
            assertThat(exception).isInstanceOf(RuntimeException.class);
            assertThat(exception).isInstanceOf(Exception.class);
            assertThat(exception).isInstanceOf(Throwable.class);
        }

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            // Given
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException("Duplicate resource");

            // When & Then
            assertThatThrownBy(() -> {
                throw exception;
            }).isInstanceOf(ResourceAlreadyExistsException.class)
              .hasMessage("Duplicate resource");
        }

        @Test
        @DisplayName("Should maintain stack trace")
        void shouldMaintainStackTrace() {
            // When
            ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException("Test message");

            // Then
            assertThat(exception.getStackTrace()).isNotEmpty();
            assertThat(exception.getStackTrace()[0].getClassName()).isEqualTo(this.getClass().getName());
            assertThat(exception.getStackTrace()[0].getMethodName()).isEqualTo("shouldMaintainStackTrace");
        }

        @Test
        @DisplayName("Should work with try-catch blocks")
        void shouldWorkWithTryCatchBlocks() {
            // Given
            boolean exceptionCaught = false;
            String expectedMessage = "Resource conflict";

            // When
            try {
                throw new ResourceAlreadyExistsException(expectedMessage);
            } catch (ResourceAlreadyExistsException e) {
                exceptionCaught = true;
                assertThat(e.getMessage()).isEqualTo(expectedMessage);
            }

            // Then
            assertThat(exceptionCaught).isTrue();
        }
    }

    @Nested
    @DisplayName("Comparison Tests")
    class ComparisonTests {

        @Test
        @DisplayName("Should differentiate from ResourceNotFoundException")
        void shouldDifferentiateFromResourceNotFoundException() {
            // Given
            ResourceAlreadyExistsException existsException = new ResourceAlreadyExistsException("Already exists");
            ResourceNotFoundException notFoundException = new ResourceNotFoundException("Not found");

            // Then
            assertThat(existsException).isNotInstanceOf(ResourceNotFoundException.class);
            assertThat(notFoundException).isNotInstanceOf(ResourceAlreadyExistsException.class);
            assertThat(existsException.getClass()).isNotEqualTo(notFoundException.getClass());
        }

        @Test
        @DisplayName("Should have different message format than ResourceNotFoundException")
        void shouldHaveDifferentMessageFormatThanResourceNotFoundException() {
            // Given
            String resourceName = "Student";
            String fieldName = "email";
            String fieldValue = "test@example.com";

            ResourceAlreadyExistsException existsException = new ResourceAlreadyExistsException(resourceName, fieldName, fieldValue);
            ResourceNotFoundException notFoundException = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

            // Then
            assertThat(existsException.getMessage()).contains("already exists");
            assertThat(notFoundException.getMessage()).contains("not found");
            assertThat(existsException.getMessage()).isNotEqualTo(notFoundException.getMessage());
        }
    }
}