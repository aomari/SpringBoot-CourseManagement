package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Instructor responses.
 */
@Schema(description = "Response DTO for Instructor")
public class InstructorResponse {

    @Schema(description = "Unique identifier of the instructor", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "First name of the instructor", example = "John")
    private String firstName;

    @Schema(description = "Last name of the instructor", example = "Doe")
    private String lastName;

    @Schema(description = "Full name of the instructor", example = "John Doe")
    private String fullName;

    @Schema(description = "Email address of the instructor", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Creation timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Instructor details if available")
    private InstructorDetailsResponse instructorDetails;

    // Default constructor
    public InstructorResponse() {}

    // Constructor
    public InstructorResponse(UUID id, String firstName, String lastName, String email,
                            LocalDateTime createdAt, LocalDateTime updatedAt,
                            InstructorDetailsResponse instructorDetails) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.instructorDetails = instructorDetails;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFullName();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateFullName();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public InstructorDetailsResponse getInstructorDetails() {
        return instructorDetails;
    }

    public void setInstructorDetails(InstructorDetailsResponse instructorDetails) {
        this.instructorDetails = instructorDetails;
    }

    // Helper method to update full name
    private void updateFullName() {
        if (firstName != null && lastName != null) {
            this.fullName = firstName + " " + lastName;
        }
    }

    @Override
    public String toString() {
        return "InstructorResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", instructorDetails=" + instructorDetails +
                '}';
    }
}