package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Generic response DTO for count operations.
 */
@Schema(description = "Response for count operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CountResponse {

    @Schema(description = "Total count of items", example = "42")
    private long count;

    @Schema(description = "Type of resource being counted", example = "Course")
    private String resourceType;

    @Schema(description = "Description of what is being counted", example = "Total courses taught by instructor")
    private String description;

    // Constructor with count only
    public CountResponse(long count) {
        this.count = count;
    }

    // Constructor with count and resource type
    public CountResponse(long count, String resourceType) {
        this.count = count;
        this.resourceType = resourceType;
    }

    // Static factory methods for common use cases
    public static CountResponse of(long count, String resourceType) {
        return new CountResponse(count, resourceType);
    }

    public static CountResponse of(long count, String resourceType, String description) {
        return new CountResponse(count, resourceType, description);
    }
}