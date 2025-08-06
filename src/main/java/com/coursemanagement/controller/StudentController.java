package com.coursemanagement.controller;

import com.coursemanagement.dto.DeletionResponse;
import com.coursemanagement.dto.StudentRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Student operations.
 */
@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "Student API", description = "API for managing students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Create new student", description = "Creates a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Student with email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody StudentRequest request) {
        
        StudentResponse response = studentService.createStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get student by ID", description = "Retrieves a student by their unique identifier with enrolled courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @Parameter(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        StudentResponse response = studentService.getStudentByIdWithCourses(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all students", description = "Retrieves a list of all students")
    @ApiResponse(responseCode = "200", description = "List of students retrieved successfully")
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> response = studentService.getAllStudents();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update student", description = "Updates an existing student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @Parameter(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody StudentRequest request) {
        
        StudentResponse response = studentService.updateStudent(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete student", description = "Deletes a student and removes all enrollments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully",
                    content = @Content(schema = @Schema(implementation = DeletionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeletionResponse> deleteStudent(
            @Parameter(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        studentService.deleteStudent(id);
        DeletionResponse response = DeletionResponse.success(id, "Student");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get student courses", description = "Retrieves all courses that a student is enrolled in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student courses retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<StudentResponse.CourseInfo>> getStudentCourses(
            @Parameter(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        List<StudentResponse.CourseInfo> response = studentService.getStudentCourses(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search students by name", description = "Searches students by first name, last name, or full name")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search/name")
    public ResponseEntity<List<StudentResponse>> searchStudentsByName(
            @Parameter(description = "Name to search for", example = "John")
            @RequestParam String name) {
        
        List<StudentResponse> response = studentService.searchStudentsByName(name);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search students by email", description = "Searches students by email containing the keyword")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search/email")
    public ResponseEntity<List<StudentResponse>> searchStudentsByEmail(
            @Parameter(description = "Email keyword to search for", example = "john")
            @RequestParam String email) {
        
        List<StudentResponse> response = studentService.searchStudentsByEmail(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get student by email", description = "Retrieves a student by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<StudentResponse> getStudentByEmail(
            @Parameter(description = "Student email", example = "john.doe@example.com")
            @PathVariable String email) {
        
        StudentResponse response = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get students with no courses", description = "Retrieves students who are not enrolled in any courses")
    @ApiResponse(responseCode = "200", description = "Students with no courses retrieved successfully")
    @GetMapping("/no-courses")
    public ResponseEntity<List<StudentResponse>> getStudentsWithNoCourses() {
        List<StudentResponse> response = studentService.getStudentsWithNoCourses();
        return ResponseEntity.ok(response);
    }
}