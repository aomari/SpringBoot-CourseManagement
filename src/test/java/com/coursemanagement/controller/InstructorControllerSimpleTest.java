package com.coursemanagement.controller;

import com.coursemanagement.dto.InstructorRequest;
import com.coursemanagement.dto.InstructorResponse;
import com.coursemanagement.service.InstructorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Simple integration tests for InstructorController using SpringBootTest.
 * This approach loads the full application context to ensure endpoints work.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Instructor Controller Simple Tests")
class InstructorControllerSimpleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create instructor successfully")
    void shouldCreateInstructorSuccessfully() throws Exception {
        // Given
        UUID instructorId = UUID.randomUUID();
        InstructorRequest request = new InstructorRequest("John", "Doe", "john.doe@example.com", null);
        InstructorResponse response = new InstructorResponse(
                instructorId, "John", "Doe", "john.doe@example.com", 
                LocalDateTime.now(), LocalDateTime.now(), null
        );

        when(instructorService.createInstructor(any(InstructorRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(instructorId.toString()))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.lastName").value("Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    @DisplayName("Should get all instructors successfully")
    void shouldGetAllInstructorsSuccessfully() throws Exception {
        // Given
        UUID instructorId = UUID.randomUUID();
        InstructorResponse response = new InstructorResponse(
                instructorId, "John", "Doe", "john.doe@example.com", 
                LocalDateTime.now(), LocalDateTime.now(), null
        );
        List<InstructorResponse> instructors = Arrays.asList(response);

        when(instructorService.getAllInstructors()).thenReturn(instructors);

        // When & Then
        mockMvc.perform(get("/api/instructors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(instructorId.toString()))
                .andExpect(jsonPath("$.data[0].firstName").value("John"))
                .andExpect(jsonPath("$.data[0].lastName").value("Doe"));
    }

    @Test
    @DisplayName("Should get instructor by ID successfully")
    void shouldGetInstructorByIdSuccessfully() throws Exception {
        // Given
        UUID instructorId = UUID.randomUUID();
        InstructorResponse response = new InstructorResponse(
                instructorId, "John", "Doe", "john.doe@example.com", 
                LocalDateTime.now(), LocalDateTime.now(), null
        );

        when(instructorService.getInstructorById(instructorId)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/instructors/{id}", instructorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(instructorId.toString()))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.lastName").value("Doe"));
    }

    @Test
    @DisplayName("Should update instructor successfully")
    void shouldUpdateInstructorSuccessfully() throws Exception {
        // Given
        UUID instructorId = UUID.randomUUID();
        InstructorRequest request = new InstructorRequest("John Updated", "Doe Updated", "john.updated@example.com", null);
        InstructorResponse response = new InstructorResponse(
                instructorId, "John Updated", "Doe Updated", "john.updated@example.com", 
                LocalDateTime.now(), LocalDateTime.now(), null
        );

        when(instructorService.updateInstructor(any(UUID.class), any(InstructorRequest.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/instructors/{id}", instructorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("John Updated"))
                .andExpect(jsonPath("$.data.lastName").value("Doe Updated"));
    }

    @Test
    @DisplayName("Should delete instructor successfully")
    void shouldDeleteInstructorSuccessfully() throws Exception {
        // Given
        UUID instructorId = UUID.randomUUID();
        // Mock service to not throw any exception (void method)
        // when(instructorService.deleteInstructor(instructorId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/instructors/{id}", instructorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Instructor deleted successfully"));
    }
}