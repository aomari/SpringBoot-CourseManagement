package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for deletion operations.
 */
@Schema(description = "Response for deletion operations")
public class DeletionResponse {

    @Schema(description = "ID of the deleted resource", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID deletedId;

    @Schema(description = "Type of resource that was deleted", example = "Course")
    private String resourceType;

    @Schema(description = "Success message", example = "Course deleted successfully")
    private String message;

    @Schema(description = "Timestamp when the deletion occurred", example = "2023-12-01T10:30:00")
    private LocalDateTime deletionTimestamp;

    @Schema(description = "Indicates if the deletion was successful", example = "true")
    private boolean success;

    // Default constructor
    public DeletionResponse() {
        this.deletionTimestamp = LocalDateTime.now();
        this.success = true;
    }

    // Constructor with parameters
    public DeletionResponse(UUID deletedId, String resourceType, String message) {
        this();
        this.deletedId = deletedId;
        this.resourceType = resourceType;
        this.message = message;
    }

    // Static factory methods for common use cases
    public static DeletionResponse success(UUID deletedId, String resourceType) {
        return new DeletionResponse(deletedId, resourceType, 
            resourceType + " deleted successfully");
    }

    public static DeletionResponse success(UUID deletedId, String resourceType, String customMessage) {
        return new DeletionResponse(deletedId, resourceType, customMessage);
    }

    // Getters and Setters
    public UUID getDeletedId() {
        return deletedId;
    }

    public void setDeletedId(UUID deletedId) {
        this.deletedId = deletedId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDeletionTimestamp() {
        return deletionTimestamp;
    }

    public void setDeletionTimestamp(LocalDateTime deletionTimestamp) {
        this.deletionTimestamp = deletionTimestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "DeletionResponse{" +
                "deletedId=" + deletedId +
                ", resourceType='" + resourceType + '\'' +
                ", message='" + message + '\'' +
                ", deletionTimestamp=" + deletionTimestamp +
                ", success=" + success +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeletionResponse that = (DeletionResponse) o;

        if (success != that.success) return false;
        if (deletedId != null ? !deletedId.equals(that.deletedId) : that.deletedId != null) return false;
        if (resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null)
            return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return deletionTimestamp != null ? deletionTimestamp.equals(that.deletionTimestamp) : that.deletionTimestamp == null;
    }

    @Override
    public int hashCode() {
        int result = deletedId != null ? deletedId.hashCode() : 0;
        result = 31 * result + (resourceType != null ? resourceType.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (deletionTimestamp != null ? deletionTimestamp.hashCode() : 0);
        result = 31 * result + (success ? 1 : 0);
        return result;
    }
}