package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Generic response DTO for count operations.
 */
@Schema(description = "Response for count operations")
public class CountResponse {

    @Schema(description = "Total count of items", example = "42")
    private long count;

    @Schema(description = "Type of resource being counted", example = "Course")
    private String resourceType;

    @Schema(description = "Description of what is being counted", example = "Total courses taught by instructor")
    private String description;

    // Default constructor
    public CountResponse() {}

    // Constructor with count only
    public CountResponse(long count) {
        this.count = count;
    }

    // Constructor with count and resource type
    public CountResponse(long count, String resourceType) {
        this.count = count;
        this.resourceType = resourceType;
    }

    // Constructor with all parameters
    public CountResponse(long count, String resourceType, String description) {
        this.count = count;
        this.resourceType = resourceType;
        this.description = description;
    }

    // Static factory methods for common use cases
    public static CountResponse of(long count, String resourceType) {
        return new CountResponse(count, resourceType);
    }

    public static CountResponse of(long count, String resourceType, String description) {
        return new CountResponse(count, resourceType, description);
    }

    // Getters and Setters
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CountResponse{" +
                "count=" + count +
                ", resourceType='" + resourceType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountResponse that = (CountResponse) o;

        if (count != that.count) return false;
        if (resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null)
            return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (count ^ (count >>> 32));
        result = 31 * result + (resourceType != null ? resourceType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}