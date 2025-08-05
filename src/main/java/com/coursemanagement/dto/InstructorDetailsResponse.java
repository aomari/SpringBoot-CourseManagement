package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for InstructorDetails responses.
 */
@Schema(description = "Response DTO for InstructorDetails")
public class InstructorDetailsResponse {

    @Schema(description = "Unique identifier of the instructor details", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "YouTube channel of the instructor", example = "https://youtube.com/@johndoe")
    private String youtubeChannel;

    @Schema(description = "Instructor's hobby", example = "Playing guitar and coding")
    private String hobby;

    @Schema(description = "Creation timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime updatedAt;

    // Default constructor
    public InstructorDetailsResponse() {}

    // Constructor
    public InstructorDetailsResponse(UUID id, String youtubeChannel, String hobby, 
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.youtubeChannel = youtubeChannel;
        this.hobby = hobby;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getYoutubeChannel() {
        return youtubeChannel;
    }

    public void setYoutubeChannel(String youtubeChannel) {
        this.youtubeChannel = youtubeChannel;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "InstructorDetailsResponse{" +
                "id=" + id +
                ", youtubeChannel='" + youtubeChannel + '\'' +
                ", hobby='" + hobby + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}