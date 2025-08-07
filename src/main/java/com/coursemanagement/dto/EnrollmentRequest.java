package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * DTO for student enrollment/unenrollment requests.
 */
@Schema(description = "Request DTO for Student enrollment operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EnrollmentRequest {

    @NotNull(message = "Course ID is required")
    @Schema(description = "ID of the course to enroll/unenroll from", 
            example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID courseId;
}