package com.coursemanagement.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Course Entity Tests")
class CourseEntityTest {

    private Course course;
    private Instructor instructor;
    private Student student1;
    private Student student2;
    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        instructor = new Instructor("John", "Doe", "john.doe@example.com");
        instructor.setId(UUID.randomUUID());
        
        course = new Course("Java Basics", instructor);
        course.setId(UUID.randomUUID());
        
        student1 = new Student("Jane", "Smith", "jane.smith@example.com");
        student1.setId(UUID.randomUUID());
        
        student2 = new Student("Bob", "Johnson", "bob.johnson@example.com");
        student2.setId(UUID.randomUUID());
        
        review1 = new Review("Great course!", course, student1);
        review1.setId(UUID.randomUUID());
        
        review2 = new Review("Very informative!", course, student2);
        review2.setId(UUID.randomUUID());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create course with default constructor")
        void shouldCreateCourseWithDefaultConstructor() {
            // When
            Course newCourse = new Course();

            // Then
            assertThat(newCourse).isNotNull();
            assertThat(newCourse.getId()).isNull();
            assertThat(newCourse.getTitle()).isNull();
            assertThat(newCourse.getInstructor()).isNull();
            assertThat(newCourse.getReviews()).isNotNull().isEmpty();
            assertThat(newCourse.getStudents()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should create course with parameterized constructor")
        void shouldCreateCourseWithParameterizedConstructor() {
            // When
            Course newCourse = new Course("Advanced Java", instructor);

            // Then
            assertThat(newCourse).isNotNull();
            assertThat(newCourse.getTitle()).isEqualTo("Advanced Java");
            assertThat(newCourse.getInstructor()).isEqualTo(instructor);
            assertThat(newCourse.getReviews()).isNotNull().isEmpty();
            assertThat(newCourse.getStudents()).isNotNull().isEmpty();
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
            course.setId(id);

            // Then
            assertThat(course.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should set and get title")
        void shouldSetAndGetTitle() {
            // When
            course.setTitle("Advanced Java");

            // Then
            assertThat(course.getTitle()).isEqualTo("Advanced Java");
        }

        @Test
        @DisplayName("Should set and get created at")
        void shouldSetAndGetCreatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            course.setCreatedAt(now);

            // Then
            assertThat(course.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get updated at")
        void shouldSetAndGetUpdatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            course.setUpdatedAt(now);

            // Then
            assertThat(course.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get instructor")
        void shouldSetAndGetInstructor() {
            // Given
            Instructor newInstructor = new Instructor("Jane", "Doe", "jane.doe@example.com");

            // When
            course.setInstructor(newInstructor);

            // Then
            assertThat(course.getInstructor()).isEqualTo(newInstructor);
        }

        @Test
        @DisplayName("Should set and get reviews")
        void shouldSetAndGetReviews() {
            // Given
            List<Review> reviews = new ArrayList<>();
            reviews.add(review1);
            reviews.add(review2);

            // When
            course.setReviews(reviews);

            // Then
            assertThat(course.getReviews()).isEqualTo(reviews);
            assertThat(course.getReviews()).hasSize(2);
        }

        @Test
        @DisplayName("Should set and get students")
        void shouldSetAndGetStudents() {
            // Given
            List<Student> students = new ArrayList<>();
            students.add(student1);
            students.add(student2);

            // When
            course.setStudents(students);

            // Then
            assertThat(course.getStudents()).isEqualTo(students);
            assertThat(course.getStudents()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Review Management Tests")
    class ReviewManagementTests {

        @Test
        @DisplayName("Should add review successfully")
        void shouldAddReviewSuccessfully() {
            // When
            course.addReview(review1);

            // Then
            assertThat(course.getReviews()).contains(review1);
            assertThat(review1.getCourse()).isEqualTo(course);
        }

        @Test
        @DisplayName("Should add multiple reviews")
        void shouldAddMultipleReviews() {
            // When
            course.addReview(review1);
            course.addReview(review2);

            // Then
            assertThat(course.getReviews()).hasSize(2);
            assertThat(course.getReviews()).contains(review1, review2);
        }

        @Test
        @DisplayName("Should remove review successfully")
        void shouldRemoveReviewSuccessfully() {
            // Given
            course.addReview(review1);
            assertThat(course.getReviews()).contains(review1);

            // When
            course.removeReview(review1);

            // Then
            assertThat(course.getReviews()).doesNotContain(review1);
            assertThat(review1.getCourse()).isNull();
        }

        @Test
        @DisplayName("Should handle removing non-existing review")
        void shouldHandleRemovingNonExistingReview() {
            // Given
            int initialSize = course.getReviews().size();

            // When
            course.removeReview(review1);

            // Then
            assertThat(course.getReviews()).hasSize(initialSize);
        }
    }

    @Nested
    @DisplayName("Student Enrollment Tests")
    class StudentEnrollmentTests {

        @Test
        @DisplayName("Should enroll student successfully")
        void shouldEnrollStudentSuccessfully() {
            // When
            course.enrollStudent(student1);

            // Then
            assertThat(course.getStudents()).contains(student1);
            assertThat(student1.getCourses()).contains(course);
            assertThat(course.hasStudent(student1)).isTrue();
        }

        @Test
        @DisplayName("Should not enroll same student twice")
        void shouldNotEnrollSameStudentTwice() {
            // Given
            course.enrollStudent(student1);
            int initialStudentCount = course.getStudents().size();
            int initialCourseCount = student1.getCourses().size();

            // When
            course.enrollStudent(student1);

            // Then
            assertThat(course.getStudents()).hasSize(initialStudentCount);
            assertThat(student1.getCourses()).hasSize(initialCourseCount);
        }

        @Test
        @DisplayName("Should unenroll student successfully")
        void shouldUnenrollStudentSuccessfully() {
            // Given
            course.enrollStudent(student1);
            assertThat(course.hasStudent(student1)).isTrue();

            // When
            course.unenrollStudent(student1);

            // Then
            assertThat(course.getStudents()).doesNotContain(student1);
            assertThat(student1.getCourses()).doesNotContain(course);
            assertThat(course.hasStudent(student1)).isFalse();
        }

        @Test
        @DisplayName("Should handle unenrolling non-enrolled student")
        void shouldHandleUnenrollingNonEnrolledStudent() {
            // Given
            assertThat(course.hasStudent(student1)).isFalse();
            int initialStudentCount = course.getStudents().size();

            // When
            course.unenrollStudent(student1);

            // Then
            assertThat(course.getStudents()).hasSize(initialStudentCount);
        }

        @Test
        @DisplayName("Should check student enrollment correctly")
        void shouldCheckStudentEnrollmentCorrectly() {
            // Given
            assertThat(course.hasStudent(student1)).isFalse();

            // When
            course.enrollStudent(student1);

            // Then
            assertThat(course.hasStudent(student1)).isTrue();
            assertThat(course.hasStudent(student2)).isFalse();
        }

        @Test
        @DisplayName("Should handle multiple student enrollments")
        void shouldHandleMultipleStudentEnrollments() {
            // When
            course.enrollStudent(student1);
            course.enrollStudent(student2);

            // Then
            assertThat(course.getStudents()).hasSize(2);
            assertThat(course.getStudents()).contains(student1, student2);
            assertThat(course.hasStudent(student1)).isTrue();
            assertThat(course.hasStudent(student2)).isTrue();
        }
    }

    @Nested
    @DisplayName("Object Methods Tests")
    class ObjectMethodsTests {

        @Test
        @DisplayName("Should generate toString correctly")
        void shouldGenerateToStringCorrectly() {
            // Given
            course.setCreatedAt(LocalDateTime.of(2023, 1, 1, 10, 0));
            course.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 10, 0));

            // When
            String toString = course.toString();

            // Then
            assertThat(toString).contains("Course{");
            assertThat(toString).contains("title='Java Basics'");
            assertThat(toString).contains("instructor=John Doe");
        }

        @Test
        @DisplayName("Should handle toString with null instructor")
        void shouldHandleToStringWithNullInstructor() {
            // Given
            course.setInstructor(null);

            // When
            String toString = course.toString();

            // Then
            assertThat(toString).contains("instructor=null");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null review in add review")
        void shouldHandleNullReviewInAddReview() {
            // When & Then
            assertThatThrownBy(() -> course.addReview(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null review in remove review")
        void shouldHandleNullReviewInRemoveReview() {
            // When & Then - The method will throw NullPointerException because it calls review.setCourse(null)
            assertThatThrownBy(() -> course.removeReview(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null student in enrollment")
        void shouldHandleNullStudentInEnrollment() {
            // When & Then
            assertThatThrownBy(() -> course.enrollStudent(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null student in unenrollment")
        void shouldHandleNullStudentInUnenrollment() {
            // When
            course.unenrollStudent(null);

            // Then - Should not throw exception, just handle gracefully
            assertThat(course.getStudents()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null student in hasStudent check")
        void shouldHandleNullStudentInHasStudentCheck() {
            // When
            boolean hasStudent = course.hasStudent(null);

            // Then
            assertThat(hasStudent).isFalse();
        }
    }
}