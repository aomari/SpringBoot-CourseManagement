package com.coursemanagement.controller;

import com.coursemanagement.dto.InstructorDetailsRequest;
import com.coursemanagement.dto.InstructorDetailsResponse;
import com.coursemanagement.dto.InstructorRequest;
import com.coursemanagement.dto.InstructorResponse;
import com.coursemanagement.exception.ResourceAlreadyExistsException;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.service.InstructorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import com.coursemanagement.exception.GlobalExceptionHandler;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for InstructorController.
 * Tests all REST endpoints with mocked service layer.
 */
@WebMvcTest(controllers = InstructorController.class)
@Import({GlobalExceptionHandler.class})
@DisplayName("Instructor Controller Tests")
class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test data
    private UUID instructorId;
    private UUID instructorDetailsId;
    private InstructorResponse instructorResponse;
    private InstructorDetailsResponse instructorDetailsResponse;
    private InstructorRequest instructorRequest;
    private InstructorDetailsRequest instructorDetailsRequest;

    @BeforeEach
    void setUp() {
        instructorId = UUID.randomUUID();
        instructorDetailsId = UUID.randomUUID();
        
        // Setup instructor details response
        instructorDetailsResponse = new InstructorDetailsResponse(
            instructorDetailsId,
            "https://youtube.com/@johndoe",
            "Playing guitar",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        // Setup instructor response
        instructorResponse = new InstructorResponse(
            instructorId,
            "John",
            "Doe",
            "john.doe@example.com",
            LocalDateTime.now(),
            LocalDateTime.now(),
            instructorDetailsResponse
        );
        
        // Setup DTOs for requests
        instructorDetailsRequest = new InstructorDetailsRequest("https://youtube.com/@johndoe", "Playing guitar");
        instructorRequest = new InstructorRequest("John", "Doe", "john.doe@example.com", instructorDetailsRequest);
    }

    @Nested
    @DisplayName("Create Instructor Endpoint Tests")
    class CreateInstructorEndpointTests {

        @Test
        @DisplayName("POST /api/v1/instructors - Should create instructor successfully")
        void shouldCreateInstructorSuccessfully() throws Exception {
            // Given
            when(instructorService.createInstructor(any(InstructorRequest.class))).thenReturn(instructorResponse);

            // When & Then
            mockMvc.perform(post("/api/v1/instructors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(instructorRequest)))
                    .andDo(print())
                                    .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(instructorId.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                                    .andExpect(jsonPath("$.instructorDetails").exists())
                                    .andExpect(jsonPath("$.instructorDetails.youtubeChannel").value("https://youtube.com/@johndoe"))
                .andExpect(jsonPath("$.instructorDetails.hoppy").value("Playing guitar"));

            verify(instructorService).createInstructor(any(InstructorRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/instructors - Should create instructor without details successfully")
        void shouldCreateInstructorWithoutDetailsSuccessfully() throws Exception {
            // Given
            InstructorRequest requestWithoutDetails = new InstructorRequest("Jane", "Smith", "jane.smith@example.com");
            InstructorResponse responseWithoutDetails = new InstructorResponse(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "jane.smith@example.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
            );
            
            when(instructorService.createInstructor(any(InstructorRequest.class))).thenReturn(responseWithoutDetails);

            // When & Then
            mockMvc.perform(post("/api/v1/instructors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestWithoutDetails)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstName").exists())
                    .andExpect(jsonPath("$.firstName").value("Jane"))
                    .andExpect(jsonPath("$.lastName").value("Smith"))
                    .andExpect(jsonPath("$.email").value("jane.smith@example.com"))
                    .andExpect(jsonPath("$.instructorDetails").doesNotExist());

            verify(instructorService).createInstructor(any(InstructorRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/instructors - Should return 409 when instructor already exists")
        void shouldReturn409WhenInstructorAlreadyExists() throws Exception {
            // Given
            when(instructorService.createInstructor(any(InstructorRequest.class)))
                .thenThrow(new ResourceAlreadyExistsException("Instructor", "email", "john.doe@example.com"));

            // When & Then
            mockMvc.perform(post("/api/v1/instructors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(instructorRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.message").value(containsString("Instructor already exists with email: john.doe@example.com")));

            verify(instructorService).createInstructor(any(InstructorRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/instructors - Should return 400 for invalid request data")
        void shouldReturn400ForInvalidRequestData() throws Exception {
            // Given - Invalid request with missing required fields
            InstructorRequest invalidRequest = new InstructorRequest("", "", "invalid-email");

            // When & Then
            mockMvc.perform(post("/api/v1/instructors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value(containsString("Validation failed")));

            verify(instructorService, never()).createInstructor(any(InstructorRequest.class));
        }
    }

    @Nested
    @DisplayName("Get Instructor Endpoint Tests")
    class GetInstructorEndpointTests {

        @Test
        @DisplayName("GET /api/v1/instructors/{id} - Should get instructor by ID successfully")
        void shouldGetInstructorByIdSuccessfully() throws Exception {
            // Given
            when(instructorService.getInstructorById(instructorId)).thenReturn(instructorResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/instructors/{id}", instructorId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").exists())
                    .andExpect(jsonPath("$.id").value(instructorId.toString()))
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.lastName").value("Doe"))
                    .andExpect(jsonPath("$.email").value("john.doe@example.com"));

            verify(instructorService).getInstructorById(instructorId);
        }

        @Test
        @DisplayName("GET /api/v1/instructors/{id} - Should return 404 when instructor not found")
        void shouldReturn404WhenInstructorNotFound() throws Exception {
            // Given
            when(instructorService.getInstructorById(instructorId))
                .thenThrow(new ResourceNotFoundException("Instructor", "id", instructorId));

            // When & Then
            mockMvc.perform(get("/api/v1/instructors/{id}", instructorId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").value(containsString("Instructor not found")));

            verify(instructorService).getInstructorById(instructorId);
        }

        @Test
        @DisplayName("GET /api/v1/instructors - Should get all instructors successfully")
        void shouldGetAllInstructorsSuccessfully() throws Exception {
            // Given
            InstructorResponse instructor2 = new InstructorResponse(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "jane.smith@example.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
            );
            List<InstructorResponse> instructors = Arrays.asList(instructorResponse, instructor2);
            
            when(instructorService.getAllInstructors()).thenReturn(instructors);

            // When & Then
            mockMvc.perform(get("/api/v1/instructors"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].firstName").exists())
                                    .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

            verify(instructorService).getAllInstructors();
        }

        @Test
        @DisplayName("GET /api/v1/instructors/email/{email} - Should get instructor by email successfully")
        void shouldGetInstructorByEmailSuccessfully() throws Exception {
            // Given
            when(instructorService.getInstructorByEmail("john.doe@example.com")).thenReturn(instructorResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/instructors/email/{email}", "john.doe@example.com"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").exists())
                    .andExpect(jsonPath("$.email").value("john.doe@example.com"));

            verify(instructorService).getInstructorByEmail("john.doe@example.com");
        }
    }

    @Nested
    @DisplayName("Update Instructor Endpoint Tests")
    class UpdateInstructorEndpointTests {

        @Test
        @DisplayName("PUT /api/v1/instructors/{id} - Should update instructor successfully")
        void shouldUpdateInstructorSuccessfully() throws Exception {
            // Given
            InstructorRequest updateRequest = new InstructorRequest("John Updated", "Doe Updated", "john.updated@example.com");
            InstructorResponse updatedResponse = new InstructorResponse(
                instructorId,
                "John Updated",
                "Doe Updated",
                "john.updated@example.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
            );
            
            when(instructorService.updateInstructor(eq(instructorId), any(InstructorRequest.class)))
                .thenReturn(updatedResponse);

            // When & Then
            mockMvc.perform(put("/api/v1/instructors/{id}", instructorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").exists())
                    .andExpect(jsonPath("$.firstName").value("John Updated"))
                    .andExpect(jsonPath("$.lastName").value("Doe Updated"))
                    .andExpect(jsonPath("$.email").value("john.updated@example.com"));

            verify(instructorService).updateInstructor(eq(instructorId), any(InstructorRequest.class));
        }

        @Test
        @DisplayName("PUT /api/v1/instructors/{id} - Should return 404 when updating non-existent instructor")
        void shouldReturn404WhenUpdatingNonExistentInstructor() throws Exception {
            // Given
            when(instructorService.updateInstructor(eq(instructorId), any(InstructorRequest.class)))
                .thenThrow(new ResourceNotFoundException("Instructor", "id", instructorId));

            // When & Then
            mockMvc.perform(put("/api/v1/instructors/{id}", instructorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(instructorRequest)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));

            verify(instructorService).updateInstructor(eq(instructorId), any(InstructorRequest.class));
        }

        @Test
        @DisplayName("PUT /api/v1/instructors/{id} - Should return 409 when updating to existing email")
        void shouldReturn409WhenUpdatingToExistingEmail() throws Exception {
            // Given
            when(instructorService.updateInstructor(eq(instructorId), any(InstructorRequest.class)))
                .thenThrow(new ResourceAlreadyExistsException("Instructor", "email", "existing@example.com"));

            // When & Then
            mockMvc.perform(put("/api/v1/instructors/{id}", instructorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(instructorRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409));

            verify(instructorService).updateInstructor(eq(instructorId), any(InstructorRequest.class));
        }
    }

    @Nested
    @DisplayName("Delete Instructor Endpoint Tests")
    class DeleteInstructorEndpointTests {

        @Test
        @DisplayName("DELETE /api/v1/instructors/{id} - Should delete instructor successfully")
        void shouldDeleteInstructorSuccessfully() throws Exception {
            // Given
            doNothing().when(instructorService).deleteInstructor(instructorId);

            // When & Then
            mockMvc.perform(delete("/api/v1/instructors/{id}", instructorId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Instructor deleted successfully"));

            verify(instructorService).deleteInstructor(instructorId);
        }

        @Test
        @DisplayName("DELETE /api/v1/instructors/{id} - Should return 404 when deleting non-existent instructor")
        void shouldReturn404WhenDeletingNonExistentInstructor() throws Exception {
            // Given
            doThrow(new ResourceNotFoundException("Instructor", "id", instructorId))
                .when(instructorService).deleteInstructor(instructorId);

            // When & Then
            mockMvc.perform(delete("/api/v1/instructors/{id}", instructorId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));

            verify(instructorService).deleteInstructor(instructorId);
        }
    }

    @Nested
    @DisplayName("Search and Filter Endpoint Tests")
    class SearchAndFilterEndpointTests {

        @Test
        @DisplayName("GET /api/v1/instructors/search - Should search instructors by name successfully")
        void shouldSearchInstructorsByNameSuccessfully() throws Exception {
            // Given
            List<InstructorResponse> searchResults = Arrays.asList(instructorResponse);
            when(instructorService.searchInstructorsByName("John")).thenReturn(searchResults);

            // When & Then
            mockMvc.perform(get("/api/v1/instructors/search")
                    .param("name", "John"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].firstName").exists())
                                    .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"));

            verify(instructorService).searchInstructorsByName("John");
        }

        @Test
        @DisplayName("GET /api/v1/instructors/with-details - Should get instructors with details successfully")
        void shouldGetInstructorsWithDetailsSuccessfully() throws Exception {
            // Given
            List<InstructorResponse> instructorsWithDetails = Arrays.asList(instructorResponse);
            when(instructorService.getInstructorsWithDetails()).thenReturn(instructorsWithDetails);

            // When & Then
            mockMvc.perform(get("/api/v1/instructors/with-details"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].firstName").exists())
                                    .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].instructorDetails").exists());

            verify(instructorService).getInstructorsWithDetails();
        }

        @Test
        @DisplayName("GET /api/v1/instructors/without-details - Should get instructors without details successfully")
        void shouldGetInstructorsWithoutDetailsSuccessfully() throws Exception {
            // Given
            InstructorResponse instructorWithoutDetails = new InstructorResponse(
                UUID.randomUUID(),
                "Jane",
                "Smith",
                "jane.smith@example.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
            );
            List<InstructorResponse> instructorsWithoutDetails = Arrays.asList(instructorWithoutDetails);
            when(instructorService.getInstructorsWithoutDetails()).thenReturn(instructorsWithoutDetails);

            // When & Then
            mockMvc.perform(get("/api/v1/instructors/without-details"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].firstName").exists())
                                    .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].instructorDetails").doesNotExist());

            verify(instructorService).getInstructorsWithoutDetails();
        }
    }

    @Nested
    @DisplayName("Instructor Details Management Endpoint Tests")
    class InstructorDetailsManagementEndpointTests {

        @Test
        @DisplayName("PUT /api/v1/instructors/{instructorId}/details/{detailsId} - Should add instructor details successfully")
        void shouldAddInstructorDetailsSuccessfully() throws Exception {
            // Given
            when(instructorService.addInstructorDetails(instructorId, instructorDetailsId))
                .thenReturn(instructorResponse);

            // When & Then
            mockMvc.perform(put("/api/v1/instructors/{instructorId}/details/{detailsId}", instructorId, instructorDetailsId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").exists())
                    .andExpect(jsonPath("$.instructorDetails").exists());

            verify(instructorService).addInstructorDetails(instructorId, instructorDetailsId);
        }

        @Test
        @DisplayName("DELETE /api/v1/instructors/{instructorId}/details - Should remove instructor details successfully")
        void shouldRemoveInstructorDetailsSuccessfully() throws Exception {
            // Given
            InstructorResponse responseWithoutDetails = new InstructorResponse(
                instructorId,
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
            );
            when(instructorService.removeInstructorDetails(instructorId)).thenReturn(responseWithoutDetails);

            // When & Then
            mockMvc.perform(delete("/api/v1/instructors/{instructorId}/details", instructorId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").exists())
                    .andExpect(jsonPath("$.instructorDetails").doesNotExist());

            verify(instructorService).removeInstructorDetails(instructorId);
        }

        @Test
        @DisplayName("PUT /api/v1/instructors/{instructorId}/details/{detailsId} - Should return 404 when instructor not found")
        void shouldReturn404WhenInstructorNotFoundForAddingDetails() throws Exception {
            // Given
            when(instructorService.addInstructorDetails(instructorId, instructorDetailsId))
                .thenThrow(new ResourceNotFoundException("Instructor", "id", instructorId));

            // When & Then
            mockMvc.perform(put("/api/v1/instructors/{instructorId}/details/{detailsId}", instructorId, instructorDetailsId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));

            verify(instructorService).addInstructorDetails(instructorId, instructorDetailsId);
        }

        @Test
        @DisplayName("PUT /api/v1/instructors/{instructorId}/details/{detailsId} - Should return 404 when details not found")
        void shouldReturn404WhenDetailsNotFoundForAdding() throws Exception {
            // Given
            when(instructorService.addInstructorDetails(instructorId, instructorDetailsId))
                .thenThrow(new ResourceNotFoundException("InstructorDetails", "id", instructorDetailsId));

            // When & Then
            mockMvc.perform(put("/api/v1/instructors/{instructorId}/details/{detailsId}", instructorId, instructorDetailsId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));

            verify(instructorService).addInstructorDetails(instructorId, instructorDetailsId);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("POST /api/v1/instructors - Should validate required fields")
        void shouldValidateRequiredFields() throws Exception {
            // Given - Request with null/empty required fields
            InstructorRequest invalidRequest = new InstructorRequest();

            // When & Then
            mockMvc.perform(post("/api/v1/instructors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value(containsString("Validation failed")));

            verify(instructorService, never()).createInstructor(any(InstructorRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/instructors - Should validate email format")
        void shouldValidateEmailFormat() throws Exception {
            // Given - Request with invalid email format
            InstructorRequest invalidRequest = new InstructorRequest("John", "Doe", "invalid-email-format");

            // When & Then
            mockMvc.perform(post("/api/v1/instructors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value(containsString("Validation failed")));

            verify(instructorService, never()).createInstructor(any(InstructorRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/instructors - Should validate field lengths")
        void shouldValidateFieldLengths() throws Exception {
            // Given - Request with fields exceeding maximum length
            String longString = "a".repeat(101); // Exceeds 100 character limit
            InstructorRequest invalidRequest = new InstructorRequest(longString, longString, "valid@example.com");

            // When & Then
            mockMvc.perform(post("/api/v1/instructors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value(containsString("Validation failed")));

            verify(instructorService, never()).createInstructor(any(InstructorRequest.class));
        }
    }
}