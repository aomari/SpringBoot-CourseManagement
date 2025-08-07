package com.coursemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for InstructorDetails creation and update requests.
 */
@Schema(description = "Request DTO for InstructorDetails operations")
public class InstructorDetailsRequest {

    @NotBlank(message = "YouTube channel is required")
    @Size(max = 255, message = "YouTube channel must not exceed 255 characters")
    @Schema(description = "YouTube channel of the instructor", example = "https://youtube.com/@johndoe", required = true)
    private String youtubeChannel;

    @Size(max = 500, message = "hobby must not exceed 500 characters")
    @Schema(description = "Instructor's hobby", example = "Playing guitar and coding")
    private String hobby;

    // Default constructor
    public InstructorDetailsRequest() {}

    // Constructor
    public InstructorDetailsRequest(String youtubeChannel, String hobby) {
        this.youtubeChannel = youtubeChannel;
        this.hobby = hobby;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "InstructorDetailsRequest{" +
                "youtubeChannel='" + youtubeChannel + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}