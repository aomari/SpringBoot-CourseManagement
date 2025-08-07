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

@DisplayName("Student Entity Tests")
class StudentEntityTest {

    private Student student;
    private Course course1;
    private Course course2;
    private Instructor instructor;

    @BeforeEach
    void setUp() {
        instructor = new Instructor("John", "Doe", "john.doe@example.com");
        instructor.setId(UUID.randomUUID());
        
        course1 = new Course("Java Basics", instructor);
        course1.setId(UUID.randomUUID());
        
        course2 = new Course("Advanced Java", instructor);
        course2.setId(UUID.randomUUID());
        
        student = new Student("Jane", "Smith", "jane.smith@example.com");
        student.setId(UUID.randomUUID());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create student with default constructor")
        void shouldCreateStudentWithDefaultConstructor() {
            // When
            Student newStudent = new Student();

            // Then
            assertThat(newStudent).isNotNull();
            assertThat(newStudent.getId()).isNull();
            assertThat(newStudent.getFirstName()).isNull();
            assertThat(newStudent.getLastName()).isNull();
            assertThat(newStudent.getEmail()).isNull();
            assertThat(newStudent.getCourses()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should create student with parameterized constructor")
        void shouldCreateStudentWithParameterizedConstructor() {
            // When
            Student newStudent = new Student("John", "Doe", "john.doe@example.com");

            // Then
            assertThat(newStudent).isNotNull();
            assertThat(newStudent.getFirstName()).isEqualTo("John");
            assertThat(newStudent.getLastName()).isEqualTo("Doe");
            assertThat(newStudent.getEmail()).isEqualTo("john.doe@example.com");
            assertThat(newStudent.getCourses()).isNotNull().isEmpty();
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
            student.setId(id);

            // Then
            assertThat(student.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should set and get first name")
        void shouldSetAndGetFirstName() {
            // When
            student.setFirstName("John");

            // Then
            assertThat(student.getFirstName()).isEqualTo("John");
        }

        @Test
        @DisplayName("Should set and get last name")
        void shouldSetAndGetLastName() {
            // When
            student.setLastName("Doe");

            // Then
            assertThat(student.getLastName()).isEqualTo("Doe");
        }

        @Test
        @DisplayName("Should set and get email")
        void shouldSetAndGetEmail() {
            // When
            student.setEmail("new.email@example.com");

            // Then
            assertThat(student.getEmail()).isEqualTo("new.email@example.com");
        }

        @Test
        @DisplayName("Should set and get created at")
        void shouldSetAndGetCreatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            student.setCreatedAt(now);

            // Then
            assertThat(student.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get updated at")
        void shouldSetAndGetUpdatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            student.setUpdatedAt(now);

            // Then
            assertThat(student.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get courses")
        void shouldSetAndGetCourses() {
            // Given
            List<Course> courses = new ArrayList<>();
            courses.add(course1);
            courses.add(course2);

            // When
            student.setCourses(courses);

            // Then
            assertThat(student.getCourses()).isEqualTo(courses);
            assertThat(student.getCourses()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should get full name correctly")
        void shouldGetFullNameCorrectly() {
            // When
            String fullName = student.getFullName();

            // Then
            assertThat(fullName).isEqualTo("Jane Smith");
        }

        @Test
        @DisplayName("Should enroll in course successfully")
        void shouldEnrollInCourseSuccessfully() {
            // When
            student.enrollInCourse(course1);

            // Then
            assertThat(student.getCourses()).contains(course1);
            assertThat(course1.getStudents()).contains(student);
            assertThat(student.isEnrolledInCourse(course1)).isTrue();
        }

        @Test
        @DisplayName("Should not enroll in same course twice")
        void shouldNotEnrollInSameCourseTwice() {
            // Given
            student.enrollInCourse(course1);
            int initialCourseCount = student.getCourses().size();
            int initialStudentCount = course1.getStudents().size();

            // When
            student.enrollInCourse(course1);

            // Then
            assertThat(student.getCourses()).hasSize(initialCourseCount);
            assertThat(course1.getStudents()).hasSize(initialStudentCount);
        }

        @Test
        @DisplayName("Should unenroll from course successfully")
        void shouldUnenrollFromCourseSuccessfully() {
            // Given
            student.enrollInCourse(course1);
            assertThat(student.isEnrolledInCourse(course1)).isTrue();

            // When
            student.unenrollFromCourse(course1);

            // Then
            assertThat(student.getCourses()).doesNotContain(course1);
            assertThat(course1.getStudents()).doesNotContain(student);
            assertThat(student.isEnrolledInCourse(course1)).isFalse();
        }

        @Test
        @DisplayName("Should handle unenrolling from non-enrolled course")
        void shouldHandleUnenrollingFromNonEnrolledCourse() {
            // Given
            assertThat(student.isEnrolledInCourse(course1)).isFalse();
            int initialCourseCount = student.getCourses().size();

            // When
            student.unenrollFromCourse(course1);

            // Then
            assertThat(student.getCourses()).hasSize(initialCourseCount);
        }

        @Test
        @DisplayName("Should check enrollment status correctly")
        void shouldCheckEnrollmentStatusCorrectly() {
            // Given
            assertThat(student.isEnrolledInCourse(course1)).isFalse();

            // When
            student.enrollInCourse(course1);

            // Then
            assertThat(student.isEnrolledInCourse(course1)).isTrue();
            assertThat(student.isEnrolledInCourse(course2)).isFalse();
        }

        @Test
        @DisplayName("Should handle multiple course enrollments")
        void shouldHandleMultipleCourseEnrollments() {
            // When
            student.enrollInCourse(course1);
            student.enrollInCourse(course2);

            // Then
            assertThat(student.getCourses()).hasSize(2);
            assertThat(student.getCourses()).contains(course1, course2);
            assertThat(student.isEnrolledInCourse(course1)).isTrue();
            assertThat(student.isEnrolledInCourse(course2)).isTrue();
        }
    }

    @Nested
    @DisplayName("Object Methods Tests")
    class ObjectMethodsTests {

        @Test
        @DisplayName("Should generate toString correctly")
        void shouldGenerateToStringCorrectly() {
            // Given
            student.setCreatedAt(LocalDateTime.of(2023, 1, 1, 10, 0));
            student.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 10, 0));

            // When
            String toString = student.toString();

            // Then
            assertThat(toString).contains("Student(");
            assertThat(toString).contains("firstName=Jane");
            assertThat(toString).contains("lastName=Smith");
            assertThat(toString).contains("email=jane.smith@example.com");
        }

        @Test
        @DisplayName("Should generate toString with courses count")
        void shouldGenerateToStringWithCoursesCount() {
            // Given
            student.enrollInCourse(course1);
            student.enrollInCourse(course2);

            // When
            String toString = student.toString();

            // Then
            assertThat(toString).contains("Student(");
        }

        @Test
        @DisplayName("Should handle equals correctly")
        void shouldHandleEqualsCorrectly() {
            // Given
            UUID id = UUID.randomUUID();
            student.setId(id);

            Student sameStudent = new Student();
            sameStudent.setId(id);

            Student differentStudent = new Student();
            differentStudent.setId(UUID.randomUUID());

            Student nullIdStudent = new Student();

            // Then
            assertThat(student.equals(student)).isTrue(); // Same reference
            assertThat(student.equals(sameStudent)).isTrue(); // Same ID
            assertThat(student.equals(differentStudent)).isFalse(); // Different ID
            assertThat(student.equals(nullIdStudent)).isFalse(); // Null ID
            assertThat(student.equals(null)).isFalse(); // Null object
            assertThat(student.equals("string")).isFalse(); // Different class
        }

        @Test
        @DisplayName("Should handle equals with null ID")
        void shouldHandleEqualsWithNullId() {
            // Given
            Student student1 = new Student();
            Student student2 = new Student();

            // Then - With Lombok's @EqualsAndHashCode(onlyExplicitlyIncluded = true), 
            // objects with null IDs are considered equal
            assertThat(student1.equals(student2)).isTrue();
        }

        @Test
        @DisplayName("Should generate consistent hashCode")
        void shouldGenerateConsistentHashCode() {
            // Given
            UUID id = UUID.randomUUID();
            student.setId(id);

            // When
            int hashCode1 = student.hashCode();
            int hashCode2 = student.hashCode();

            // Then
            assertThat(hashCode1).isEqualTo(hashCode2);
            // With Lombok's @EqualsAndHashCode(onlyExplicitlyIncluded = true), 
            // hashCode is based on the included fields (id in this case)
            assertThat(hashCode1).isNotEqualTo(Student.class.hashCode());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null course in enrollment")
        void shouldHandleNullCourseInEnrollment() {
            // When & Then
            assertThatThrownBy(() -> student.enrollInCourse(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null course in unenrollment")
        void shouldHandleNullCourseInUnenrollment() {
            // When
            boolean result = student.getCourses().contains(null);

            // Then - The method uses contains() which returns false for null
            assertThat(result).isFalse();
            
            // The unenrollFromCourse method will not throw exception, just do nothing
            student.unenrollFromCourse(null);
            // Should not throw exception
        }

        @Test
        @DisplayName("Should handle null course in enrollment check")
        void shouldHandleNullCourseInEnrollmentCheck() {
            // When
            boolean isEnrolled = student.isEnrolledInCourse(null);

            // Then - The method uses contains() which returns false for null
            assertThat(isEnrolled).isFalse();
        }

        @Test
        @DisplayName("Should handle null courses list in toString")
        void shouldHandleNullCoursesListInToString() {
            // Given
            student.setCourses(null);

            // When
            String toString = student.toString();

            // Then
            assertThat(toString).contains("Student(");
        }
    }
}