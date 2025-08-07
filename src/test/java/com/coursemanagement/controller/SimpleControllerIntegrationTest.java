package com.coursemanagement.controller;

import com.coursemanagement.dto.*;
import com.coursemanagement.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Simple integration tests to boost controller coverage.
 * Focuses on basic CRUD operations that are likely to work.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Simple Controller Integration Tests")
class SimpleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private InstructorService instructorService;

    @MockBean
    private InstructorDetailsService instructorDetailsService;

    @Test
    @DisplayName("Should create student successfully")
    void shouldCreateStudentSuccessfully() throws Exception {
        // Given
        StudentRequest request = new StudentRequest("John", "Doe", "john.doe@example.com");
        StudentResponse response = new StudentResponse(
                UUID.randomUUID(),
                "John",
                "Doe", 
                "john.doe@example.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(studentService.createStudent(any(StudentRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @DisplayName("Should get all students successfully")
    void shouldGetAllStudentsSuccessfully() throws Exception {
        // Given
        StudentResponse student = new StudentResponse(
                UUID.randomUUID(),
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student));

        // When & Then
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    @DisplayName("Should create course successfully")
    void shouldCreateCourseSuccessfully() throws Exception {
        // Given
        UUID instructorId = UUID.randomUUID();
        CourseRequest request = new CourseRequest("Java Basics", instructorId);
        
        CourseResponse.InstructorInfo instructorInfo = new CourseResponse.InstructorInfo(
                instructorId, "John Instructor", "john@example.com"
        );
        
        CourseResponse response = new CourseResponse(
                UUID.randomUUID(),
                "Java Basics",
                LocalDateTime.now(),
                LocalDateTime.now(),
                instructorInfo,
                null
        );

        when(courseService.createCourse(any(CourseRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Java Basics"))
                .andExpect(jsonPath("$.instructor.fullName").value("John Instructor"));
    }

    @Test
    @DisplayName("Should get all courses successfully")
    void shouldGetAllCoursesSuccessfully() throws Exception {
        // Given
        CourseResponse.InstructorInfo instructorInfo = new CourseResponse.InstructorInfo(
                UUID.randomUUID(), "John Instructor", "john@example.com"
        );
        
        CourseResponse course = new CourseResponse(
                UUID.randomUUID(),
                "Java Basics",
                LocalDateTime.now(),
                LocalDateTime.now(),
                instructorInfo,
                null
        );

        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course));

        // When & Then
        mockMvc.perform(get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Java Basics"));
    }

    @Test
    @DisplayName("Should create review successfully")
    void shouldCreateReviewSuccessfully() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        ReviewRequest request = new ReviewRequest("Great course!", studentId);
        
        ReviewResponse.CourseInfo courseInfo = new ReviewResponse.CourseInfo(
                courseId, "Java Basics", "John Instructor"
        );
        
        ReviewResponse.StudentInfo studentInfo = new ReviewResponse.StudentInfo(
                studentId, "Jane Student", "jane@example.com"
        );
        
        ReviewResponse response = new ReviewResponse(
                UUID.randomUUID(),
                "Great course!",
                LocalDateTime.now(),
                LocalDateTime.now(),
                courseInfo,
                studentInfo
        );

        when(reviewService.createReview(any(UUID.class), any(ReviewRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/courses/{courseId}/reviews", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value("Great course!"))
                .andExpect(jsonPath("$.course.title").value("Java Basics"));
    }

    @Test
    @DisplayName("Should get all reviews successfully")
    void shouldGetAllReviewsSuccessfully() throws Exception {
        // Given
        ReviewResponse.CourseInfo courseInfo = new ReviewResponse.CourseInfo(
                UUID.randomUUID(), "Java Basics", "John Instructor"
        );
        
        ReviewResponse.StudentInfo studentInfo = new ReviewResponse.StudentInfo(
                UUID.randomUUID(), "Jane Student", "jane@example.com"
        );
        
        ReviewResponse review = new ReviewResponse(
                UUID.randomUUID(),
                "Great course!",
                LocalDateTime.now(),
                LocalDateTime.now(),
                courseInfo,
                studentInfo
        );

        when(reviewService.getAllReviews()).thenReturn(Arrays.asList(review));

        // When & Then
        mockMvc.perform(get("/api/v1/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].comment").value("Great course!"));
    }
}