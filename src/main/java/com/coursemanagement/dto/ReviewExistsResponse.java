package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO for Review exists responses.
 */
@Schema(description = "Response DTO for Review existence check")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ReviewExistsResponse {

    @Schema(description = "Whether the review exists", example = "true")
    private boolean exists;
}