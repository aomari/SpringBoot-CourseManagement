package com.coursemanagement.service;

import com.coursemanagement.dto.ReviewRequest;
import com.coursemanagement.dto.ReviewResponse;
import com.coursemanagement.entity.Course;
import com.coursemanagement.entity.Instructor;
import com.coursemanagement.entity.Review;
import com.coursemanagement.entity.Student;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.ReviewRepository;
import com.coursemanagement.repository.StudentRepository;
import com.coursemanagement.service.impl.ReviewServiceImpl;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService Tests")
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review testReview;
    private Course testCourse;
    private Student testStudent;
    private Instructor testInstructor;
    private ReviewRequest reviewRequest;

    @BeforeEach
    void setUp() {
        testInstructor = new Instructor("John", "Doe", "john.doe@example.com");
        testInstructor.setId(UUID.randomUUID());

        testCourse = new Course("Java Basics", testInstructor);
        testCourse.setId(UUID.randomUUID());

        testStudent = new Student("Jane", "Smith", "jane.smith@example.com");
        testStudent.setId(UUID.randomUUID());

        testReview = new Review("Great course!", testCourse, testStudent);
        testReview.setId(UUID.randomUUID());

        reviewRequest = new ReviewRequest("Great course!", testStudent.getId());
    }

    @Nested
    @DisplayName("Create Review Tests")
    class CreateReviewTests {

        @Test
        @DisplayName("Should create review successfully")
        void shouldCreateReviewSuccessfully() {
            // Given
            UUID courseId = testCourse.getId();
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
            when(studentRepository.findById(reviewRequest.getStudentId())).thenReturn(Optional.of(testStudent));
            when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

            // When
            ReviewResponse result = reviewService.createReview(courseId, reviewRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getComment()).isEqualTo(testReview.getComment());
            assertThat(result.getCourse().getId()).isEqualTo(testCourse.getId());
            assertThat(result.getStudent().getId()).isEqualTo(testStudent.getId());

            verify(courseRepository).findById(courseId);
            verify(studentRepository).findById(reviewRequest.getStudentId());
            verify(reviewRepository).save(any(Review.class));
        }

        @Test
        @DisplayName("Should throw exception when course not found")
        void shouldThrowExceptionWhenCourseNotFound() {
            // Given
            UUID courseId = UUID.randomUUID();
            when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> reviewService.createReview(courseId, reviewRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("id")
                    .hasMessageContaining(courseId.toString());

            verify(courseRepository).findById(courseId);
            verify(studentRepository, never()).findById(any());
            verify(reviewRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when student not found")
        void shouldThrowExceptionWhenStudentNotFound() {
            // Given
            UUID courseId = testCourse.getId();
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
            when(studentRepository.findById(reviewRequest.getStudentId())).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> reviewService.createReview(courseId, reviewRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("id")
                    .hasMessageContaining(reviewRequest.getStudentId().toString());

            verify(courseRepository).findById(courseId);
            verify(studentRepository).findById(reviewRequest.getStudentId());
            verify(reviewRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Review Tests")
    class GetReviewTests {

        @Test
        @DisplayName("Should get review by ID successfully")
        void shouldGetReviewByIdSuccessfully() {
            // Given
            UUID reviewId = testReview.getId();
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(testReview));

            // When
            ReviewResponse result = reviewService.getReviewById(reviewId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testReview.getId());
            assertThat(result.getComment()).isEqualTo(testReview.getComment());
            assertThat(result.getCourse().getId()).isEqualTo(testCourse.getId());
            assertThat(result.getStudent().getId()).isEqualTo(testStudent.getId());

            verify(reviewRepository).findById(reviewId);
        }

        @Test
        @DisplayName("Should throw exception when review not found by ID")
        void shouldThrowExceptionWhenReviewNotFoundById() {
            // Given
            UUID reviewId = UUID.randomUUID();
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> reviewService.getReviewById(reviewId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Review")
                    .hasMessageContaining("id")
                    .hasMessageContaining(reviewId.toString());

            verify(reviewRepository).findById(reviewId);
        }

        @Test
        @DisplayName("Should get all reviews successfully")
        void shouldGetAllReviewsSuccessfully() {
            // Given
            List<Review> reviews = Arrays.asList(testReview);
            when(reviewRepository.findAllWithCourseInstructorAndStudent()).thenReturn(reviews);

            // When
            List<ReviewResponse> result = reviewService.getAllReviews();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testReview.getId());
            assertThat(result.get(0).getComment()).isEqualTo(testReview.getComment());

            verify(reviewRepository).findAllWithCourseInstructorAndStudent();
        }

        @Test
        @DisplayName("Should return empty list when no reviews exist")
        void shouldReturnEmptyListWhenNoReviewsExist() {
            // Given
            when(reviewRepository.findAllWithCourseInstructorAndStudent()).thenReturn(Collections.emptyList());

            // When
            List<ReviewResponse> result = reviewService.getAllReviews();

            // Then
            assertThat(result).isEmpty();
            verify(reviewRepository).findAllWithCourseInstructorAndStudent();
        }
    }

    @Nested
    @DisplayName("Update Review Tests")
    class UpdateReviewTests {

        @Test
        @DisplayName("Should update review successfully")
        void shouldUpdateReviewSuccessfully() {
            // Given
            UUID reviewId = testReview.getId();
            ReviewRequest updateRequest = new ReviewRequest("Updated comment", testStudent.getId());
            
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(testReview));
            when(reviewRepository.save(testReview)).thenReturn(testReview);

            // When
            ReviewResponse result = reviewService.updateReview(reviewId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(reviewRepository).findById(reviewId);
            verify(reviewRepository).save(testReview);
        }

        @Test
        @DisplayName("Should update review with different student successfully")
        void shouldUpdateReviewWithDifferentStudentSuccessfully() {
            // Given
            UUID reviewId = testReview.getId();
            Student newStudent = new Student("New", "Student", "new@example.com");
            newStudent.setId(UUID.randomUUID());
            ReviewRequest updateRequest = new ReviewRequest("Updated comment", newStudent.getId());
            
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(testReview));
            when(studentRepository.findById(newStudent.getId())).thenReturn(Optional.of(newStudent));
            when(reviewRepository.save(testReview)).thenReturn(testReview);

            // When
            ReviewResponse result = reviewService.updateReview(reviewId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(reviewRepository).findById(reviewId);
            verify(studentRepository).findById(newStudent.getId());
            verify(reviewRepository).save(testReview);
        }

        @Test
        @DisplayName("Should throw exception when review not found for update")
        void shouldThrowExceptionWhenReviewNotFoundForUpdate() {
            // Given
            UUID reviewId = UUID.randomUUID();
            ReviewRequest updateRequest = new ReviewRequest("Updated comment", testStudent.getId());
            
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> reviewService.updateReview(reviewId, updateRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Review")
                    .hasMessageContaining("id")
                    .hasMessageContaining(reviewId.toString());

            verify(reviewRepository).findById(reviewId);
            verify(reviewRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when new student not found")
        void shouldThrowExceptionWhenNewStudentNotFound() {
            // Given
            UUID reviewId = testReview.getId();
            UUID newStudentId = UUID.randomUUID();
            ReviewRequest updateRequest = new ReviewRequest("Updated comment", newStudentId);
            
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(testReview));
            when(studentRepository.findById(newStudentId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> reviewService.updateReview(reviewId, updateRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Student")
                    .hasMessageContaining("id")
                    .hasMessageContaining(newStudentId.toString());

            verify(reviewRepository).findById(reviewId);
            verify(studentRepository).findById(newStudentId);
            verify(reviewRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Delete Review Tests")
    class DeleteReviewTests {

        @Test
        @DisplayName("Should delete review successfully")
        void shouldDeleteReviewSuccessfully() {
            // Given
            UUID reviewId = testReview.getId();
            when(reviewRepository.existsById(reviewId)).thenReturn(true);

            // When
            reviewService.deleteReview(reviewId);

            // Then
            verify(reviewRepository).existsById(reviewId);
            verify(reviewRepository).deleteById(reviewId);
        }

        @Test
        @DisplayName("Should throw exception when review not found for deletion")
        void shouldThrowExceptionWhenReviewNotFoundForDeletion() {
            // Given
            UUID reviewId = UUID.randomUUID();
            when(reviewRepository.existsById(reviewId)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> reviewService.deleteReview(reviewId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Review")
                    .hasMessageContaining("id")
                    .hasMessageContaining(reviewId.toString());

            verify(reviewRepository).existsById(reviewId);
            verify(reviewRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Get Reviews By Course Tests")
    class GetReviewsByCourseTests {

        @Test
        @DisplayName("Should get reviews by course ID successfully")
        void shouldGetReviewsByCourseIdSuccessfully() {
            // Given
            UUID courseId = testCourse.getId();
            List<Review> reviews = Arrays.asList(testReview);
            when(courseRepository.existsById(courseId)).thenReturn(true);
            when(reviewRepository.findByCourseId(courseId)).thenReturn(reviews);

            // When
            List<ReviewResponse> result = reviewService.getReviewsByCourseId(courseId);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testReview.getId());

            verify(courseRepository).existsById(courseId);
            verify(reviewRepository).findByCourseId(courseId);
        }

        @Test
        @DisplayName("Should throw exception when course not found for getting reviews")
        void shouldThrowExceptionWhenCourseNotFoundForGettingReviews() {
            // Given
            UUID courseId = UUID.randomUUID();
            when(courseRepository.existsById(courseId)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> reviewService.getReviewsByCourseId(courseId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("id")
                    .hasMessageContaining(courseId.toString());

            verify(courseRepository).existsById(courseId);
            verify(reviewRepository, never()).findByCourseId(any());
        }

        @Test
        @DisplayName("Should get reviews by course ID ordered by date successfully")
        void shouldGetReviewsByCourseIdOrderedByDateSuccessfully() {
            // Given
            UUID courseId = testCourse.getId();
            List<Review> reviews = Arrays.asList(testReview);
            when(courseRepository.existsById(courseId)).thenReturn(true);
            when(reviewRepository.findByCourseIdOrderByCreatedAtDesc(courseId)).thenReturn(reviews);

            // When
            List<ReviewResponse> result = reviewService.getReviewsByCourseIdOrderedByDate(courseId);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testReview.getId());

            verify(courseRepository).existsById(courseId);
            verify(reviewRepository).findByCourseIdOrderByCreatedAtDesc(courseId);
        }

        @Test
        @DisplayName("Should throw exception when course not found for getting ordered reviews")
        void shouldThrowExceptionWhenCourseNotFoundForGettingOrderedReviews() {
            // Given
            UUID courseId = UUID.randomUUID();
            when(courseRepository.existsById(courseId)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> reviewService.getReviewsByCourseIdOrderedByDate(courseId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Course")
                    .hasMessageContaining("id")
                    .hasMessageContaining(courseId.toString());

            verify(courseRepository).existsById(courseId);
            verify(reviewRepository, never()).findByCourseIdOrderByCreatedAtDesc(any());
        }
    }

    @Nested
    @DisplayName("Search Reviews Tests")
    class SearchReviewsTests {

        @Test
        @DisplayName("Should search reviews by comment successfully")
        void shouldSearchReviewsByCommentSuccessfully() {
            // Given
            String keyword = "great";
            List<Review> reviews = Arrays.asList(testReview);
            when(reviewRepository.findByCommentContainingIgnoreCase(keyword)).thenReturn(reviews);

            // When
            List<ReviewResponse> result = reviewService.searchReviewsByComment(keyword);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testReview.getId());

            verify(reviewRepository).findByCommentContainingIgnoreCase(keyword);
        }

        @Test
        @DisplayName("Should return empty list when no reviews match comment search")
        void shouldReturnEmptyListWhenNoReviewsMatchCommentSearch() {
            // Given
            String keyword = "nonexistent";
            when(reviewRepository.findByCommentContainingIgnoreCase(keyword)).thenReturn(Collections.emptyList());

            // When
            List<ReviewResponse> result = reviewService.searchReviewsByComment(keyword);

            // Then
            assertThat(result).isEmpty();
            verify(reviewRepository).findByCommentContainingIgnoreCase(keyword);
        }

        @Test
        @DisplayName("Should get reviews by instructor ID successfully")
        void shouldGetReviewsByInstructorIdSuccessfully() {
            // Given
            UUID instructorId = testInstructor.getId();
            List<Review> reviews = Arrays.asList(testReview);
            when(reviewRepository.findByInstructorId(instructorId)).thenReturn(reviews);

            // When
            List<ReviewResponse> result = reviewService.getReviewsByInstructorId(instructorId);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testReview.getId());

            verify(reviewRepository).findByInstructorId(instructorId);
        }

        @Test
        @DisplayName("Should search reviews by course title successfully")
        void shouldSearchReviewsByCourseTitleSuccessfully() {
            // Given
            String title = "Java";
            List<Review> reviews = Arrays.asList(testReview);
            when(reviewRepository.findByCourseTitle(title)).thenReturn(reviews);

            // When
            List<ReviewResponse> result = reviewService.searchReviewsByCourseTitle(title);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testReview.getId());

            verify(reviewRepository).findByCourseTitle(title);
        }
    }

    @Nested
    @DisplayName("Existence Check Tests")
    class ExistenceCheckTests {

        @Test
        @DisplayName("Should return true when review exists by ID")
        void shouldReturnTrueWhenReviewExistsById() {
            // Given
            UUID reviewId = testReview.getId();
            when(reviewRepository.existsById(reviewId)).thenReturn(true);

            // When
            boolean result = reviewService.existsById(reviewId);

            // Then
            assertThat(result).isTrue();
            verify(reviewRepository).existsById(reviewId);
        }

        @Test
        @DisplayName("Should return false when review does not exist by ID")
        void shouldReturnFalseWhenReviewDoesNotExistById() {
            // Given
            UUID reviewId = UUID.randomUUID();
            when(reviewRepository.existsById(reviewId)).thenReturn(false);

            // When
            boolean result = reviewService.existsById(reviewId);

            // Then
            assertThat(result).isFalse();
            verify(reviewRepository).existsById(reviewId);
        }
    }

    @Nested
    @DisplayName("Count Tests")
    class CountTests {

        @Test
        @DisplayName("Should return correct review count by course")
        void shouldReturnCorrectReviewCountByCourse() {
            // Given
            UUID courseId = testCourse.getId();
            long expectedCount = 5L;
            when(courseRepository.existsById(courseId)).thenReturn(true);
            when(reviewRepository.countByCourseId(courseId)).thenReturn(expectedCount);

            // When
            long result = reviewService.countReviewsByCourseId(courseId);

            // Then
            assertThat(result).isEqualTo(expectedCount);
            verify(courseRepository).existsById(courseId);
            verify(reviewRepository).countByCourseId(courseId);
        }
    }
}