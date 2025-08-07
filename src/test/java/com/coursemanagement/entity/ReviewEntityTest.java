package com.coursemanagement.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Review Entity Tests")
class ReviewEntityTest {

    private Review review;
    private Course course;
    private Student student;
    private Instructor instructor;

    @BeforeEach
    void setUp() {
        instructor = new Instructor("John", "Doe", "john.doe@example.com");
        instructor.setId(UUID.randomUUID());
        
        course = new Course("Java Basics", instructor);
        course.setId(UUID.randomUUID());
        
        student = new Student("Jane", "Smith", "jane.smith@example.com");
        student.setId(UUID.randomUUID());
        
        review = new Review("Great course!", course, student);
        review.setId(UUID.randomUUID());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create review with default constructor")
        void shouldCreateReviewWithDefaultConstructor() {
            // When
            Review newReview = new Review();

            // Then
            assertThat(newReview).isNotNull();
            assertThat(newReview.getId()).isNull();
            assertThat(newReview.getComment()).isNull();
            assertThat(newReview.getCourse()).isNull();
            assertThat(newReview.getStudent()).isNull();
            assertThat(newReview.getCreatedAt()).isNull();
            assertThat(newReview.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("Should create review with parameterized constructor")
        void shouldCreateReviewWithParameterizedConstructor() {
            // When
            Review newReview = new Review("Excellent course!", course, student);

            // Then
            assertThat(newReview).isNotNull();
            assertThat(newReview.getComment()).isEqualTo("Excellent course!");
            assertThat(newReview.getCourse()).isEqualTo(course);
            assertThat(newReview.getStudent()).isEqualTo(student);
        }

        @Test
        @DisplayName("Should create review with null values")
        void shouldCreateReviewWithNullValues() {
            // When
            Review newReview = new Review(null, null, null);

            // Then
            assertThat(newReview).isNotNull();
            assertThat(newReview.getComment()).isNull();
            assertThat(newReview.getCourse()).isNull();
            assertThat(newReview.getStudent()).isNull();
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get ID")
        void shouldSetAndGetId() {
            // Given
            UUID id = UUID.randomUUID();

            // When
            review.setId(id);

            // Then
            assertThat(review.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should set and get comment")
        void shouldSetAndGetComment() {
            // When
            review.setComment("Updated comment");

            // Then
            assertThat(review.getComment()).isEqualTo("Updated comment");
        }

        @Test
        @DisplayName("Should set and get comment with null")
        void shouldSetAndGetCommentWithNull() {
            // When
            review.setComment(null);

            // Then
            assertThat(review.getComment()).isNull();
        }

        @Test
        @DisplayName("Should set and get comment with empty string")
        void shouldSetAndGetCommentWithEmptyString() {
            // When
            review.setComment("");

            // Then
            assertThat(review.getComment()).isEqualTo("");
        }

        @Test
        @DisplayName("Should set and get created at")
        void shouldSetAndGetCreatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            review.setCreatedAt(now);

            // Then
            assertThat(review.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get updated at")
        void shouldSetAndGetUpdatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            review.setUpdatedAt(now);

            // Then
            assertThat(review.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get course")
        void shouldSetAndGetCourse() {
            // Given
            Course newCourse = new Course("Advanced Java", instructor);
            newCourse.setId(UUID.randomUUID());

            // When
            review.setCourse(newCourse);

            // Then
            assertThat(review.getCourse()).isEqualTo(newCourse);
        }

        @Test
        @DisplayName("Should set and get course with null")
        void shouldSetAndGetCourseWithNull() {
            // When
            review.setCourse(null);

            // Then
            assertThat(review.getCourse()).isNull();
        }

        @Test
        @DisplayName("Should set and get student")
        void shouldSetAndGetStudent() {
            // Given
            Student newStudent = new Student("Bob", "Johnson", "bob.johnson@example.com");
            newStudent.setId(UUID.randomUUID());

            // When
            review.setStudent(newStudent);

            // Then
            assertThat(review.getStudent()).isEqualTo(newStudent);
        }

        @Test
        @DisplayName("Should set and get student with null")
        void shouldSetAndGetStudentWithNull() {
            // When
            review.setStudent(null);

            // Then
            assertThat(review.getStudent()).isNull();
        }
    }

    @Nested
    @DisplayName("Object Methods Tests")
    class ObjectMethodsTests {

        @Test
        @DisplayName("Should generate toString correctly")
        void shouldGenerateToStringCorrectly() {
            // Given
            review.setCreatedAt(LocalDateTime.of(2023, 1, 1, 10, 0));
            review.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 10, 0));

            // When
            String toString = review.toString();

            // Then
            assertThat(toString).contains("Review{");
            assertThat(toString).contains("comment='Great course!'");
        }

        @Test
        @DisplayName("Should generate toString with null comment")
        void shouldGenerateToStringWithNullComment() {
            // Given
            review.setComment(null);

            // When
            String toString = review.toString();

            // Then
            assertThat(toString).contains("Review{");
            assertThat(toString).contains("comment='null'");
        }

        @Test
        @DisplayName("Should generate toString with empty comment")
        void shouldGenerateToStringWithEmptyComment() {
            // Given
            review.setComment("");

            // When
            String toString = review.toString();

            // Then
            assertThat(toString).contains("Review{");
            assertThat(toString).contains("comment=''");
        }
    }

    @Nested
    @DisplayName("Relationship Tests")
    class RelationshipTests {

        @Test
        @DisplayName("Should maintain relationship with course")
        void shouldMaintainRelationshipWithCourse() {
            // Given
            Course newCourse = new Course("Spring Boot", instructor);

            // When
            review.setCourse(newCourse);

            // Then
            assertThat(review.getCourse()).isEqualTo(newCourse);
        }

        @Test
        @DisplayName("Should maintain relationship with student")
        void shouldMaintainRelationshipWithStudent() {
            // Given
            Student newStudent = new Student("Alice", "Brown", "alice.brown@example.com");

            // When
            review.setStudent(newStudent);

            // Then
            assertThat(review.getStudent()).isEqualTo(newStudent);
        }

        @Test
        @DisplayName("Should handle changing course")
        void shouldHandleChangingCourse() {
            // Given
            Course originalCourse = review.getCourse();
            Course newCourse = new Course("Python Basics", instructor);

            // When
            review.setCourse(newCourse);

            // Then
            assertThat(review.getCourse()).isEqualTo(newCourse);
            assertThat(review.getCourse()).isNotEqualTo(originalCourse);
        }

        @Test
        @DisplayName("Should handle changing student")
        void shouldHandleChangingStudent() {
            // Given
            Student originalStudent = review.getStudent();
            Student newStudent = new Student("Charlie", "Davis", "charlie.davis@example.com");

            // When
            review.setStudent(newStudent);

            // Then
            assertThat(review.getStudent()).isEqualTo(newStudent);
            assertThat(review.getStudent()).isNotEqualTo(originalStudent);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long comment")
        void shouldHandleVeryLongComment() {
            // Given
            String longComment = "A".repeat(1000);

            // When
            review.setComment(longComment);

            // Then
            assertThat(review.getComment()).isEqualTo(longComment);
            assertThat(review.getComment()).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle comment with special characters")
        void shouldHandleCommentWithSpecialCharacters() {
            // Given
            String specialComment = "Great course! 🎉 It's amazing & very helpful. Rating: 5/5 ⭐⭐⭐⭐⭐";

            // When
            review.setComment(specialComment);

            // Then
            assertThat(review.getComment()).isEqualTo(specialComment);
        }

        @Test
        @DisplayName("Should handle comment with line breaks")
        void shouldHandleCommentWithLineBreaks() {
            // Given
            String multilineComment = "Great course!\nVery informative.\nHighly recommended.";

            // When
            review.setComment(multilineComment);

            // Then
            assertThat(review.getComment()).isEqualTo(multilineComment);
            assertThat(review.getComment()).contains("\n");
        }

        @Test
        @DisplayName("Should handle timestamps correctly")
        void shouldHandleTimestampsCorrectly() {
            // Given
            LocalDateTime createdTime = LocalDateTime.of(2023, 1, 1, 10, 0, 0);
            LocalDateTime updatedTime = LocalDateTime.of(2023, 1, 2, 15, 30, 45);

            // When
            review.setCreatedAt(createdTime);
            review.setUpdatedAt(updatedTime);

            // Then
            assertThat(review.getCreatedAt()).isEqualTo(createdTime);
            assertThat(review.getUpdatedAt()).isEqualTo(updatedTime);
            assertThat(review.getUpdatedAt()).isAfter(review.getCreatedAt());
        }
    }
}