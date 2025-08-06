package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Unenrollment responses.
 */
@Schema(description = "Response DTO for Unenrollment operations")
public class UnenrollmentResponse {

    @Schema(description = "Unenrollment confirmation message", example = "Student successfully unenrolled from course")
    private String message;

    @Schema(description = "Unenrollment timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime unenrollmentDate;

    @Schema(description = "Student information")
    private StudentInfo student;

    @Schema(description = "Course information")
    private CourseInfo course;

    // Nested class for student information
    @Schema(description = "Student information")
    public static class StudentInfo {
        @Schema(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID id;

        @Schema(description = "Student full name", example = "John Doe")
        private String fullName;

        @Schema(description = "Student email", example = "john.doe@example.com")
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

    // Nested class for course information
    @Schema(description = "Course information")
    public static class CourseInfo {
        @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID id;

        @Schema(description = "Course title", example = "Introduction to Spring Boot")
        private String title;

        @Schema(description = "Instructor name", example = "Jane Smith")
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
    public UnenrollmentResponse() {}

    // Constructor
    public UnenrollmentResponse(String message, LocalDateTime unenrollmentDate, StudentInfo student, CourseInfo course) {
        this.message = message;
        this.unenrollmentDate = unenrollmentDate;
        this.student = student;
        this.course = course;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getUnenrollmentDate() {
        return unenrollmentDate;
    }

    public void setUnenrollmentDate(LocalDateTime unenrollmentDate) {
        this.unenrollmentDate = unenrollmentDate;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    public CourseInfo getCourse() {
        return course;
    }

    public void setCourse(CourseInfo course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "UnenrollmentResponse{" +
                "message='" + message + '\'' +
                ", unenrollmentDate=" + unenrollmentDate +
                ", student=" + student +
                ", course=" + course +
                '}';
    }
}