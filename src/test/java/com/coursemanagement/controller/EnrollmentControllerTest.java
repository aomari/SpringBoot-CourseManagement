package com.coursemanagement.controller;

import com.coursemanagement.dto.EnrollmentRequest;
import com.coursemanagement.dto.EnrollmentResponse;
import com.coursemanagement.dto.StudentResponse;
import com.coursemanagement.dto.UnenrollmentResponse;
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

@WebMvcTest(EnrollmentController.class)
@DisplayName("EnrollmentController Tests")
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private EnrollmentRequest enrollmentRequest;
    private EnrollmentResponse enrollmentResponse;
    private UnenrollmentResponse unenrollmentResponse;
    private UUID studentId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        
        enrollmentRequest = new EnrollmentRequest(courseId);
        
        EnrollmentResponse.StudentInfo studentInfo = new EnrollmentResponse.StudentInfo(
                studentId, "John Doe", "john.doe@example.com"
        );
        
        EnrollmentResponse.CourseInfo courseInfo = new EnrollmentResponse.CourseInfo(
                courseId, "Java Basics", "Jane Instructor"
        );
        
        enrollmentResponse = new EnrollmentResponse(
                "Student John Doe successfully enrolled in course Java Basics",
                LocalDateTime.now(),
                studentInfo,
                courseInfo
        );
        
        UnenrollmentResponse.StudentInfo unenrollStudentInfo = new UnenrollmentResponse.StudentInfo(
                studentId, "John Doe", "john.doe@example.com"
        );
        
        UnenrollmentResponse.CourseInfo unenrollCourseInfo = new UnenrollmentResponse.CourseInfo(
                courseId, "Java Basics", "Jane Instructor"
        );
        
        unenrollmentResponse = new UnenrollmentResponse(
                "Student John Doe successfully unenrolled from course Java Basics",
                LocalDateTime.now(),
                unenrollStudentInfo,
                unenrollCourseInfo
        );
    }

    @Nested
    @DisplayName("Enrollment Tests")
    class EnrollmentTests {

        @Test
        @DisplayName("Should enroll student in course successfully")
        void shouldEnrollStudentInCourseSuccessfully() throws Exception {
            // Given
            when(studentService.enrollStudentInCourse(eq(studentId), any()))
                    .thenReturn(enrollmentResponse);

            // When & Then
            mockMvc.perform(post("/api/v1/students/{studentId}/enroll", studentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(enrollmentRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(containsString("successfully enrolled")))
                    .andExpect(jsonPath("$.student.id").value(studentId.toString()))
                    .andExpect(jsonPath("$.course.id").value(courseId.toString()));

            verify(studentService).enrollStudentInCourse(eq(studentId), any());
        }

        @Test
        @DisplayName("Should return 400 for invalid enrollment request")
        void shouldReturn400ForInvalidEnrollmentRequest() throws Exception {
            // Given
            EnrollmentRequest invalidRequest = new EnrollmentRequest(null);

            // When & Then
            mockMvc.perform(post("/api/v1/students/{studentId}/enroll", studentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(studentService, never()).enrollStudentInCourse(any(), any());
        }
    }

    @Nested
    @DisplayName("Unenrollment Tests")
    class UnenrollmentTests {

        @Test
        @DisplayName("Should unenroll student from course successfully")
        void shouldUnenrollStudentFromCourseSuccessfully() throws Exception {
            // Given
            when(studentService.unenrollStudentFromCourse(eq(studentId), any()))
                    .thenReturn(unenrollmentResponse);

            // When & Then
            mockMvc.perform(post("/api/v1/students/{studentId}/unenroll", studentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(enrollmentRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(containsString("successfully unenrolled")))
                    .andExpect(jsonPath("$.student.id").value(studentId.toString()));

            verify(studentService).unenrollStudentFromCourse(eq(studentId), any());
        }

        @Test
        @DisplayName("Should return 400 for invalid unenrollment request")
        void shouldReturn400ForInvalidUnenrollmentRequest() throws Exception {
            // Given
            EnrollmentRequest invalidRequest = new EnrollmentRequest(null);

            // When & Then
            mockMvc.perform(post("/api/v1/students/{studentId}/unenroll", studentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(studentService, never()).unenrollStudentFromCourse(any(), any());
        }
    }

    @Nested
    @DisplayName("Get Enrolled Students Tests")
    class GetEnrolledStudentsTests {

        @Test
        @DisplayName("Should get students enrolled in course successfully")
        void shouldGetStudentsEnrolledInCourseSuccessfully() throws Exception {
            // Given
            StudentResponse studentResponse = new StudentResponse(
                    studentId, "John", "Doe", "john.doe@example.com",
                    LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
            );
            List<StudentResponse> students = Arrays.asList(studentResponse);
            when(studentService.getStudentsEnrolledInCourse(courseId)).thenReturn(students);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{courseId}/students", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(studentId.toString()));

            verify(studentService).getStudentsEnrolledInCourse(courseId);
        }

        @Test
        @DisplayName("Should return empty list when no students enrolled")
        void shouldReturnEmptyListWhenNoStudentsEnrolled() throws Exception {
            // Given
            when(studentService.getStudentsEnrolledInCourse(courseId)).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{courseId}/students", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(studentService).getStudentsEnrolledInCourse(courseId);
        }
    }

    @Nested
    @DisplayName("Enrollment Status Tests")
    class EnrollmentStatusTests {

        @Test
        @DisplayName("Should check enrollment status successfully")
        void shouldCheckEnrollmentStatusSuccessfully() throws Exception {
            // Given
            when(studentService.isStudentEnrolledInCourse(studentId, courseId)).thenReturn(true);

            // When & Then
            mockMvc.perform(get("/api/v1/students/{studentId}/courses/{courseId}/enrolled", studentId, courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.enrolled").value(true));

            verify(studentService).isStudentEnrolledInCourse(studentId, courseId);
        }

        @Test
        @DisplayName("Should return false when student not enrolled")
        void shouldReturnFalseWhenStudentNotEnrolled() throws Exception {
            // Given
            when(studentService.isStudentEnrolledInCourse(studentId, courseId)).thenReturn(false);

            // When & Then
            mockMvc.perform(get("/api/v1/students/{studentId}/courses/{courseId}/enrolled", studentId, courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.enrolled").value(false));

            verify(studentService).isStudentEnrolledInCourse(studentId, courseId);
        }
    }

    @Nested
    @DisplayName("Enrollment Statistics Tests")
    class EnrollmentStatisticsTests {

        @Test
        @DisplayName("Should get student count for course successfully")
        void shouldGetStudentCountForCourseSuccessfully() throws Exception {
            // Given
            long count = 25L;
            when(studentService.countStudentsInCourse(courseId)).thenReturn(count);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{courseId}/students/count", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(25))
                    .andExpect(jsonPath("$.message").value("Total students enrolled in course"));

            verify(studentService).countStudentsInCourse(courseId);
        }

        @Test
        @DisplayName("Should return zero count when no students enrolled")
        void shouldReturnZeroCountWhenNoStudentsEnrolled() throws Exception {
            // Given
            when(studentService.countStudentsInCourse(courseId)).thenReturn(0L);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{courseId}/students/count", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(0));

            verify(studentService).countStudentsInCourse(courseId);
        }
    }
}