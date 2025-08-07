package com.coursemanagement.service;

import com.coursemanagement.dto.CourseRequest;
import com.coursemanagement.dto.CourseResponse;
import com.coursemanagement.entity.Course;
import com.coursemanagement.entity.Instructor;
import com.coursemanagement.entity.Review;
import com.coursemanagement.entity.Student;
import com.coursemanagement.exception.ResourceAlreadyExistsException;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.InstructorRepository;
import com.coursemanagement.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Tests")
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course testCourse;
    private Instructor testInstructor;
    private Student testStudent;
    private Review testReview;
    private CourseRequest courseRequest;

    @BeforeEach
    void setUp() {
        testInstructor = new Instructor("John", "Doe", "john.doe@example.com");
        testInstructor.setId(UUID.randomUUID());

        testCourse = new Course("Java Basics", testInstructor);
        testCourse.setId(UUID.randomUUID());
        testCourse.setCreatedAt(LocalDateTime.now());
        testCourse.setUpdatedAt(LocalDateTime.now());

        testStudent = new Student("Jane", "Smith", "jane.smith@example.com");
        testStudent.setId(UUID.randomUUID());

        testReview = new Review("Great course!", testCourse, testStudent);
        testReview.setId(UUID.randomUUID());

        courseRequest = new CourseRequest("Java Basics", testInstructor.getId());
    }

    @Nested
    @DisplayName("Create Course Tests")
    class CreateCourseTests {

        @Test
        @DisplayName("Should create course successfully")
        void shouldCreateCourseSuccessfully() {
            // Given
            when(instructorRepository.findById(courseRequest.getInstructorId())).thenReturn(Optional.of(testInstructor));
            when(courseRepository.existsByTitleAndInstructorId(courseRequest.getTitle(), courseRequest.getInstructorId())).thenReturn(false);
            when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

            // When
            CourseResponse result = courseService.createCourse(courseRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(testCourse.getTitle());
            assertThat(result.getInstructor().getId()).isEqualTo(testInstructor.getId());
            assertThat(result.getReviews()).isEmpty();

            verify(instructorRepository).findById(courseRequest.getInstructorId());
            verify(courseRepository).existsByTitleAndInstructorId(courseRequest.getTitle(), courseRequest.getInstructorId());
            verify(courseRepository).save(any(Course.class));
        }

        @Test
        @DisplayName("Should throw exception when instructor not found")
        void shouldThrowExceptionWhenInstructorNotFound() {
            // Given
            when(instructorRepository.findById(courseRequest.getInstructorId())).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courseService.createCourse(courseRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Instructor")
                    .hasMessageContaining("id")
                    .hasMessageContaining(courseRequest.getInstructorId().toString());

            verify(instructorRepository).findById(courseRequest.getInstructorId());
            verify(courseRepository, never()).existsByTitleAndInstructorId(any(), any());
            verify(courseRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when course title already exists for instructor")
        void shouldThrowExceptionWhenCourseTitleAlreadyExistsForInstructor() {
            // Given
            when(instructorRepository.findById(courseRequest.getInstructorId())).thenReturn(Optional.of(testInstructor));
            when(courseRepository.existsByTitleAndInstructorId(courseRequest.getTitle(), courseRequest.getInstructorId())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> courseService.createCourse(courseRequest))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("title")
                    .hasMessageContaining(courseRequest.getTitle())
                    .hasMessageContaining(testInstructor.getFullName());

            verify(instructorRepository).findById(courseRequest.getInstructorId());
            verify(courseRepository).existsByTitleAndInstructorId(courseRequest.getTitle(), courseRequest.getInstructorId());
            verify(courseRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Course Tests")
    class GetCourseTests {

        @Test
        @DisplayName("Should get course by ID successfully")
        void shouldGetCourseByIdSuccessfully() {
            // Given
            UUID courseId = testCourse.getId();
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));

            // When
            CourseResponse result = courseService.getCourseById(courseId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testCourse.getId());
            assertThat(result.getTitle()).isEqualTo(testCourse.getTitle());
            assertThat(result.getInstructor().getId()).isEqualTo(testInstructor.getId());

            verify(courseRepository).findById(courseId);
        }

        @Test
        @DisplayName("Should throw exception when course not found by ID")
        void shouldThrowExceptionWhenCourseNotFoundById() {
            // Given
            UUID courseId = UUID.randomUUID();
            when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courseService.getCourseById(courseId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("id")
                    .hasMessageContaining(courseId.toString());

            verify(courseRepository).findById(courseId);
        }

        @Test
        @DisplayName("Should get course by ID with reviews successfully")
        void shouldGetCourseByIdWithReviewsSuccessfully() {
            // Given
            UUID courseId = testCourse.getId();
            testCourse.addReview(testReview);
            when(courseRepository.findByIdWithReviews(courseId)).thenReturn(testCourse);

            // When
            CourseResponse result = courseService.getCourseByIdWithReviews(courseId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testCourse.getId());
            assertThat(result.getReviews()).hasSize(1);
            assertThat(result.getReviews().get(0).getComment()).isEqualTo(testReview.getComment());

            verify(courseRepository).findByIdWithReviews(courseId);
        }

        @Test
        @DisplayName("Should throw exception when course not found by ID with reviews")
        void shouldThrowExceptionWhenCourseNotFoundByIdWithReviews() {
            // Given
            UUID courseId = UUID.randomUUID();
            when(courseRepository.findByIdWithReviews(courseId)).thenReturn(null);

            // When & Then
            assertThatThrownBy(() -> courseService.getCourseByIdWithReviews(courseId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("id")
                    .hasMessageContaining(courseId.toString());

            verify(courseRepository).findByIdWithReviews(courseId);
        }

        @Test
        @DisplayName("Should get all courses successfully")
        void shouldGetAllCoursesSuccessfully() {
            // Given
            List<Course> courses = Arrays.asList(testCourse);
            when(courseRepository.findAll()).thenReturn(courses);

            // When
            List<CourseResponse> result = courseService.getAllCourses();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testCourse.getId());
            assertThat(result.get(0).getTitle()).isEqualTo(testCourse.getTitle());

            verify(courseRepository).findAll();
        }

        @Test
        @DisplayName("Should get all courses with reviews successfully")
        void shouldGetAllCoursesWithReviewsSuccessfully() {
            // Given
            testCourse.addReview(testReview);
            List<Course> courses = Arrays.asList(testCourse);
            when(courseRepository.findAllWithReviews()).thenReturn(courses);

            // When
            List<CourseResponse> result = courseService.getAllCoursesWithReviews();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testCourse.getId());
            assertThat(result.get(0).getReviews()).hasSize(1);

            verify(courseRepository).findAllWithReviews();
        }
    }

    @Nested
    @DisplayName("Update Course Tests")
    class UpdateCourseTests {

        @Test
        @DisplayName("Should update course successfully")
        void shouldUpdateCourseSuccessfully() {
            // Given
            UUID courseId = testCourse.getId();
            CourseRequest updateRequest = new CourseRequest("Java Advanced", testInstructor.getId());
            
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
            when(instructorRepository.findById(updateRequest.getInstructorId())).thenReturn(Optional.of(testInstructor));
            when(courseRepository.existsByTitleAndInstructorId(updateRequest.getTitle(), updateRequest.getInstructorId())).thenReturn(false);
            when(courseRepository.save(testCourse)).thenReturn(testCourse);

            // When
            CourseResponse result = courseService.updateCourse(courseId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(courseRepository).findById(courseId);
            verify(instructorRepository).findById(updateRequest.getInstructorId());
            verify(courseRepository).existsByTitleAndInstructorId(updateRequest.getTitle(), updateRequest.getInstructorId());
            verify(courseRepository).save(testCourse);
        }

        @Test
        @DisplayName("Should update course with same title successfully")
        void shouldUpdateCourseWithSameTitleSuccessfully() {
            // Given
            UUID courseId = testCourse.getId();
            CourseRequest updateRequest = new CourseRequest(testCourse.getTitle(), testInstructor.getId());
            
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
            when(instructorRepository.findById(updateRequest.getInstructorId())).thenReturn(Optional.of(testInstructor));
            when(courseRepository.save(testCourse)).thenReturn(testCourse);

            // When
            CourseResponse result = courseService.updateCourse(courseId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(courseRepository).findById(courseId);
            verify(instructorRepository).findById(updateRequest.getInstructorId());
            verify(courseRepository, never()).existsByTitleAndInstructorId(any(), any());
            verify(courseRepository).save(testCourse);
        }

        @Test
        @DisplayName("Should throw exception when course not found for update")
        void shouldThrowExceptionWhenCourseNotFoundForUpdate() {
            // Given
            UUID courseId = UUID.randomUUID();
            CourseRequest updateRequest = new CourseRequest("Java Advanced", testInstructor.getId());
            
            when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courseService.updateCourse(courseId, updateRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("id")
                    .hasMessageContaining(courseId.toString());

            verify(courseRepository).findById(courseId);
            verify(instructorRepository, never()).findById(any());
            verify(courseRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when instructor not found for update")
        void shouldThrowExceptionWhenInstructorNotFoundForUpdate() {
            // Given
            UUID courseId = testCourse.getId();
            UUID newInstructorId = UUID.randomUUID();
            CourseRequest updateRequest = new CourseRequest("Java Advanced", newInstructorId);
            
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
            when(instructorRepository.findById(newInstructorId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courseService.updateCourse(courseId, updateRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Instructor")
                    .hasMessageContaining("id")
                    .hasMessageContaining(newInstructorId.toString());

            verify(courseRepository).findById(courseId);
            verify(instructorRepository).findById(newInstructorId);
            verify(courseRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when updating to existing title for instructor")
        void shouldThrowExceptionWhenUpdatingToExistingTitleForInstructor() {
            // Given
            UUID courseId = testCourse.getId();
            CourseRequest updateRequest = new CourseRequest("Existing Course", testInstructor.getId());
            
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
            when(instructorRepository.findById(updateRequest.getInstructorId())).thenReturn(Optional.of(testInstructor));
            when(courseRepository.existsByTitleAndInstructorId(updateRequest.getTitle(), updateRequest.getInstructorId())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> courseService.updateCourse(courseId, updateRequest))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("title")
                    .hasMessageContaining(updateRequest.getTitle())
                    .hasMessageContaining(testInstructor.getFullName());

            verify(courseRepository).findById(courseId);
            verify(instructorRepository).findById(updateRequest.getInstructorId());
            verify(courseRepository).existsByTitleAndInstructorId(updateRequest.getTitle(), updateRequest.getInstructorId());
            verify(courseRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Delete Course Tests")
    class DeleteCourseTests {

        @Test
        @DisplayName("Should delete course successfully")
        void shouldDeleteCourseSuccessfully() {
            // Given
            UUID courseId = testCourse.getId();
            when(courseRepository.existsById(courseId)).thenReturn(true);

            // When
            courseService.deleteCourse(courseId);

            // Then
            verify(courseRepository).existsById(courseId);
            verify(courseRepository).deleteById(courseId);
        }

        @Test
        @DisplayName("Should throw exception when course not found for deletion")
        void shouldThrowExceptionWhenCourseNotFoundForDeletion() {
            // Given
            UUID courseId = UUID.randomUUID();
            when(courseRepository.existsById(courseId)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> courseService.deleteCourse(courseId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("id")
                    .hasMessageContaining(courseId.toString());

            verify(courseRepository).existsById(courseId);
            verify(courseRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Get Courses By Instructor Tests")
    class GetCoursesByInstructorTests {

        @Test
        @DisplayName("Should get courses by instructor ID successfully")
        void shouldGetCoursesByInstructorIdSuccessfully() {
            // Given
            UUID instructorId = testInstructor.getId();
            List<Course> courses = Arrays.asList(testCourse);
            when(instructorRepository.existsById(instructorId)).thenReturn(true);
            when(courseRepository.findByInstructorId(instructorId)).thenReturn(courses);

            // When
            List<CourseResponse> result = courseService.getCoursesByInstructorId(instructorId);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testCourse.getId());

            verify(instructorRepository).existsById(instructorId);
            verify(courseRepository).findByInstructorId(instructorId);
        }

        @Test
        @DisplayName("Should throw exception when instructor not found for getting courses")
        void shouldThrowExceptionWhenInstructorNotFoundForGettingCourses() {
            // Given
            UUID instructorId = UUID.randomUUID();
            when(instructorRepository.existsById(instructorId)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> courseService.getCoursesByInstructorId(instructorId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Instructor")
                    .hasMessageContaining("id")
                    .hasMessageContaining(instructorId.toString());

            verify(instructorRepository).existsById(instructorId);
            verify(courseRepository, never()).findByInstructorId(any());
        }

        @Test
        @DisplayName("Should get courses by instructor ID with reviews successfully")
        void shouldGetCoursesByInstructorIdWithReviewsSuccessfully() {
            // Given
            UUID instructorId = testInstructor.getId();
            testCourse.addReview(testReview);
            List<Course> courses = Arrays.asList(testCourse);
            when(instructorRepository.existsById(instructorId)).thenReturn(true);
            when(courseRepository.findByInstructorIdWithReviews(instructorId)).thenReturn(courses);

            // When
            List<CourseResponse> result = courseService.getCoursesByInstructorIdWithReviews(instructorId);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testCourse.getId());
            assertThat(result.get(0).getReviews()).hasSize(1);

            verify(instructorRepository).existsById(instructorId);
            verify(courseRepository).findByInstructorIdWithReviews(instructorId);
        }

        @Test
        @DisplayName("Should throw exception when instructor not found for getting courses with reviews")
        void shouldThrowExceptionWhenInstructorNotFoundForGettingCoursesWithReviews() {
            // Given
            UUID instructorId = UUID.randomUUID();
            when(instructorRepository.existsById(instructorId)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> courseService.getCoursesByInstructorIdWithReviews(instructorId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Instructor")
                    .hasMessageContaining("id")
                    .hasMessageContaining(instructorId.toString());

            verify(instructorRepository).existsById(instructorId);
            verify(courseRepository, never()).findByInstructorIdWithReviews(any());
        }
    }

    @Nested
    @DisplayName("Search Courses Tests")
    class SearchCoursesTests {

        @Test
        @DisplayName("Should search courses by title successfully")
        void shouldSearchCoursesByTitleSuccessfully() {
            // Given
            String title = "Java";
            List<Course> courses = Arrays.asList(testCourse);
            when(courseRepository.findByTitleContainingIgnoreCase(title)).thenReturn(courses);

            // When
            List<CourseResponse> result = courseService.searchCoursesByTitle(title);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testCourse.getId());

            verify(courseRepository).findByTitleContainingIgnoreCase(title);
        }

        @Test
        @DisplayName("Should return empty list when no courses match title search")
        void shouldReturnEmptyListWhenNoCoursesMatchTitleSearch() {
            // Given
            String title = "nonexistent";
            when(courseRepository.findByTitleContainingIgnoreCase(title)).thenReturn(Collections.emptyList());

            // When
            List<CourseResponse> result = courseService.searchCoursesByTitle(title);

            // Then
            assertThat(result).isEmpty();
            verify(courseRepository).findByTitleContainingIgnoreCase(title);
        }

        @Test
        @DisplayName("Should search courses by instructor name successfully")
        void shouldSearchCoursesByInstructorNameSuccessfully() {
            // Given
            String name = "John";
            List<Course> courses = Arrays.asList(testCourse);
            when(courseRepository.findByInstructorNameContaining(name)).thenReturn(courses);

            // When
            List<CourseResponse> result = courseService.searchCoursesByInstructorName(name);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testCourse.getId());

            verify(courseRepository).findByInstructorNameContaining(name);
        }
    }

    @Nested
    @DisplayName("Count Tests")
    class CountTests {

        @Test
        @DisplayName("Should return correct course count by instructor")
        void shouldReturnCorrectCourseCountByInstructor() {
            // Given
            UUID instructorId = testInstructor.getId();
            long expectedCount = 3L;
            when(courseRepository.countByInstructorId(instructorId)).thenReturn(expectedCount);

            // When
            long result = courseService.countCoursesByInstructorId(instructorId);

            // Then
            assertThat(result).isEqualTo(expectedCount);
            verify(courseRepository).countByInstructorId(instructorId);
        }
    }
}