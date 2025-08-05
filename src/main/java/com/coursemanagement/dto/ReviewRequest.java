package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for Review creation and update requests.
 */
@Schema(description = "Request DTO for Review operations")
public class ReviewRequest {

    @NotBlank(message = "Comment is required")
    @Schema(description = "Review comment", example = "This course was excellent! Learned a lot about Spring Boot.", required = true)
    private String comment;

    @NotNull(message = "Course ID is required")
    @Schema(description = "ID of the course being reviewed", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID courseId;

    // Default constructor
    public ReviewRequest() {}

    // Constructor with all fields
    public ReviewRequest(String comment, UUID courseId) {
        this.comment = comment;
        this.courseId = courseId;
    }

    // Getters and Setters
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "ReviewRequest{" +
                "comment='" + comment + '\'' +
                ", courseId=" + courseId +
                '}';
    }
}