package com.coursemanagement.controller;

import com.coursemanagement.dto.StudentRequest;
import com.coursemanagement.dto.StudentResponse;
import com.coursemanagement.service.StudentService;
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

@WebMvcTest(StudentController.class)
@DisplayName("StudentController Tests")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentRequest studentRequest;
    private StudentResponse studentResponse;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        studentRequest = new StudentRequest("John", "Doe", "john.doe@example.com");
        studentResponse = new StudentResponse(
                studentId,
                "John",
                "Doe", 
                "john.doe@example.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList()
        );
    }

    @Nested
    @DisplayName("Create Student Tests")
    class CreateStudentTests {

        @Test
        @DisplayName("Should create student successfully")
        void shouldCreateStudentSuccessfully() throws Exception {
            // Given
            when(studentService.createStudent(any())).thenReturn(studentResponse);

            // When & Then
            mockMvc.perform(post("/api/v1/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(studentRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(studentId.toString()))
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.lastName").value("Doe"))
                    .andExpect(jsonPath("$.email").value("john.doe@example.com"));

            verify(studentService).createStudent(any());
        }

        @Test
        @DisplayName("Should return 400 for invalid request data")
        void shouldReturn400ForInvalidRequestData() throws Exception {
            // Given
            StudentRequest invalidRequest = new StudentRequest("", "", "invalid-email");

            // When & Then
            mockMvc.perform(post("/api/v1/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(studentService, never()).createStudent(any());
        }
    }

    @Nested
    @DisplayName("Get Student Tests")
    class GetStudentTests {

        @Test
        @DisplayName("Should get student by ID successfully")
        void shouldGetStudentByIdSuccessfully() throws Exception {
            // Given
            when(studentService.getStudentById(studentId)).thenReturn(studentResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/students/{id}", studentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(studentId.toString()))
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.lastName").value("Doe"))
                    .andExpect(jsonPath("$.email").value("john.doe@example.com"));

            verify(studentService).getStudentById(studentId);
        }

        @Test
        @DisplayName("Should get all students successfully")
        void shouldGetAllStudentsSuccessfully() throws Exception {
            // Given
            List<StudentResponse> students = Arrays.asList(studentResponse);
            when(studentService.getAllStudents()).thenReturn(students);

            // When & Then
            mockMvc.perform(get("/api/v1/students"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(studentId.toString()))
                    .andExpect(jsonPath("$[0].firstName").value("John"));

            verify(studentService).getAllStudents();
        }

        @Test
        @DisplayName("Should get student by email successfully")
        void shouldGetStudentByEmailSuccessfully() throws Exception {
            // Given
            String email = "john.doe@example.com";
            when(studentService.getStudentByEmail(email)).thenReturn(studentResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/students/email/{email}", email))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(email));

            verify(studentService).getStudentByEmail(email);
        }

        @Test
        @DisplayName("Should get students with courses successfully")
        void shouldGetStudentsWithCoursesSuccessfully() throws Exception {
            // Given
            List<StudentResponse> students = Arrays.asList(studentResponse);
            when(studentService.getAllStudentsWithCourses()).thenReturn(students);

            // When & Then
            mockMvc.perform(get("/api/v1/students/with-courses"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(studentService).getAllStudentsWithCourses();
        }
    }

    @Nested
    @DisplayName("Update Student Tests")
    class UpdateStudentTests {

        @Test
        @DisplayName("Should update student successfully")
        void shouldUpdateStudentSuccessfully() throws Exception {
            // Given
            StudentRequest updateRequest = new StudentRequest("John Updated", "Doe Updated", "john.updated@example.com");
            StudentResponse updatedResponse = new StudentResponse(
                    studentId,
                    "John Updated",
                    "Doe Updated",
                    "john.updated@example.com",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    Collections.emptyList()
            );
            when(studentService.updateStudent(eq(studentId), any())).thenReturn(updatedResponse);

            // When & Then
            mockMvc.perform(put("/api/v1/students/{id}", studentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("John Updated"))
                    .andExpect(jsonPath("$.lastName").value("Doe Updated"))
                    .andExpect(jsonPath("$.email").value("john.updated@example.com"));

            verify(studentService).updateStudent(eq(studentId), any());
        }

        @Test
        @DisplayName("Should return 400 for invalid update data")
        void shouldReturn400ForInvalidUpdateData() throws Exception {
            // Given
            StudentRequest invalidRequest = new StudentRequest("", "", "invalid-email");

            // When & Then
            mockMvc.perform(put("/api/v1/students/{id}", studentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(studentService, never()).updateStudent(any(), any());
        }
    }

    @Nested
    @DisplayName("Delete Student Tests")
    class DeleteStudentTests {

        @Test
        @DisplayName("Should delete student successfully")
        void shouldDeleteStudentSuccessfully() throws Exception {
            // Given
            doNothing().when(studentService).deleteStudent(studentId);

            // When & Then
            mockMvc.perform(delete("/api/v1/students/{id}", studentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Student deleted successfully"))
                    .andExpect(jsonPath("$.resourceId").value(studentId.toString()));

            verify(studentService).deleteStudent(studentId);
        }
    }

    @Nested
    @DisplayName("Search Student Tests")
    class SearchStudentTests {

        @Test
        @DisplayName("Should search students by name successfully")
        void shouldSearchStudentsByNameSuccessfully() throws Exception {
            // Given
            String searchTerm = "John";
            List<StudentResponse> students = Arrays.asList(studentResponse);
            when(studentService.searchStudentsByName(searchTerm)).thenReturn(students);

            // When & Then
            mockMvc.perform(get("/api/v1/students/search")
                            .param("name", searchTerm))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].firstName").value("John"));

            verify(studentService).searchStudentsByName(searchTerm);
        }

        @Test
        @DisplayName("Should search students by email successfully")
        void shouldSearchStudentsByEmailSuccessfully() throws Exception {
            // Given
            String searchTerm = "john";
            List<StudentResponse> students = Arrays.asList(studentResponse);
            when(studentService.searchStudentsByEmail(searchTerm)).thenReturn(students);

            // When & Then
            mockMvc.perform(get("/api/v1/students/search")
                            .param("email", searchTerm))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(studentService).searchStudentsByEmail(searchTerm);
        }

        @Test
        @DisplayName("Should return empty list when no students match search")
        void shouldReturnEmptyListWhenNoStudentsMatchSearch() throws Exception {
            // Given
            String searchTerm = "nonexistent";
            when(studentService.searchStudentsByName(searchTerm)).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/v1/students/search")
                            .param("name", searchTerm))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(studentService).searchStudentsByName(searchTerm);
        }
    }

    @Nested
    @DisplayName("Student Courses Tests")
    class StudentCoursesTests {

        @Test
        @DisplayName("Should get student courses successfully")
        void shouldGetStudentCoursesSuccessfully() throws Exception {
            // Given
            List<StudentResponse.CourseInfo> courses = Arrays.asList(
                    new StudentResponse.CourseInfo(UUID.randomUUID(), "Java Basics", "John Instructor")
            );
            when(studentService.getStudentCourses(studentId)).thenReturn(courses);

            // When & Then
            mockMvc.perform(get("/api/v1/students/{id}/courses", studentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].title").value("Java Basics"));

            verify(studentService).getStudentCourses(studentId);
        }

        @Test
        @DisplayName("Should get students with no courses successfully")
        void shouldGetStudentsWithNoCoursesSuccessfully() throws Exception {
            // Given
            List<StudentResponse> students = Arrays.asList(studentResponse);
            when(studentService.getStudentsWithNoCourses()).thenReturn(students);

            // When & Then
            mockMvc.perform(get("/api/v1/students/no-courses"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(studentService).getStudentsWithNoCourses();
        }
    }

    @Nested
    @DisplayName("Student Statistics Tests")
    class StudentStatisticsTests {

        @Test
        @DisplayName("Should get students by instructor successfully")
        void shouldGetStudentsByInstructorSuccessfully() throws Exception {
            // Given
            UUID instructorId = UUID.randomUUID();
            List<StudentResponse> students = Arrays.asList(studentResponse);
            when(studentService.getStudentsByInstructor(instructorId)).thenReturn(students);

            // When & Then
            mockMvc.perform(get("/api/v1/students/by-instructor/{instructorId}", instructorId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(studentService).getStudentsByInstructor(instructorId);
        }

        @Test
        @DisplayName("Should check if student exists by email")
        void shouldCheckIfStudentExistsByEmail() throws Exception {
            // Given
            String email = "john.doe@example.com";
            when(studentService.existsByEmail(email)).thenReturn(true);

            // When & Then
            mockMvc.perform(get("/api/v1/students/exists/email/{email}", email))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exists").value(true));

            verify(studentService).existsByEmail(email);
        }
    }
}