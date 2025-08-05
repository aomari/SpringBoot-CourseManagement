package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for Student responses.
 */
@Schema(description = "Response DTO for Student")
public class StudentResponse {

    @Schema(description = "Unique identifier of the student", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "First name of the student", example = "John")
    private String firstName;

    @Schema(description = "Last name of the student", example = "Doe")
    private String lastName;

    @Schema(description = "Full name of the student", example = "John Doe")
    private String fullName;

    @Schema(description = "Email address of the student", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Creation timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "List of courses the student is enrolled in")
    private List<CourseInfo> courses;

    // Nested class for course information
    @Schema(description = "Course information for enrolled courses")
    public static class CourseInfo {
        @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID id;

        @Schema(description = "Course title", example = "Introduction to Spring Boot")
        private String title;

        @Schema(description = "Instructor full name", example = "Jane Smith")
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
    public StudentResponse() {}

    // Constructor
    public StudentResponse(UUID id, String firstName, String lastName, String email,
                          LocalDateTime createdAt, LocalDateTime updatedAt, List<CourseInfo> courses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.courses = courses;
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

    public List<CourseInfo> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseInfo> courses) {
        this.courses = courses;
    }

    // Helper method to update full name
    private void updateFullName() {
        if (firstName != null && lastName != null) {
            this.fullName = firstName + " " + lastName;
        }
    }

    @Override
    public String toString() {
        return "StudentResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", coursesCount=" + (courses != null ? courses.size() : 0) +
                '}';
    }
}