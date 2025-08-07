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

@DisplayName("Instructor Entity Tests")
class InstructorEntityTest {

    private Instructor instructor;
    private InstructorDetails instructorDetails;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        instructorDetails = new InstructorDetails("https://youtube.com/@johndoe", "Playing guitar");
        instructorDetails.setId(UUID.randomUUID());
        
        instructor = new Instructor("John", "Doe", "john.doe@example.com");
        instructor.setId(UUID.randomUUID());
        
        course1 = new Course("Java Basics", instructor);
        course1.setId(UUID.randomUUID());
        
        course2 = new Course("Advanced Java", instructor);
        course2.setId(UUID.randomUUID());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instructor with default constructor")
        void shouldCreateInstructorWithDefaultConstructor() {
            // When
            Instructor newInstructor = new Instructor();

            // Then
            assertThat(newInstructor).isNotNull();
            assertThat(newInstructor.getId()).isNull();
            assertThat(newInstructor.getFirstName()).isNull();
            assertThat(newInstructor.getLastName()).isNull();
            assertThat(newInstructor.getEmail()).isNull();
            assertThat(newInstructor.getInstructorDetails()).isNull();
            assertThat(newInstructor.getCourses()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should create instructor with basic constructor")
        void shouldCreateInstructorWithBasicConstructor() {
            // When
            Instructor newInstructor = new Instructor("Jane", "Smith", "jane.smith@example.com");

            // Then
            assertThat(newInstructor).isNotNull();
            assertThat(newInstructor.getFirstName()).isEqualTo("Jane");
            assertThat(newInstructor.getLastName()).isEqualTo("Smith");
            assertThat(newInstructor.getEmail()).isEqualTo("jane.smith@example.com");
            assertThat(newInstructor.getInstructorDetails()).isNull();
            assertThat(newInstructor.getCourses()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should create instructor with all fields constructor")
        void shouldCreateInstructorWithAllFieldsConstructor() {
            // When
            Instructor newInstructor = new Instructor("Jane", "Smith", "jane.smith@example.com", instructorDetails);

            // Then
            assertThat(newInstructor).isNotNull();
            assertThat(newInstructor.getFirstName()).isEqualTo("Jane");
            assertThat(newInstructor.getLastName()).isEqualTo("Smith");
            assertThat(newInstructor.getEmail()).isEqualTo("jane.smith@example.com");
            assertThat(newInstructor.getInstructorDetails()).isEqualTo(instructorDetails);
            assertThat(newInstructor.getCourses()).isNotNull().isEmpty();
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
            instructor.setId(id);

            // Then
            assertThat(instructor.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should set and get first name")
        void shouldSetAndGetFirstName() {
            // When
            instructor.setFirstName("Jane");

            // Then
            assertThat(instructor.getFirstName()).isEqualTo("Jane");
        }

        @Test
        @DisplayName("Should set and get last name")
        void shouldSetAndGetLastName() {
            // When
            instructor.setLastName("Smith");

            // Then
            assertThat(instructor.getLastName()).isEqualTo("Smith");
        }

        @Test
        @DisplayName("Should set and get email")
        void shouldSetAndGetEmail() {
            // When
            instructor.setEmail("new.email@example.com");

            // Then
            assertThat(instructor.getEmail()).isEqualTo("new.email@example.com");
        }

        @Test
        @DisplayName("Should set and get created at")
        void shouldSetAndGetCreatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            instructor.setCreatedAt(now);

            // Then
            assertThat(instructor.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get updated at")
        void shouldSetAndGetUpdatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            instructor.setUpdatedAt(now);

            // Then
            assertThat(instructor.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get instructor details")
        void shouldSetAndGetInstructorDetails() {
            // When
            instructor.setInstructorDetails(instructorDetails);

            // Then
            assertThat(instructor.getInstructorDetails()).isEqualTo(instructorDetails);
            assertThat(instructorDetails.getInstructor()).isEqualTo(instructor);
        }

        @Test
        @DisplayName("Should set instructor details to null")
        void shouldSetInstructorDetailsToNull() {
            // Given
            instructor.setInstructorDetails(instructorDetails);
            assertThat(instructor.getInstructorDetails()).isNotNull();

            // When
            instructor.setInstructorDetails(null);

            // Then
            assertThat(instructor.getInstructorDetails()).isNull();
        }

        @Test
        @DisplayName("Should set and get courses")
        void shouldSetAndGetCourses() {
            // Given
            List<Course> courses = new ArrayList<>();
            courses.add(course1);
            courses.add(course2);

            // When
            instructor.setCourses(courses);

            // Then
            assertThat(instructor.getCourses()).isEqualTo(courses);
            assertThat(instructor.getCourses()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Course Management Tests")
    class CourseManagementTests {

        @Test
        @DisplayName("Should add course successfully")
        void shouldAddCourseSuccessfully() {
            // When
            instructor.addCourse(course1);

            // Then
            assertThat(instructor.getCourses()).contains(course1);
            assertThat(course1.getInstructor()).isEqualTo(instructor);
        }

        @Test
        @DisplayName("Should add multiple courses")
        void shouldAddMultipleCourses() {
            // When
            instructor.addCourse(course1);
            instructor.addCourse(course2);

            // Then
            assertThat(instructor.getCourses()).hasSize(2);
            assertThat(instructor.getCourses()).contains(course1, course2);
        }

        @Test
        @DisplayName("Should remove course successfully")
        void shouldRemoveCourseSuccessfully() {
            // Given
            instructor.addCourse(course1);
            assertThat(instructor.getCourses()).contains(course1);

            // When
            instructor.removeCourse(course1);

            // Then
            assertThat(instructor.getCourses()).doesNotContain(course1);
            assertThat(course1.getInstructor()).isNull();
        }

        @Test
        @DisplayName("Should handle removing non-existing course")
        void shouldHandleRemovingNonExistingCourse() {
            // Given
            int initialSize = instructor.getCourses().size();

            // When
            instructor.removeCourse(course1);

            // Then
            assertThat(instructor.getCourses()).hasSize(initialSize);
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should get full name correctly")
        void shouldGetFullNameCorrectly() {
            // When
            String fullName = instructor.getFullName();

            // Then
            assertThat(fullName).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("Should handle bidirectional relationship with instructor details")
        void shouldHandleBidirectionalRelationshipWithInstructorDetails() {
            // When
            instructor.setInstructorDetails(instructorDetails);

            // Then
            assertThat(instructor.getInstructorDetails()).isEqualTo(instructorDetails);
            assertThat(instructorDetails.getInstructor()).isEqualTo(instructor);
        }

        @Test
        @DisplayName("Should handle replacing instructor details")
        void shouldHandleReplacingInstructorDetails() {
            // Given
            InstructorDetails newDetails = new InstructorDetails("https://youtube.com/@newchannel", "Photography");
            instructor.setInstructorDetails(instructorDetails);

            // When
            instructor.setInstructorDetails(newDetails);

            // Then
            assertThat(instructor.getInstructorDetails()).isEqualTo(newDetails);
            assertThat(newDetails.getInstructor()).isEqualTo(instructor);
        }
    }

    @Nested
    @DisplayName("Object Methods Tests")
    class ObjectMethodsTests {

        @Test
        @DisplayName("Should generate toString correctly")
        void shouldGenerateToStringCorrectly() {
            // Given
            instructor.setCreatedAt(LocalDateTime.of(2023, 1, 1, 10, 0));
            instructor.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 10, 0));

            // When
            String toString = instructor.toString();

            // Then
            assertThat(toString).contains("Instructor(");
            assertThat(toString).contains("firstName=John");
            assertThat(toString).contains("lastName=Doe");
            assertThat(toString).contains("email=john.doe@example.com");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null course in add course")
        void shouldHandleNullCourseInAddCourse() {
            // When & Then
            assertThatThrownBy(() -> instructor.addCourse(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null course in remove course")
        void shouldHandleNullCourseInRemoveCourse() {
            // When & Then - The method will throw NullPointerException because it calls course.setInstructor(null)
            assertThatThrownBy(() -> instructor.removeCourse(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle full name with null names")
        void shouldHandleFullNameWithNullNames() {
            // Given
            Instructor nullNameInstructor = new Instructor();

            // When
            String fullName = nullNameInstructor.getFullName();

            // Then
            assertThat(fullName).isEqualTo("null null");
        }

        @Test
        @DisplayName("Should handle full name with empty names")
        void shouldHandleFullNameWithEmptyNames() {
            // Given
            Instructor emptyNameInstructor = new Instructor("", "", "test@example.com");

            // When
            String fullName = emptyNameInstructor.getFullName();

            // Then
            assertThat(fullName).isEqualTo(" ");
        }
    }
}