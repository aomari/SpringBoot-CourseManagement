package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Review responses.
 */
@Schema(description = "Response DTO for Review")
public class ReviewResponse {

    @Schema(description = "Unique identifier of the review", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Review comment", example = "This course was excellent! Learned a lot about Spring Boot.")
    private String comment;

    @Schema(description = "Creation timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Course information")
    private CourseInfo course;

    // Nested class for course information
    @Schema(description = "Course information")
    public static class CourseInfo {
        @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID id;

        @Schema(description = "Course title", example = "Introduction to Spring Boot")
        private String title;

        @Schema(description = "Instructor name", example = "John Doe")
        private String instructorName;

        public CourseInfo() {}

        public CourseInfo(UUID id, String title, String instructorName) {
            this.id = id;
            this.title = title;
            this.instructorName = instructorName;
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

        public String getInstructorName() {
            return instructorName;
        }

        public void setInstructorName(String instructorName) {
            this.instructorName = instructorName;
        }
    }

    // Default constructor
    public ReviewResponse() {}

    // Constructor
    public ReviewResponse(UUID id, String comment, LocalDateTime createdAt, LocalDateTime updatedAt,
                         CourseInfo course) {
        this.id = id;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.course = course;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public CourseInfo getCourse() {
        return course;
    }

    public void setCourse(CourseInfo course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "ReviewResponse{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", course=" + course +
                '}';
    }
}