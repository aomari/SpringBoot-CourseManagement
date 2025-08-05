package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for Course responses.
 */
@Schema(description = "Response DTO for Course")
public class CourseResponse {

    @Schema(description = "Unique identifier of the course", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Title of the course", example = "Introduction to Spring Boot")
    private String title;

    @Schema(description = "Creation timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Instructor information")
    private InstructorInfo instructor;

    @Schema(description = "List of reviews for this course")
    private List<ReviewResponse> reviews;

    // Nested class for instructor information
    @Schema(description = "Instructor information")
    public static class InstructorInfo {
        @Schema(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID id;

        @Schema(description = "Instructor full name", example = "John Doe")
        private String fullName;

        @Schema(description = "Instructor email", example = "john.doe@example.com")
        private String email;

        public InstructorInfo() {}

        public InstructorInfo(UUID id, String fullName, String email) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
        }

        // Getters and Setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
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
    }

    // Default constructor
    public CourseResponse() {}

    // Constructor
    public CourseResponse(UUID id, String title, LocalDateTime createdAt, LocalDateTime updatedAt,
                         InstructorInfo instructor, List<ReviewResponse> reviews) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.instructor = instructor;
        this.reviews = reviews;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public InstructorInfo getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorInfo instructor) {
        this.instructor = instructor;
    }

    public List<ReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponse> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "CourseResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", instructor=" + instructor +
                ", reviewsCount=" + (reviews != null ? reviews.size() : 0) +
                '}';
    }
}