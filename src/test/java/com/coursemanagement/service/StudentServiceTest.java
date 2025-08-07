package com.coursemanagement.service;

import com.coursemanagement.dto.EnrollmentRequest;
import com.coursemanagement.dto.EnrollmentResponse;
import com.coursemanagement.dto.StudentRequest;
import com.coursemanagement.dto.StudentResponse;
import com.coursemanagement.dto.UnenrollmentResponse;
import com.coursemanagement.entity.Course;
import com.coursemanagement.entity.Instructor;
import com.coursemanagement.entity.Student;
import com.coursemanagement.exception.ResourceAlreadyExistsException;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.StudentRepository;
import com.coursemanagement.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student testStudent;
    private Course testCourse;
    private Instructor testInstructor;
    private StudentRequest studentRequest;
    private EnrollmentRequest enrollmentRequest;

    @BeforeEach
    void setUp() {
        testInstructor = new Instructor("John", "Doe", "john.doe@example.com");
        testInstructor.setId(UUID.randomUUID());

        testCourse = new Course("Java Basics", testInstructor);
        testCourse.setId(UUID.randomUUID());

        testStudent = new Student("Jane", "Smith", "jane.smith@example.com");
        testStudent.setId(UUID.randomUUID());
        testStudent.setCreatedAt(LocalDateTime.now());
        testStudent.setUpdatedAt(LocalDateTime.now());

        studentRequest = new StudentRequest("Jane", "Smith", "jane.smith@example.com");
        enrollmentRequest = new EnrollmentRequest(testCourse.getId());
    }

    @Nested
    @DisplayName("Create Student Tests")
    class CreateStudentTests {

        @Test
        @DisplayName("Should create student successfully")
        void shouldCreateStudentSuccessfully() {
            // Given
            when(studentRepository.existsByEmail(studentRequest.getEmail())).thenReturn(false);
            when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

            // When
            StudentResponse result = studentService.createStudent(studentRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo(testStudent.getFirstName());
            assertThat(result.getLastName()).isEqualTo(testStudent.getLastName());
            assertThat(result.getEmail()).isEqualTo(testStudent.getEmail());
            assertThat(result.getCourses()).isEmpty();

            verify(studentRepository).existsByEmail(studentRequest.getEmail());
            verify(studentRepository).save(any(Student.class));
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // Given
            when(studentRepository.existsByEmail(studentRequest.getEmail())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> studentService.createStudent(studentRequest))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("email")
                    .hasMessageContaining(studentRequest.getEmail());

            verify(studentRepository).existsByEmail(studentRequest.getEmail());
            verify(studentRepository, never()).save(any(Student.class));
        }
    }

    @Nested
    @DisplayName("Get Student Tests")
    class GetStudentTests {

        @Test
        @DisplayName("Should get student by ID successfully")
        void shouldGetStudentByIdSuccessfully() {
            // Given
            UUID studentId = testStudent.getId();
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));

            // When
            StudentResponse result = studentService.getStudentById(studentId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testStudent.getId());
            assertThat(result.getFirstName()).isEqualTo(testStudent.getFirstName());
            assertThat(result.getLastName()).isEqualTo(testStudent.getLastName());
            assertThat(result.getEmail()).isEqualTo(testStudent.getEmail());

            verify(studentRepository).findById(studentId);
        }

        @Test
        @DisplayName("Should throw exception when student not found by ID")
        void shouldThrowExceptionWhenStudentNotFoundById() {
            // Given
            UUID studentId = UUID.randomUUID();
            when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> studentService.getStudentById(studentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("id")
                    .hasMessageContaining(studentId.toString());

            verify(studentRepository).findById(studentId);
        }

        @Test
        @DisplayName("Should get student by ID with courses successfully")
        void shouldGetStudentByIdWithCoursesSuccessfully() {
            // Given
            UUID studentId = testStudent.getId();
            testStudent.enrollInCourse(testCourse);
            when(studentRepository.findByIdWithCourses(studentId)).thenReturn(Optional.of(testStudent));

            // When
            StudentResponse result = studentService.getStudentByIdWithCourses(studentId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testStudent.getId());
            assertThat(result.getCourses()).hasSize(1);
            assertThat(result.getCourses().get(0).getTitle()).isEqualTo(testCourse.getTitle());

            verify(studentRepository).findByIdWithCourses(studentId);
        }

        @Test
        @DisplayName("Should throw exception when student not found by ID with courses")
        void shouldThrowExceptionWhenStudentNotFoundByIdWithCourses() {
            // Given
            UUID studentId = UUID.randomUUID();
            when(studentRepository.findByIdWithCourses(studentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> studentService.getStudentByIdWithCourses(studentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("id")
                    .hasMessageContaining(studentId.toString());

            verify(studentRepository).findByIdWithCourses(studentId);
        }

        @Test
        @DisplayName("Should get all students successfully")
        void shouldGetAllStudentsSuccessfully() {
            // Given
            List<Student> students = Arrays.asList(testStudent);
            when(studentRepository.findAll()).thenReturn(students);

            // When
            List<StudentResponse> result = studentService.getAllStudents();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testStudent.getId());
            assertThat(result.get(0).getFirstName()).isEqualTo(testStudent.getFirstName());

            verify(studentRepository).findAll();
        }

        @Test
        @DisplayName("Should get all students with courses successfully")
        void shouldGetAllStudentsWithCoursesSuccessfully() {
            // Given
            testStudent.enrollInCourse(testCourse);
            List<Student> students = Arrays.asList(testStudent);
            when(studentRepository.findAllWithCourses()).thenReturn(students);

            // When
            List<StudentResponse> result = studentService.getAllStudentsWithCourses();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testStudent.getId());
            assertThat(result.get(0).getCourses()).hasSize(1);

            verify(studentRepository).findAllWithCourses();
        }
    }

    @Nested
    @DisplayName("Update Student Tests")
    class UpdateStudentTests {

        @Test
        @DisplayName("Should update student successfully")
        void shouldUpdateStudentSuccessfully() {
            // Given
            UUID studentId = testStudent.getId();
            StudentRequest updateRequest = new StudentRequest("Jane Updated", "Smith Updated", "jane.updated@example.com");
            
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
            when(studentRepository.existsByEmail(updateRequest.getEmail())).thenReturn(false);
            when(studentRepository.save(testStudent)).thenReturn(testStudent);

            // When
            StudentResponse result = studentService.updateStudent(studentId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(studentRepository).findById(studentId);
            verify(studentRepository).existsByEmail(updateRequest.getEmail());
            verify(studentRepository).save(testStudent);
        }

        @Test
        @DisplayName("Should update student with same email successfully")
        void shouldUpdateStudentWithSameEmailSuccessfully() {
            // Given
            UUID studentId = testStudent.getId();
            StudentRequest updateRequest = new StudentRequest("Jane Updated", "Smith Updated", testStudent.getEmail());
            
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
            when(studentRepository.save(testStudent)).thenReturn(testStudent);

            // When
            StudentResponse result = studentService.updateStudent(studentId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(studentRepository).findById(studentId);
            verify(studentRepository, never()).existsByEmail(any());
            verify(studentRepository).save(testStudent);
        }

        @Test
        @DisplayName("Should throw exception when updating to existing email")
        void shouldThrowExceptionWhenUpdatingToExistingEmail() {
            // Given
            UUID studentId = testStudent.getId();
            StudentRequest updateRequest = new StudentRequest("Jane", "Smith", "existing@example.com");
            
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
            when(studentRepository.existsByEmail(updateRequest.getEmail())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> studentService.updateStudent(studentId, updateRequest))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("email")
                    .hasMessageContaining(updateRequest.getEmail());

            verify(studentRepository).findById(studentId);
            verify(studentRepository).existsByEmail(updateRequest.getEmail());
            verify(studentRepository, never()).save(any(Student.class));
        }

        @Test
        @DisplayName("Should throw exception when student not found for update")
        void shouldThrowExceptionWhenStudentNotFoundForUpdate() {
            // Given
            UUID studentId = UUID.randomUUID();
            StudentRequest updateRequest = new StudentRequest("Jane", "Smith", "jane@example.com");
            
            when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> studentService.updateStudent(studentId, updateRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("id")
                    .hasMessageContaining(studentId.toString());

            verify(studentRepository).findById(studentId);
            verify(studentRepository, never()).save(any(Student.class));
        }
    }

    @Nested
    @DisplayName("Delete Student Tests")
    class DeleteStudentTests {

        @Test
        @DisplayName("Should delete student successfully")
        void shouldDeleteStudentSuccessfully() {
            // Given
            UUID studentId = testStudent.getId();
            testStudent.enrollInCourse(testCourse);
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));

            // When
            studentService.deleteStudent(studentId);

            // Then
            verify(studentRepository).findById(studentId);
            verify(studentRepository).save(testStudent);
            verify(studentRepository).delete(testStudent);
            assertThat(testStudent.getCourses()).isEmpty();
        }

        @Test
        @DisplayName("Should throw exception when student not found for deletion")
        void shouldThrowExceptionWhenStudentNotFoundForDeletion() {
            // Given
            UUID studentId = UUID.randomUUID();
            when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> studentService.deleteStudent(studentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("id")
                    .hasMessageContaining(studentId.toString());

            verify(studentRepository).findById(studentId);
            verify(studentRepository, never()).delete(any(Student.class));
        }
    }

    @Nested
    @DisplayName("Enrollment Tests")
    class EnrollmentTests {

        @Test
        @DisplayName("Should enroll student in course successfully")
        void shouldEnrollStudentInCourseSuccessfully() {
            // Given
            UUID studentId = testStudent.getId();
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
            when(courseRepository.findById(testCourse.getId())).thenReturn(Optional.of(testCourse));
            when(studentRepository.save(testStudent)).thenReturn(testStudent);

            // When
            EnrollmentResponse result = studentService.enrollStudentInCourse(studentId, enrollmentRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStudent().getId()).isEqualTo(testStudent.getId());
            assertThat(result.getCourse().getId()).isEqualTo(testCourse.getId());
            assertThat(result.getMessage()).contains("successfully enrolled");

            verify(studentRepository).findById(studentId);
            verify(courseRepository).findById(testCourse.getId());
            verify(studentRepository).save(testStudent);
        }

        @Test
        @DisplayName("Should throw exception when student not found for enrollment")
        void shouldThrowExceptionWhenStudentNotFoundForEnrollment() {
            // Given
            UUID studentId = UUID.randomUUID();
            when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> studentService.enrollStudentInCourse(studentId, enrollmentRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("id")
                    .hasMessageContaining(studentId.toString());

            verify(studentRepository).findById(studentId);
            verify(courseRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should throw exception when course not found for enrollment")
        void shouldThrowExceptionWhenCourseNotFoundForEnrollment() {
            // Given
            UUID studentId = testStudent.getId();
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
            when(courseRepository.findById(testCourse.getId())).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> studentService.enrollStudentInCourse(studentId, enrollmentRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("id")
                    .hasMessageContaining(testCourse.getId().toString());

            verify(studentRepository).findById(studentId);
            verify(courseRepository).findById(testCourse.getId());
        }

        @Test
        @DisplayName("Should throw exception when student already enrolled")
        void shouldThrowExceptionWhenStudentAlreadyEnrolled() {
            // Given
            UUID studentId = testStudent.getId();
            testStudent.enrollInCourse(testCourse);
            
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
            when(courseRepository.findById(testCourse.getId())).thenReturn(Optional.of(testCourse));

            // When & Then
            assertThatThrownBy(() -> studentService.enrollStudentInCourse(studentId, enrollmentRequest))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessageContaining("Enrollment")
                    .hasMessageContaining(testStudent.getFullName())
                    .hasMessageContaining(testCourse.getTitle());

            verify(studentRepository).findById(studentId);
            verify(courseRepository).findById(testCourse.getId());
            verify(studentRepository, never()).save(any(Student.class));
        }
    }

    @Nested
    @DisplayName("Unenrollment Tests")
    class UnenrollmentTests {

        @Test
        @DisplayName("Should unenroll student from course successfully")
        void shouldUnenrollStudentFromCourseSuccessfully() {
            // Given
            UUID studentId = testStudent.getId();
            testStudent.enrollInCourse(testCourse);
            
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
            when(courseRepository.findById(testCourse.getId())).thenReturn(Optional.of(testCourse));
            when(studentRepository.save(testStudent)).thenReturn(testStudent);

            // When
            UnenrollmentResponse result = studentService.unenrollStudentFromCourse(studentId, new EnrollmentRequest(testCourse.getId()));

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStudent().getId()).isEqualTo(testStudent.getId());
            assertThat(result.getCourse().getId()).isEqualTo(testCourse.getId());
            assertThat(result.getMessage()).contains("successfully unenrolled");

            verify(studentRepository).findById(studentId);
            verify(courseRepository).findById(testCourse.getId());
            verify(studentRepository).save(testStudent);
        }

        @Test
        @DisplayName("Should throw exception when student not found for unenrollment")
        void shouldThrowExceptionWhenStudentNotFoundForUnenrollment() {
            // Given
            UUID studentId = UUID.randomUUID();
            when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> studentService.unenrollStudentFromCourse(studentId, new EnrollmentRequest(testCourse.getId())))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("id")
                    .hasMessageContaining(studentId.toString());

            verify(studentRepository).findById(studentId);
            verify(courseRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should throw exception when course not found for unenrollment")
        void shouldThrowExceptionWhenCourseNotFoundForUnenrollment() {
            // Given
            UUID studentId = testStudent.getId();
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
            when(courseRepository.findById(testCourse.getId())).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> studentService.unenrollStudentFromCourse(studentId, new EnrollmentRequest(testCourse.getId())))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("id")
                    .hasMessageContaining(testCourse.getId().toString());

            verify(studentRepository).findById(studentId);
            verify(courseRepository).findById(testCourse.getId());
        }

        @Test
        @DisplayName("Should throw exception when student not enrolled in course")
        void shouldThrowExceptionWhenStudentNotEnrolledInCourse() {
            // Given
            UUID studentId = testStudent.getId();
            
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
            when(courseRepository.findById(testCourse.getId())).thenReturn(Optional.of(testCourse));

            // When & Then
            assertThatThrownBy(() -> studentService.unenrollStudentFromCourse(studentId, new EnrollmentRequest(testCourse.getId())))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Enrollment")
                    .hasMessageContaining(testStudent.getFullName())
                    .hasMessageContaining(testCourse.getTitle());

            verify(studentRepository).findById(studentId);
            verify(courseRepository).findById(testCourse.getId());
            verify(studentRepository, never()).save(any(Student.class));
        }
    }

    @Nested
    @DisplayName("Existence Check Tests")
    class ExistenceCheckTests {

        @Test
        @DisplayName("Should return true when student exists by email")
        void shouldReturnTrueWhenStudentExistsByEmail() {
            // Given
            String email = "test@example.com";
            when(studentRepository.existsByEmail(email)).thenReturn(true);

            // When
            boolean result = studentService.existsByEmail(email);

            // Then
            assertThat(result).isTrue();
            verify(studentRepository).existsByEmail(email);
        }

        @Test
        @DisplayName("Should return false when student does not exist by email")
        void shouldReturnFalseWhenStudentDoesNotExistByEmail() {
            // Given
            String email = "test@example.com";
            when(studentRepository.existsByEmail(email)).thenReturn(false);

            // When
            boolean result = studentService.existsByEmail(email);

            // Then
            assertThat(result).isFalse();
            verify(studentRepository).existsByEmail(email);
        }

        @Test
        @DisplayName("Should return true when student exists by ID")
        void shouldReturnTrueWhenStudentExistsById() {
            // Given
            UUID studentId = UUID.randomUUID();
            when(studentRepository.existsById(studentId)).thenReturn(true);

            // When
            boolean result = studentService.existsById(studentId);

            // Then
            assertThat(result).isTrue();
            verify(studentRepository).existsById(studentId);
        }

        @Test
        @DisplayName("Should return false when student does not exist by ID")
        void shouldReturnFalseWhenStudentDoesNotExistById() {
            // Given
            UUID studentId = UUID.randomUUID();
            when(studentRepository.existsById(studentId)).thenReturn(false);

            // When
            boolean result = studentService.existsById(studentId);

            // Then
            assertThat(result).isFalse();
            verify(studentRepository).existsById(studentId);
        }
    }

    @Nested
    @DisplayName("Count Tests")
    class CountTests {

        @Test
        @DisplayName("Should return correct student count in course")
        void shouldReturnCorrectStudentCountInCourse() {
            // Given
            long expectedCount = 5L;
            when(studentRepository.countStudentsInCourse(testCourse.getId())).thenReturn(expectedCount);

            // When
            long result = studentService.countStudentsInCourse(testCourse.getId());

            // Then
            assertThat(result).isEqualTo(expectedCount);
            verify(studentRepository).countStudentsInCourse(testCourse.getId());
        }
    }
}