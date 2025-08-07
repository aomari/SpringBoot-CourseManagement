package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * DTO for Review creation and update requests.
 */
@Schema(description = "Request DTO for Review operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ReviewRequest {

    @NotBlank(message = "Comment is required")
    @Schema(description = "Review comment", example = "This course was excellent! Learned a lot about Spring Boot.", required = true)
    private String comment;

    @NotNull(message = "Student ID is required")
    @Schema(description = "ID of the student writing the review", example = "987fcdeb-51a2-43d1-9b12-345678901234", required = true)
    private UUID studentId;
}