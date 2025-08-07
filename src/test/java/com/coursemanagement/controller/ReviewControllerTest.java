package com.coursemanagement.controller;

import com.coursemanagement.dto.ReviewRequest;
import com.coursemanagement.dto.ReviewResponse;
import com.coursemanagement.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@DisplayName("ReviewController Tests")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReviewRequest reviewRequest;
    private ReviewResponse reviewResponse;
    private UUID reviewId;
    private UUID courseId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        reviewId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        
        reviewRequest = new ReviewRequest("Great course!", studentId);
        
        ReviewResponse.CourseInfo courseInfo = new ReviewResponse.CourseInfo(
                courseId, "Java Basics", "John Instructor"
        );
        
        ReviewResponse.StudentInfo studentInfo = new ReviewResponse.StudentInfo(
                studentId, "Jane Student", "jane@example.com"
        );
        
        reviewResponse = new ReviewResponse(
                reviewId,
                "Great course!",
                LocalDateTime.now(),
                LocalDateTime.now(),
                courseInfo,
                studentInfo
        );
    }

    @Nested
    @DisplayName("Create Review Tests")
    class CreateReviewTests {

        @Test
        @DisplayName("Should create review for course successfully")
        void shouldCreateReviewForCourseSuccessfully() throws Exception {
            // Given
            when(reviewService.createReview(eq(courseId), any())).thenReturn(reviewResponse);

            // When & Then
            mockMvc.perform(post("/api/v1/courses/{courseId}/reviews", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reviewRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(reviewId.toString()))
                    .andExpect(jsonPath("$.comment").value("Great course!"))
                    .andExpect(jsonPath("$.course.id").value(courseId.toString()));

            verify(reviewService).createReview(eq(courseId), any());
        }

        @Test
        @DisplayName("Should return 400 for invalid review request")
        void shouldReturn400ForInvalidReviewRequest() throws Exception {
            // Given
            ReviewRequest invalidRequest = new ReviewRequest("", null);

            // When & Then
            mockMvc.perform(post("/api/v1/courses/{courseId}/reviews", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(reviewService, never()).createReview(any(), any());
        }
    }

    @Nested
    @DisplayName("Get Review Tests")
    class GetReviewTests {

        @Test
        @DisplayName("Should get review by ID successfully")
        void shouldGetReviewByIdSuccessfully() throws Exception {
            // Given
            when(reviewService.getReviewById(reviewId)).thenReturn(reviewResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/reviews/{id}", reviewId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(reviewId.toString()))
                    .andExpect(jsonPath("$.comment").value("Great course!"));

            verify(reviewService).getReviewById(reviewId);
        }

        @Test
        @DisplayName("Should get all reviews successfully")
        void shouldGetAllReviewsSuccessfully() throws Exception {
            // Given
            List<ReviewResponse> reviews = Arrays.asList(reviewResponse);
            when(reviewService.getAllReviews()).thenReturn(reviews);

            // When & Then
            mockMvc.perform(get("/api/v1/reviews"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(reviewId.toString()));

            verify(reviewService).getAllReviews();
        }

        @Test
        @DisplayName("Should get reviews by course ID successfully")
        void shouldGetReviewsByCourseIdSuccessfully() throws Exception {
            // Given
            List<ReviewResponse> reviews = Arrays.asList(reviewResponse);
            when(reviewService.getReviewsByCourseIdOrderedByDate(courseId)).thenReturn(reviews);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{courseId}/reviews", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(reviewService).getReviewsByCourseIdOrderedByDate(courseId);
        }

        @Test
        @DisplayName("Should get reviews by course ID ordered by date successfully")
        void shouldGetReviewsByCourseIdOrderedByDateSuccessfully() throws Exception {
            // Given
            List<ReviewResponse> reviews = Arrays.asList(reviewResponse);
            when(reviewService.getReviewsByCourseIdOrderedByDate(courseId)).thenReturn(reviews);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{courseId}/reviews", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(reviewService).getReviewsByCourseIdOrderedByDate(courseId);
        }
    }

    @Nested
    @DisplayName("Update Review Tests")
    class UpdateReviewTests {

        @Test
        @DisplayName("Should update review successfully")
        void shouldUpdateReviewSuccessfully() throws Exception {
            // Given
            ReviewRequest updateRequest = new ReviewRequest("Updated review!", studentId);
            ReviewResponse updatedResponse = new ReviewResponse(
                    reviewId,
                    "Updated review!",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    reviewResponse.getCourse(),
                    reviewResponse.getStudent()
            );
            when(reviewService.updateReview(eq(reviewId), any())).thenReturn(updatedResponse);

            // When & Then
            mockMvc.perform(put("/api/v1/reviews/{id}", reviewId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.comment").value("Updated review!"));

            verify(reviewService).updateReview(eq(reviewId), any());
        }

        @Test
        @DisplayName("Should return 400 for invalid update data")
        void shouldReturn400ForInvalidUpdateData() throws Exception {
            // Given
            ReviewRequest invalidRequest = new ReviewRequest("", null);

            // When & Then
            mockMvc.perform(put("/api/v1/reviews/{id}", reviewId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(reviewService, never()).updateReview(any(), any());
        }
    }

    @Nested
    @DisplayName("Delete Review Tests")
    class DeleteReviewTests {

        @Test
        @DisplayName("Should delete review successfully")
        void shouldDeleteReviewSuccessfully() throws Exception {
            // Given
            doNothing().when(reviewService).deleteReview(reviewId);

            // When & Then
            mockMvc.perform(delete("/api/v1/reviews/{id}", reviewId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Review deleted successfully"))
                    .andExpect(jsonPath("$.deletedId").value(reviewId.toString()));

            verify(reviewService).deleteReview(reviewId);
        }
    }

    @Nested
    @DisplayName("Search Review Tests")
    class SearchReviewTests {

        @Test
        @DisplayName("Should search reviews by comment successfully")
        void shouldSearchReviewsByCommentSuccessfully() throws Exception {
            // Given
            String keyword = "great";
            List<ReviewResponse> reviews = Arrays.asList(reviewResponse);
            when(reviewService.searchReviewsByComment(keyword)).thenReturn(reviews);

            // When & Then
            mockMvc.perform(get("/api/v1/reviews/search/comment")
                            .param("keyword", keyword))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(reviewService).searchReviewsByComment(keyword);
        }

        @Test
        @DisplayName("Should search reviews by course title successfully")
        void shouldSearchReviewsByCourseTitleSuccessfully() throws Exception {
            // Given
            String title = "Java";
            List<ReviewResponse> reviews = Arrays.asList(reviewResponse);
            when(reviewService.searchReviewsByCourseTitle(title)).thenReturn(reviews);

            // When & Then
            mockMvc.perform(get("/api/v1/reviews/search/course")
                            .param("title", title))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(reviewService).searchReviewsByCourseTitle(title);
        }

        @Test
        @DisplayName("Should return empty list when no reviews match search")
        void shouldReturnEmptyListWhenNoReviewsMatchSearch() throws Exception {
            // Given
            String keyword = "nonexistent";
            when(reviewService.searchReviewsByComment(keyword)).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/v1/reviews/search/comment")
                            .param("keyword", keyword))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(reviewService).searchReviewsByComment(keyword);
        }
    }

    @Nested
    @DisplayName("Review by Instructor Tests")
    class ReviewByInstructorTests {

        @Test
        @DisplayName("Should get reviews by instructor ID successfully")
        void shouldGetReviewsByInstructorIdSuccessfully() throws Exception {
            // Given
            UUID instructorId = UUID.randomUUID();
            List<ReviewResponse> reviews = Arrays.asList(reviewResponse);
            when(reviewService.getReviewsByInstructorId(instructorId)).thenReturn(reviews);

            // When & Then
            mockMvc.perform(get("/api/v1/reviews/instructor/{instructorId}", instructorId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(reviewService).getReviewsByInstructorId(instructorId);
        }
    }

    @Nested
    @DisplayName("Review Statistics Tests")
    class ReviewStatisticsTests {

        @Test
        @DisplayName("Should get latest reviews successfully")
        void shouldGetLatestReviewsSuccessfully() throws Exception {
            // Given
            List<ReviewResponse> reviews = Arrays.asList(reviewResponse);
            when(reviewService.getLatestReviews()).thenReturn(reviews);

            // When & Then
            mockMvc.perform(get("/api/v1/reviews/latest"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(reviewService).getLatestReviews();
        }

        @Test
        @DisplayName("Should get review count by course successfully")
        void shouldGetReviewCountByCourseSuccessfully() throws Exception {
            // Given
            long count = 10L;
            when(reviewService.countReviewsByCourseId(courseId)).thenReturn(count);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{courseId}/reviews/count", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(10))
                    .andExpect(jsonPath("$.description").value("Total reviews for course"));

            verify(reviewService).countReviewsByCourseId(courseId);
        }

        @Test
        @DisplayName("Should check if review exists successfully")
        void shouldCheckIfReviewExistsSuccessfully() throws Exception {
            // Given
            when(reviewService.existsById(reviewId)).thenReturn(true);

            // When & Then
            mockMvc.perform(get("/api/v1/reviews/{id}/exists", reviewId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exists").value(true));

            verify(reviewService).existsById(reviewId);
        }
    }

    @Nested
    @DisplayName("Review by Student Tests")
    class ReviewByStudentTests {

        @Test
        @DisplayName("Should get reviews by student ID successfully")
        void shouldGetReviewsByStudentIdSuccessfully() throws Exception {
            // Given
            List<ReviewResponse> reviews = Arrays.asList(reviewResponse);
            when(reviewService.getReviewsByStudentIdOrderedByDate(studentId)).thenReturn(reviews);

            // When & Then
            mockMvc.perform(get("/api/v1/students/{studentId}/reviews", studentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(reviewService).getReviewsByStudentIdOrderedByDate(studentId);
        }

        @Test
        @DisplayName("Should get reviews by student ID ordered by date successfully")
        void shouldGetReviewsByStudentIdOrderedByDateSuccessfully() throws Exception {
            // Given
            List<ReviewResponse> reviews = Arrays.asList(reviewResponse);
            when(reviewService.getReviewsByStudentIdOrderedByDate(studentId)).thenReturn(reviews);

            // When & Then
            mockMvc.perform(get("/api/v1/students/{studentId}/reviews", studentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(reviewService).getReviewsByStudentIdOrderedByDate(studentId);
        }

        @Test
        @DisplayName("Should get review count by student successfully")
        void shouldGetReviewCountByStudentSuccessfully() throws Exception {
            // Given
            long count = 3L;
            when(reviewService.countReviewsByStudentId(studentId)).thenReturn(count);

            // When & Then
            mockMvc.perform(get("/api/v1/students/{studentId}/reviews/count", studentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(3))
                    .andExpect(jsonPath("$.description").value("Total reviews written by student"));

            verify(reviewService).countReviewsByStudentId(studentId);
        }
    }
}