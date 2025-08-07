package com.coursemanagement.controller;

import com.coursemanagement.dto.DeletionResponse;
import com.coursemanagement.dto.InstructorDetailsRequest;
import com.coursemanagement.dto.InstructorDetailsResponse;
import com.coursemanagement.exception.ErrorResponse;
import com.coursemanagement.service.InstructorDetailsService;
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
 * REST Controller for InstructorDetails operations.
 */
@RestController
@RequestMapping("/api/v1/instructor-details")
@Tag(name = "Instructor Details", description = "API for managing instructor details")
public class InstructorDetailsController {

    private final InstructorDetailsService instructorDetailsService;

    @Autowired
    public InstructorDetailsController(InstructorDetailsService instructorDetailsService) {
        this.instructorDetailsService = instructorDetailsService;
    }

    @Operation(summary = "Create new instructor details", description = "Creates new instructor details with YouTube channel and hobby information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instructor details created successfully",
                    content = @Content(schema = @Schema(implementation = InstructorDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<InstructorDetailsResponse> createInstructorDetails(
            @Valid @RequestBody InstructorDetailsRequest request) {
        
        InstructorDetailsResponse response = instructorDetailsService.createInstructorDetails(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get instructor details by ID", description = "Retrieves instructor details by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor details found",
                    content = @Content(schema = @Schema(implementation = InstructorDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instructor details not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<InstructorDetailsResponse> getInstructorDetailsById(
            @Parameter(description = "Instructor details ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        InstructorDetailsResponse response = instructorDetailsService.getInstructorDetailsById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all instructor details", description = "Retrieves a list of all instructor details")
    @ApiResponse(responseCode = "200", description = "List of instructor details retrieved successfully")
    @GetMapping
    public ResponseEntity<List<InstructorDetailsResponse>> getAllInstructorDetails() {
        List<InstructorDetailsResponse> response = instructorDetailsService.getAllInstructorDetails();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update instructor details", description = "Updates existing instructor details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor details updated successfully",
                    content = @Content(schema = @Schema(implementation = InstructorDetailsResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instructor details not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<InstructorDetailsResponse> updateInstructorDetails(
            @Parameter(description = "Instructor details ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody InstructorDetailsRequest request) {
        
        InstructorDetailsResponse response = instructorDetailsService.updateInstructorDetails(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete instructor details", description = "Deletes instructor details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instructor details deleted successfully",
                    content = @Content(schema = @Schema(implementation = DeletionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instructor details not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeletionResponse> deleteInstructorDetails(
            @Parameter(description = "Instructor details ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        instructorDetailsService.deleteInstructorDetails(id);
        DeletionResponse response = DeletionResponse.success(id, "Instructor details");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search by YouTube channel", description = "Searches instructor details by YouTube channel")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search/youtube")
    public ResponseEntity<List<InstructorDetailsResponse>> searchByYoutubeChannel(
            @Parameter(description = "YouTube channel search term", example = "johndoe")
            @RequestParam String channel) {
        
        List<InstructorDetailsResponse> response = instructorDetailsService.searchByYoutubeChannel(channel);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search by hobby", description = "Searches instructor details by hobby")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search/hobby")
    public ResponseEntity<List<InstructorDetailsResponse>> searchByHobby(
            @Parameter(description = "hobby search term", example = "guitar")
            @RequestParam String hobby) {
        
        List<InstructorDetailsResponse> response = instructorDetailsService.searchByHobby(hobby);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get orphaned instructor details", description = "Retrieves instructor details not linked to any instructor")
    @ApiResponse(responseCode = "200", description = "Orphaned instructor details retrieved successfully")
    @GetMapping("/orphaned")
    public ResponseEntity<List<InstructorDetailsResponse>> getOrphanedInstructorDetails() {
        List<InstructorDetailsResponse> response = instructorDetailsService.getOrphanedInstructorDetails();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check if instructor details exist", description = "Checks if instructor details exist by ID")
    @ApiResponse(responseCode = "200", description = "Existence check completed")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "Instructor details ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        
        boolean exists = instructorDetailsService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}