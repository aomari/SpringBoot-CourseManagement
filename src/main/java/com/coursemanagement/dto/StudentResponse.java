package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for Student responses.
 */
@Schema(description = "Response DTO for Student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
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

    // Custom setters to maintain fullName consistency
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFullName();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateFullName();
    }

    // Helper method to update full name
    private void updateFullName() {
        if (firstName != null && lastName != null) {
            this.fullName = firstName + " " + lastName;
        }
    }

    // Nested class for course information
    @Schema(description = "Course information for enrolled courses")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class CourseInfo {
        @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID id;

        @Schema(description = "Course title", example = "Introduction to Spring Boot")
        private String title;

        @Schema(description = "Instructor full name", example = "Jane Smith")
        private String instructorName;
    }
}