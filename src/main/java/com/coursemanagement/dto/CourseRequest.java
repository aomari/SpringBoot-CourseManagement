package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * DTO for Course creation and update requests.
 */
@Schema(description = "Request DTO for Course operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CourseRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @Schema(description = "Title of the course", example = "Introduction to Spring Boot", required = true)
    private String title;

    @NotNull(message = "Instructor ID is required")
    @Schema(description = "ID of the instructor who teaches this course", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID instructorId;
}