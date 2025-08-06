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

    @NotNull(message = "Student ID is required")
    @Schema(description = "ID of the student writing the review", example = "987fcdeb-51a2-43d1-9b12-345678901234", required = true)
    private UUID studentId;

    // Default constructor
    public ReviewRequest() {}

    // Constructor with all fields
    public ReviewRequest(String comment, UUID studentId) {
        this.comment = comment;
        this.studentId = studentId;
    }

    // Getters and Setters
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "ReviewRequest{" +
                "comment='" + comment + '\'' +
                ", studentId=" + studentId +
                '}';
    }
}