package com.coursemanagement.controller;

import com.coursemanagement.dto.InstructorRequest;
import com.coursemanagement.dto.InstructorResponse;
import com.coursemanagement.exception.ErrorResponse;
import com.coursemanagement.service.InstructorService;
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
 * REST Controller for Instructor operations.
 */
@RestController
@RequestMapping("/api/v1/instructors")
@Tag(name = "Instructors", description = "API for managing instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @Operation(summary = "Create new instructor", description = "Creates a new instructor with optional instructor details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instructor created successfully",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Instructor with email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<InstructorResponse> createInstructor(
            @Valid @RequestBody InstructorRequest request) {
        
        InstructorResponse response = instructorService.createInstructor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get instructor by ID", description = "Retrieves an instructor by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor found",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instructor not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<InstructorResponse> getInstructorById(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        InstructorResponse response = instructorService.getInstructorById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all instructors", description = "Retrieves a list of all instructors")
    @ApiResponse(responseCode = "200", description = "List of instructors retrieved successfully")
    @GetMapping
    public ResponseEntity<List<InstructorResponse>> getAllInstructors() {
        List<InstructorResponse> response = instructorService.getAllInstructors();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update instructor", description = "Updates an existing instructor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor updated successfully",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instructor not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponse> updateInstructor(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody InstructorRequest request) {
        
        InstructorResponse response = instructorService.updateInstructor(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete instructor", description = "Deletes an instructor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Instructor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Instructor not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        instructorService.deleteInstructor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get instructor by email", description = "Retrieves an instructor by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor found",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instructor not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<InstructorResponse> getInstructorByEmail(
            @Parameter(description = "Instructor email", example = "john.doe@example.com")
            @PathVariable String email) {
        
        InstructorResponse response = instructorService.getInstructorByEmail(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search instructors by name", description = "Searches instructors by first name, last name, or full name")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<List<InstructorResponse>> searchInstructorsByName(
            @Parameter(description = "Name search term", example = "John")
            @RequestParam String name) {
        
        List<InstructorResponse> response = instructorService.searchInstructorsByName(name);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get instructors with details", description = "Retrieves all instructors that have instructor details")
    @ApiResponse(responseCode = "200", description = "Instructors with details retrieved successfully")
    @GetMapping("/with-details")
    public ResponseEntity<List<InstructorResponse>> getInstructorsWithDetails() {
        List<InstructorResponse> response = instructorService.getInstructorsWithDetails();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get instructors without details", description = "Retrieves all instructors that don't have instructor details")
    @ApiResponse(responseCode = "200", description = "Instructors without details retrieved successfully")
    @GetMapping("/without-details")
    public ResponseEntity<List<InstructorResponse>> getInstructorsWithoutDetails() {
        List<InstructorResponse> response = instructorService.getInstructorsWithoutDetails();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add instructor details", description = "Links existing instructor details to an instructor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor details added successfully",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instructor or instructor details not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{instructorId}/details/{detailsId}")
    public ResponseEntity<InstructorResponse> addInstructorDetails(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID instructorId,
            @Parameter(description = "Instructor details ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID detailsId) {
        
        InstructorResponse response = instructorService.addInstructorDetails(instructorId, detailsId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove instructor details", description = "Removes instructor details from an instructor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor details removed successfully",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instructor not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{instructorId}/details")
    public ResponseEntity<InstructorResponse> removeInstructorDetails(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID instructorId) {
        
        InstructorResponse response = instructorService.removeInstructorDetails(instructorId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check if instructor exists by ID", description = "Checks if an instructor exists by their ID")
    @ApiResponse(responseCode = "200", description = "Existence check completed")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        boolean exists = instructorService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Check if instructor exists by email", description = "Checks if an instructor exists by their email")
    @ApiResponse(responseCode = "200", description = "Existence check completed")
    @GetMapping("/email/{email}/exists")
    public ResponseEntity<Boolean> existsByEmail(
            @Parameter(description = "Instructor email", example = "john.doe@example.com")
            @PathVariable String email) {
        
        boolean exists = instructorService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}