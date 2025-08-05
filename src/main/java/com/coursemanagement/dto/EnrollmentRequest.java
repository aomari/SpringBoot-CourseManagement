package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for student enrollment/unenrollment requests.
 */
@Schema(description = "Request DTO for Student enrollment operations")
public class EnrollmentRequest {

    @NotNull(message = "Course ID is required")
    @Schema(description = "ID of the course to enroll/unenroll from", 
            example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID courseId;

    // Default constructor
    public EnrollmentRequest() {}

    // Constructor with courseId
    public EnrollmentRequest(UUID courseId) {
        this.courseId = courseId;
    }

    // Getters and Setters
    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "EnrollmentRequest{" +
                "courseId=" + courseId +
                '}';
    }
}