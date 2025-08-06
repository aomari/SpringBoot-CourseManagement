package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for Review count responses.
 */
@Schema(description = "Response DTO for Review count")
public class ReviewCountResponse {

    @Schema(description = "Total number of reviews", example = "42")
    private long count;

    // Default constructor
    public ReviewCountResponse() {}

    // Constructor with count
    public ReviewCountResponse(long count) {
        this.count = count;
    }

    // Getter
    public long getCount() {
        return count;
    }

    // Setter
    public void setCount(long count) {
        this.count = count;
    }
}