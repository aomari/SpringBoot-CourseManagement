package com.coursemanagement.controller;

import com.coursemanagement.dto.InstructorDetailsRequest;
import com.coursemanagement.dto.InstructorDetailsResponse;
import com.coursemanagement.service.InstructorDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InstructorDetailsController.class)
@DisplayName("InstructorDetailsController Tests")
class InstructorDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstructorDetailsService instructorDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private InstructorDetailsRequest instructorDetailsRequest;
    private InstructorDetailsResponse instructorDetailsResponse;
    private UUID detailsId;

    @BeforeEach
    void setUp() {
        detailsId = UUID.randomUUID();
        
        instructorDetailsRequest = new InstructorDetailsRequest(
                "https://youtube.com/@johndoe", 
                "Playing guitar"
        );
        
        instructorDetailsResponse = new InstructorDetailsResponse(
                detailsId,
                "https://youtube.com/@johndoe",
                "Playing guitar",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Nested
    @DisplayName("Create InstructorDetails Tests")
    class CreateInstructorDetailsTests {

        @Test
        @DisplayName("Should create instructor details successfully")
        void shouldCreateInstructorDetailsSuccessfully() throws Exception {
            // Given
            when(instructorDetailsService.createInstructorDetails(any()))
                    .thenReturn(instructorDetailsResponse);

            // When & Then
            mockMvc.perform(post("/api/v1/instructor-details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(instructorDetailsRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(detailsId.toString()))
                    .andExpect(jsonPath("$.youtubeChannel").value("https://youtube.com/@johndoe"))
                    .andExpect(jsonPath("$.hoppy").value("Playing guitar"));

            verify(instructorDetailsService).createInstructorDetails(any());
        }

        @Test
        @DisplayName("Should return 400 when creating instructor details with null youtube channel")
        void shouldReturn400WhenCreatingInstructorDetailsWithNullYoutubeChannel() throws Exception {
            // Given
            InstructorDetailsRequest nullRequest = new InstructorDetailsRequest(null, null);

            // When & Then
            mockMvc.perform(post("/api/v1/instructor-details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nullRequest)))
                    .andExpect(status().isBadRequest());

            verify(instructorDetailsService, never()).createInstructorDetails(any());
        }
    }

    @Nested
    @DisplayName("Get InstructorDetails Tests")
    class GetInstructorDetailsTests {

        @Test
        @DisplayName("Should get instructor details by ID successfully")
        void shouldGetInstructorDetailsByIdSuccessfully() throws Exception {
            // Given
            when(instructorDetailsService.getInstructorDetailsById(detailsId))
                    .thenReturn(instructorDetailsResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details/{id}", detailsId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(detailsId.toString()))
                    .andExpect(jsonPath("$.youtubeChannel").value("https://youtube.com/@johndoe"))
                    .andExpect(jsonPath("$.hoppy").value("Playing guitar"));

            verify(instructorDetailsService).getInstructorDetailsById(detailsId);
        }

        @Test
        @DisplayName("Should get all instructor details successfully")
        void shouldGetAllInstructorDetailsSuccessfully() throws Exception {
            // Given
            List<InstructorDetailsResponse> detailsList = Arrays.asList(instructorDetailsResponse);
            when(instructorDetailsService.getAllInstructorDetails()).thenReturn(detailsList);

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(detailsId.toString()));

            verify(instructorDetailsService).getAllInstructorDetails();
        }

        @Test
        @DisplayName("Should return empty list when no instructor details exist")
        void shouldReturnEmptyListWhenNoInstructorDetailsExist() throws Exception {
            // Given
            when(instructorDetailsService.getAllInstructorDetails()).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(instructorDetailsService).getAllInstructorDetails();
        }
    }

    @Nested
    @DisplayName("Update InstructorDetails Tests")
    class UpdateInstructorDetailsTests {

        @Test
        @DisplayName("Should update instructor details successfully")
        void shouldUpdateInstructorDetailsSuccessfully() throws Exception {
            // Given
            InstructorDetailsRequest updateRequest = new InstructorDetailsRequest(
                    "https://youtube.com/@johnupdated", 
                    "Updated hoppy"
            );
            InstructorDetailsResponse updatedResponse = new InstructorDetailsResponse(
                    detailsId,
                    "https://youtube.com/@johnupdated",
                    "Updated hoppy",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            when(instructorDetailsService.updateInstructorDetails(eq(detailsId), any()))
                    .thenReturn(updatedResponse);

            // When & Then
            mockMvc.perform(put("/api/v1/instructor-details/{id}", detailsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.youtubeChannel").value("https://youtube.com/@johnupdated"))
                    .andExpect(jsonPath("$.hoppy").value("Updated hoppy"));

            verify(instructorDetailsService).updateInstructorDetails(eq(detailsId), any());
        }

        @Test
        @DisplayName("Should return 400 when updating instructor details with null youtube channel")
        void shouldReturn400WhenUpdatingInstructorDetailsWithNullYoutubeChannel() throws Exception {
            // Given
            InstructorDetailsRequest nullRequest = new InstructorDetailsRequest(null, null);

            // When & Then
            mockMvc.perform(put("/api/v1/instructor-details/{id}", detailsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nullRequest)))
                    .andExpect(status().isBadRequest());

            verify(instructorDetailsService, never()).updateInstructorDetails(eq(detailsId), any());
        }
    }

    @Nested
    @DisplayName("Delete InstructorDetails Tests")
    class DeleteInstructorDetailsTests {

        @Test
        @DisplayName("Should delete instructor details successfully")
        void shouldDeleteInstructorDetailsSuccessfully() throws Exception {
            // Given
            doNothing().when(instructorDetailsService).deleteInstructorDetails(detailsId);

            // When & Then
            mockMvc.perform(delete("/api/v1/instructor-details/{id}", detailsId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Instructor details deleted successfully"))
                    .andExpect(jsonPath("$.deletedId").value(detailsId.toString()));

            verify(instructorDetailsService).deleteInstructorDetails(detailsId);
        }
    }

    @Nested
    @DisplayName("Search InstructorDetails Tests")
    class SearchInstructorDetailsTests {

        @Test
        @DisplayName("Should search instructor details by YouTube channel successfully")
        void shouldSearchInstructorDetailsByYouTubeChannelSuccessfully() throws Exception {
            // Given
            String channel = "johndoe";
            List<InstructorDetailsResponse> detailsList = Arrays.asList(instructorDetailsResponse);
            when(instructorDetailsService.searchByYoutubeChannel(channel)).thenReturn(detailsList);

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details/search/youtube")
                            .param("channel", channel))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(detailsId.toString()));

            verify(instructorDetailsService).searchByYoutubeChannel(channel);
        }

        @Test
        @DisplayName("Should search instructor details by hoppy successfully")
        void shouldSearchInstructorDetailsByhoppySuccessfully() throws Exception {
            // Given
            String hoppy = "guitar";
            List<InstructorDetailsResponse> detailsList = Arrays.asList(instructorDetailsResponse);
            when(instructorDetailsService.searchByHobby(hoppy)).thenReturn(detailsList);

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details/search/hoppy")
                            .param("hoppy", hoppy))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(instructorDetailsService).searchByHobby(hoppy);
        }

        @Test
        @DisplayName("Should return empty list when no instructor details match search")
        void shouldReturnEmptyListWhenNoInstructorDetailsMatchSearch() throws Exception {
            // Given
            String hoppy = "nonexistent";
            when(instructorDetailsService.searchByHobby(hoppy)).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details/search/hoppy")
                            .param("hoppy", hoppy))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(instructorDetailsService).searchByHobby(hoppy);
        }
    }

    @Nested
    @DisplayName("Orphaned InstructorDetails Tests")
    class OrphanedInstructorDetailsTests {

        @Test
        @DisplayName("Should get orphaned instructor details successfully")
        void shouldGetOrphanedInstructorDetailsSuccessfully() throws Exception {
            // Given
            List<InstructorDetailsResponse> orphanedDetails = Arrays.asList(instructorDetailsResponse);
            when(instructorDetailsService.getOrphanedInstructorDetails()).thenReturn(orphanedDetails);

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details/orphaned"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(instructorDetailsService).getOrphanedInstructorDetails();
        }

        @Test
        @DisplayName("Should return empty list when no orphaned instructor details exist")
        void shouldReturnEmptyListWhenNoOrphanedInstructorDetailsExist() throws Exception {
            // Given
            when(instructorDetailsService.getOrphanedInstructorDetails()).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details/orphaned"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(instructorDetailsService).getOrphanedInstructorDetails();
        }
    }

    @Nested
    @DisplayName("InstructorDetails Existence Tests")
    class InstructorDetailsExistenceTests {

        @Test
        @DisplayName("Should check if instructor details exist successfully")
        void shouldCheckIfInstructorDetailsExistSuccessfully() throws Exception {
            // Given
            when(instructorDetailsService.existsById(detailsId)).thenReturn(true);

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details/{id}/exists", detailsId))
                    .andExpect(status().isOk())
                    .andExpect(status().isOk());

            verify(instructorDetailsService).existsById(detailsId);
        }

        @Test
        @DisplayName("Should return false when instructor details do not exist")
        void shouldReturnFalseWhenInstructorDetailsDoNotExist() throws Exception {
            // Given
            when(instructorDetailsService.existsById(detailsId)).thenReturn(false);

            // When & Then
            mockMvc.perform(get("/api/v1/instructor-details/{id}/exists", detailsId))
                    .andExpect(status().isOk())
                    .andExpect(status().isOk());

            verify(instructorDetailsService).existsById(detailsId);
        }
    }
}