package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for InstructorDetails creation and update requests.
 */
@Schema(description = "Request DTO for InstructorDetails operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class InstructorDetailsRequest {

    @NotBlank(message = "YouTube channel is required")
    @Size(max = 255, message = "YouTube channel must not exceed 255 characters")
    @Schema(description = "YouTube channel of the instructor", example = "https://youtube.com/@johndoe", required = true)
    private String youtubeChannel;

    @Size(max = 500, message = "hobby must not exceed 500 characters")
    @Schema(description = "Instructor's hobby", example = "Playing guitar and coding")
    private String hobby;
}