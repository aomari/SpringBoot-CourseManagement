package com.coursemanagement.controller;

import com.coursemanagement.dto.EnrollmentRequest;
import com.coursemanagement.dto.StudentResponse;
import com.coursemanagement.exception.ErrorResponse;
import com.coursemanagement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Student Enrollment operations.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Enrollment API", description = "API for managing student course enrollments")
public class EnrollmentController {

    private final StudentService studentService;

    @Autowired
    public EnrollmentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Enroll student in course", description = "Enrolls a student in a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student enrolled successfully"),
            @ApiResponse(responseCode = "404", description = "Student or course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Student already enrolled in course",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/students/{id}/enroll")
    public ResponseEntity<Void> enrollStudentInCourse(
            @Parameter(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody EnrollmentRequest request) {
        
        studentService.enrollStudentInCourse(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unenroll student from course", description = "Removes a student from a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student unenrolled successfully"),
            @ApiResponse(responseCode = "404", description = "Student, course, or enrollment not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/students/{id}/unenroll")
    public ResponseEntity<Void> unenrollStudentFromCourse(
            @Parameter(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody EnrollmentRequest request) {
        
        studentService.unenrollStudentFromCourse(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get students enrolled in course", description = "Retrieves all students enrolled in a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/courses/{id}/students")
    public ResponseEntity<List<StudentResponse>> getStudentsEnrolledInCourse(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        List<StudentResponse> response = studentService.getStudentsEnrolledInCourse(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check enrollment status", description = "Checks if a student is enrolled in a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/students/{studentId}/enrollment/courses/{courseId}")
    public ResponseEntity<Boolean> checkEnrollmentStatus(
            @Parameter(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID studentId,
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID courseId) {
        
        boolean isEnrolled = studentService.isStudentEnrolledInCourse(studentId, courseId);
        return ResponseEntity.ok(isEnrolled);
    }

    @Operation(summary = "Count students in course", description = "Returns the number of students enrolled in a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student count retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/courses/{id}/students/count")
    public ResponseEntity<Long> countStudentsInCourse(
            @Parameter(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        long count = studentService.countStudentsInCourse(id);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get students by instructor", description = "Retrieves students enrolled in courses taught by a specific instructor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/instructors/{id}/students")
    public ResponseEntity<List<StudentResponse>> getStudentsByInstructor(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        List<StudentResponse> response = studentService.getStudentsByInstructor(id);
        return ResponseEntity.ok(response);
    }
}