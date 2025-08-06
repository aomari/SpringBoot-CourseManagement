package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for Review exists responses.
 */
@Schema(description = "Response DTO for Review existence check")
public class ReviewExistsResponse {

    @Schema(description = "Whether the review exists", example = "true")
    private boolean exists;

    // Default constructor
    public ReviewExistsResponse() {}

    // Constructor with exists flag
    public ReviewExistsResponse(boolean exists) {
        this.exists = exists;
    }

    // Getter
    public boolean isExists() {
        return exists;
    }

    // Setter
    public void setExists(boolean exists) {
        this.exists = exists;
    }
}