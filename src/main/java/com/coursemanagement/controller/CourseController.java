package com.coursemanagement.controller;

import com.coursemanagement.dto.CountResponse;
import com.coursemanagement.dto.CourseRequest;
import com.coursemanagement.dto.CourseResponse;
import com.coursemanagement.dto.DeletionResponse;
import com.coursemanagement.exception.ErrorResponse;
import com.coursemanagement.service.CourseService;
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
 * REST Controller for Course operations.
 */
@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Course API", description = "API for managing courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Create new course", description = "Creates a new course and assigns it to an existing instructor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instructor not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Course with title already exists for this instructor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseRequest request) {
        
        CourseResponse response = courseService.createCourse(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get course by ID", description = "Retrieves a course by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        CourseResponse response = courseService.getCourseByIdWithReviews(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all courses", description = "Retrieves a list of all courses")
    @ApiResponse(responseCode = "200", description = "List of courses retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update course", description = "Updates an existing course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully",
                    content = @Content(schema = @Schema(implementation = CourseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Course or instructor not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Course title already exists for this instructor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody CourseRequest request) {
        
        CourseResponse response = courseService.updateCourse(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete course", description = "Deletes a course by ID (and all its reviews via database cascade)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully",
                    content = @Content(schema = @Schema(implementation = DeletionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeletionResponse> deleteCourse(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        courseService.deleteCourse(id);
        DeletionResponse response = DeletionResponse.success(id, "Course");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get courses by instructor", description = "Retrieves all courses taught by a specific instructor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Instructor not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByInstructor(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID instructorId) {
        
        List<CourseResponse> response = courseService.getCoursesByInstructorId(instructorId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search courses by title", description = "Searches courses by title (case-insensitive partial match)")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search/title")
    public ResponseEntity<List<CourseResponse>> searchCoursesByTitle(
            @Parameter(description = "Title search term", example = "Spring")
            @RequestParam String title) {
        
        List<CourseResponse> response = courseService.searchCoursesByTitle(title);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search courses by instructor name", description = "Searches courses by instructor name (first name, last name, or full name)")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search/instructor")
    public ResponseEntity<List<CourseResponse>> searchCoursesByInstructorName(
            @Parameter(description = "Instructor name search term", example = "John")
            @RequestParam String name) {
        
        List<CourseResponse> response = courseService.searchCoursesByInstructorName(name);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all courses with reviews", description = "Retrieves all courses including their reviews")
    @ApiResponse(responseCode = "200", description = "Courses with reviews retrieved successfully")
    @GetMapping("/with-reviews")
    public ResponseEntity<List<CourseResponse>> getAllCoursesWithReviews() {
        List<CourseResponse> response = courseService.getAllCoursesWithReviews();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get courses by instructor with reviews", description = "Retrieves all courses by instructor including their reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses with reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Instructor not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/instructor/{instructorId}/with-reviews")
    public ResponseEntity<List<CourseResponse>> getCoursesByInstructorWithReviews(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID instructorId) {
        
        List<CourseResponse> response = courseService.getCoursesByInstructorIdWithReviews(instructorId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check if course exists by ID", description = "Checks if a course exists by its ID")
    @ApiResponse(responseCode = "200", description = "Existence check completed")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        boolean exists = courseService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Count courses by instructor", description = "Counts the number of courses taught by an instructor")
    @ApiResponse(responseCode = "200", description = "Course count retrieved successfully",
            content = @Content(schema = @Schema(implementation = CountResponse.class)))
    @GetMapping("/instructor/{instructorId}/count")
    public ResponseEntity<CountResponse> countCoursesByInstructor(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID instructorId) {
        
        long count = courseService.countCoursesByInstructorId(instructorId);
        CountResponse response = CountResponse.of(count, "Course", "Total courses taught by instructor");
        return ResponseEntity.ok(response);
    }
}