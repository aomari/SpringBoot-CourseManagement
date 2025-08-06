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

    @Schema(description = "Student information")
    private StudentInfo student;

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

    // Nested class for student information
    @Schema(description = "Student information")
    public static class StudentInfo {
        @Schema(description = "Student ID", example = "987fcdeb-51a2-43d1-9b12-345678901234")
        private UUID id;

        @Schema(description = "Student full name", example = "Jane Smith")
        private String fullName;

        @Schema(description = "Student email", example = "jane.smith@example.com")
        private String email;

        public StudentInfo() {}

        public StudentInfo(UUID id, String fullName, String email) {
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
    public ReviewResponse() {}

    // Constructor
    public ReviewResponse(UUID id, String comment, LocalDateTime createdAt, LocalDateTime updatedAt,
                         CourseInfo course, StudentInfo student) {
        this.id = id;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.course = course;
        this.student = student;
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

    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "ReviewResponse{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", course=" + course +
                ", student=" + student +
                '}';
    }
}