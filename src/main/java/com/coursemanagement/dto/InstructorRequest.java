package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for Instructor creation and update requests.
 */
@Schema(description = "Request DTO for Instructor operations")
public class InstructorRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    @Schema(description = "First name of the instructor", example = "John", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    @Schema(description = "Last name of the instructor", example = "Doe", required = true)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Schema(description = "Email address of the instructor", example = "john.doe@example.com", required = true)
    private String email;

    @Valid
    @Schema(description = "Optional instructor details")
    private InstructorDetailsRequest instructorDetails;

    // Default constructor
    public InstructorRequest() {}

    // Constructor without instructor details
    public InstructorRequest(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Constructor with instructor details
    public InstructorRequest(String firstName, String lastName, String email, 
                           InstructorDetailsRequest instructorDetails) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.instructorDetails = instructorDetails;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public InstructorDetailsRequest getInstructorDetails() {
        return instructorDetails;
    }

    public void setInstructorDetails(InstructorDetailsRequest instructorDetails) {
        this.instructorDetails = instructorDetails;
    }

    @Override
    public String toString() {
        return "InstructorRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", instructorDetails=" + instructorDetails +
                '}';
    }
}