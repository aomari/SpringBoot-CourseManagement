package com.coursemanagement.controller;

import com.coursemanagement.dto.CountResponse;
import com.coursemanagement.dto.DeletionResponse;
import com.coursemanagement.dto.ReviewExistsResponse;
import com.coursemanagement.dto.ReviewRequest;
import com.coursemanagement.dto.ReviewResponse;
import com.coursemanagement.exception.ErrorResponse;
import com.coursemanagement.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Review operations.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Review API", description = "API for managing course reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Nested endpoints under courses
    @Operation(summary = "Add review to course", description = "Creates a new review for a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully",
                    content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/courses/{courseId}/reviews")
    public ResponseEntity<ReviewResponse> addReviewToCourse(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID courseId,
            @Valid @RequestBody ReviewRequest request) {
        
        ReviewResponse response = reviewService.createReview(courseId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get reviews for course", description = "Retrieves all reviews for a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/courses/{courseId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsForCourse(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID courseId) {
        
        List<ReviewResponse> response = reviewService.getReviewsByCourseIdOrderedByDate(courseId);
        return ResponseEntity.ok(response);
    }

    // Direct review endpoints
    @Operation(summary = "Get review by ID", description = "Retrieves a review by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found",
                    content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(
            @Parameter(description = "Review ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        ReviewResponse response = reviewService.getReviewById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update review", description = "Updates an existing review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully",
                    content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review or course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @Parameter(description = "Review ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody ReviewRequest request) {
        
        ReviewResponse response = reviewService.updateReview(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete review", description = "Deletes a review by ID (course remains unaffected)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted successfully",
                    content = @Content(schema = @Schema(implementation = DeletionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<DeletionResponse> deleteReview(
            @Parameter(description = "Review ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        reviewService.deleteReview(id);
        DeletionResponse response = DeletionResponse.success(id, "Review");
        return ResponseEntity.ok(response);
    }

    // Additional query endpoints
    @Operation(summary = "Get all reviews", description = "Retrieves a list of all reviews")
    @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully")
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        List<ReviewResponse> response = reviewService.getAllReviews();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search reviews by comment", description = "Searches reviews by comment content (case-insensitive partial match)")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/reviews/search/comment")
    public ResponseEntity<List<ReviewResponse>> searchReviewsByComment(
            @Parameter(description = "Comment search term", example = "excellent")
            @RequestParam String keyword) {
        
        List<ReviewResponse> response = reviewService.searchReviewsByComment(keyword);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search reviews by course title", description = "Searches reviews by course title (case-insensitive partial match)")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/reviews/search/course")
    public ResponseEntity<List<ReviewResponse>> searchReviewsByCourseTitle(
            @Parameter(description = "Course title search term", example = "Spring")
            @RequestParam String title) {
        
        List<ReviewResponse> response = reviewService.searchReviewsByCourseTitle(title);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get reviews by instructor", description = "Retrieves all reviews for courses taught by a specific instructor")
    @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    @GetMapping("/reviews/instructor/{instructorId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByInstructor(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID instructorId) {
        
        List<ReviewResponse> response = reviewService.getReviewsByInstructorId(instructorId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get latest reviews", description = "Retrieves the most recent reviews (ordered by creation date, newest first)")
    @ApiResponse(responseCode = "200", description = "Latest reviews retrieved successfully")
    @GetMapping("/reviews/latest")
    public ResponseEntity<List<ReviewResponse>> getLatestReviews() {
        List<ReviewResponse> response = reviewService.getLatestReviews();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check if review exists by ID", description = "Checks if a review exists by its ID")
    @ApiResponse(responseCode = "200", description = "Existence check completed",
            content = @Content(schema = @Schema(implementation = ReviewExistsResponse.class)))
    @GetMapping("/reviews/{id}/exists")
    public ResponseEntity<ReviewExistsResponse> existsById(
            @Parameter(description = "Review ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        boolean exists = reviewService.existsById(id);
        return ResponseEntity.ok(new ReviewExistsResponse(exists));
    }

    @Operation(summary = "Count reviews for course", description = "Counts the number of reviews for a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review count retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CountResponse.class))),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/courses/{courseId}/reviews/count")
    public ResponseEntity<CountResponse> countReviewsForCourse(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID courseId) {
        
        long count = reviewService.countReviewsByCourseId(courseId);
        CountResponse response = CountResponse.of(count, "Review", "Total reviews for course");
        return ResponseEntity.ok(response);
    }

    // Student-related endpoints
    @Operation(summary = "Get reviews by student", description = "Retrieves all reviews written by a specific student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/students/{studentId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByStudent(
            @Parameter(description = "Student ID", example = "987fcdeb-51a2-43d1-9b12-345678901234")
            @PathVariable UUID studentId) {
        
        List<ReviewResponse> response = reviewService.getReviewsByStudentIdOrderedByDate(studentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get reviews by course and student", description = "Retrieves all reviews for a specific course written by a specific student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Course or student not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/courses/{courseId}/students/{studentId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByCourseAndStudent(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID courseId,
            @Parameter(description = "Student ID", example = "987fcdeb-51a2-43d1-9b12-345678901234")
            @PathVariable UUID studentId) {
        
        List<ReviewResponse> response = reviewService.getReviewsByCourseIdAndStudentId(courseId, studentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Count reviews by student", description = "Counts the number of reviews written by a specific student")
    @ApiResponse(responseCode = "200", description = "Review count retrieved successfully",
            content = @Content(schema = @Schema(implementation = CountResponse.class)))
    @GetMapping("/students/{studentId}/reviews/count")
    public ResponseEntity<CountResponse> countReviewsByStudent(
            @Parameter(description = "Student ID", example = "987fcdeb-51a2-43d1-9b12-345678901234")
            @PathVariable UUID studentId) {
        
        long count = reviewService.countReviewsByStudentId(studentId);
        CountResponse response = CountResponse.of(count, "Review", "Total reviews written by student");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search reviews by student email", description = "Searches reviews by student email address")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/reviews/search/student/email")
    public ResponseEntity<List<ReviewResponse>> searchReviewsByStudentEmail(
            @Parameter(description = "Student email", example = "jane.smith@example.com")
            @RequestParam String email) {
        
        List<ReviewResponse> response = reviewService.searchReviewsByStudentEmail(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search reviews by student name", description = "Searches reviews by student name (partial match)")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/reviews/search/student/name")
    public ResponseEntity<List<ReviewResponse>> searchReviewsByStudentName(
            @Parameter(description = "Student name search term", example = "Jane")
            @RequestParam String name) {
        
        List<ReviewResponse> response = reviewService.searchReviewsByStudentName(name);
        return ResponseEntity.ok(response);
    }
}