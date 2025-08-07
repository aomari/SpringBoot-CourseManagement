package com.coursemanagement.controller;

import com.coursemanagement.dto.CourseRequest;
import com.coursemanagement.dto.CourseResponse;
import com.coursemanagement.service.CourseService;
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

@WebMvcTest(CourseController.class)
@DisplayName("CourseController Tests")
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseRequest courseRequest;
    private CourseResponse courseResponse;
    private UUID courseId;
    private UUID instructorId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        instructorId = UUID.randomUUID();
        courseRequest = new CourseRequest("Java Basics", instructorId);
        
        CourseResponse.InstructorInfo instructorInfo = new CourseResponse.InstructorInfo(
                instructorId, "John Doe", "john.doe@example.com"
        );
        
        courseResponse = new CourseResponse(
                courseId,
                "Java Basics",
                LocalDateTime.now(),
                LocalDateTime.now(),
                instructorInfo,
                Collections.emptyList()
        );
    }

    @Nested
    @DisplayName("Create Course Tests")
    class CreateCourseTests {

        @Test
        @DisplayName("Should create course successfully")
        void shouldCreateCourseSuccessfully() throws Exception {
            // Given
            when(courseService.createCourse(any())).thenReturn(courseResponse);

            // When & Then
            mockMvc.perform(post("/api/v1/courses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(courseRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(courseId.toString()))
                    .andExpect(jsonPath("$.title").value("Java Basics"))
                    .andExpect(jsonPath("$.instructor.id").value(instructorId.toString()));

            verify(courseService).createCourse(any());
        }

        @Test
        @DisplayName("Should return 400 for invalid request data")
        void shouldReturn400ForInvalidRequestData() throws Exception {
            // Given
            CourseRequest invalidRequest = new CourseRequest("", null);

            // When & Then
            mockMvc.perform(post("/api/v1/courses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(courseService, never()).createCourse(any());
        }
    }

    @Nested
    @DisplayName("Get Course Tests")
    class GetCourseTests {

        @Test
        @DisplayName("Should get course by ID successfully")
        void shouldGetCourseByIdSuccessfully() throws Exception {
            // Given
            when(courseService.getCourseById(courseId)).thenReturn(courseResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{id}", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(courseId.toString()))
                    .andExpect(jsonPath("$.title").value("Java Basics"));

            verify(courseService).getCourseById(courseId);
        }

        @Test
        @DisplayName("Should get all courses successfully")
        void shouldGetAllCoursesSuccessfully() throws Exception {
            // Given
            List<CourseResponse> courses = Arrays.asList(courseResponse);
            when(courseService.getAllCourses()).thenReturn(courses);

            // When & Then
            mockMvc.perform(get("/api/v1/courses"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(courseId.toString()));

            verify(courseService).getAllCourses();
        }

        @Test
        @DisplayName("Should get courses with reviews successfully")
        void shouldGetCoursesWithReviewsSuccessfully() throws Exception {
            // Given
            List<CourseResponse> courses = Arrays.asList(courseResponse);
            when(courseService.getAllCoursesWithReviews()).thenReturn(courses);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/with-reviews"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(courseService).getAllCoursesWithReviews();
        }

        @Test
        @DisplayName("Should get course by ID with reviews successfully")
        void shouldGetCourseByIdWithReviewsSuccessfully() throws Exception {
            // Given
            when(courseService.getCourseByIdWithReviews(courseId)).thenReturn(courseResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{id}/with-reviews", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(courseId.toString()));

            verify(courseService).getCourseByIdWithReviews(courseId);
        }
    }

    @Nested
    @DisplayName("Update Course Tests")
    class UpdateCourseTests {

        @Test
        @DisplayName("Should update course successfully")
        void shouldUpdateCourseSuccessfully() throws Exception {
            // Given
            CourseRequest updateRequest = new CourseRequest("Java Advanced", instructorId);
            CourseResponse updatedResponse = new CourseResponse(
                    courseId,
                    "Java Advanced",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    courseResponse.getInstructor(),
                    Collections.emptyList()
            );
            when(courseService.updateCourse(eq(courseId), any())).thenReturn(updatedResponse);

            // When & Then
            mockMvc.perform(put("/api/v1/courses/{id}", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Java Advanced"));

            verify(courseService).updateCourse(eq(courseId), any());
        }

        @Test
        @DisplayName("Should return 400 for invalid update data")
        void shouldReturn400ForInvalidUpdateData() throws Exception {
            // Given
            CourseRequest invalidRequest = new CourseRequest("", null);

            // When & Then
            mockMvc.perform(put("/api/v1/courses/{id}", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(courseService, never()).updateCourse(any(), any());
        }
    }

    @Nested
    @DisplayName("Delete Course Tests")
    class DeleteCourseTests {

        @Test
        @DisplayName("Should delete course successfully")
        void shouldDeleteCourseSuccessfully() throws Exception {
            // Given
            doNothing().when(courseService).deleteCourse(courseId);

            // When & Then
            mockMvc.perform(delete("/api/v1/courses/{id}", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Course deleted successfully"))
                    .andExpect(jsonPath("$.resourceId").value(courseId.toString()));

            verify(courseService).deleteCourse(courseId);
        }
    }

    @Nested
    @DisplayName("Search Course Tests")
    class SearchCourseTests {

        @Test
        @DisplayName("Should search courses by title successfully")
        void shouldSearchCoursesByTitleSuccessfully() throws Exception {
            // Given
            String searchTerm = "Java";
            List<CourseResponse> courses = Arrays.asList(courseResponse);
            when(courseService.searchCoursesByTitle(searchTerm)).thenReturn(courses);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/search")
                            .param("title", searchTerm))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].title").value("Java Basics"));

            verify(courseService).searchCoursesByTitle(searchTerm);
        }

        @Test
        @DisplayName("Should search courses by instructor name successfully")
        void shouldSearchCoursesByInstructorNameSuccessfully() throws Exception {
            // Given
            String searchTerm = "John";
            List<CourseResponse> courses = Arrays.asList(courseResponse);
            when(courseService.searchCoursesByInstructorName(searchTerm)).thenReturn(courses);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/search")
                            .param("instructor", searchTerm))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(courseService).searchCoursesByInstructorName(searchTerm);
        }

        @Test
        @DisplayName("Should return empty list when no courses match search")
        void shouldReturnEmptyListWhenNoCoursesMatchSearch() throws Exception {
            // Given
            String searchTerm = "nonexistent";
            when(courseService.searchCoursesByTitle(searchTerm)).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/v1/courses/search")
                            .param("title", searchTerm))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(courseService).searchCoursesByTitle(searchTerm);
        }
    }

    @Nested
    @DisplayName("Course by Instructor Tests")
    class CourseByInstructorTests {

        @Test
        @DisplayName("Should get courses by instructor ID successfully")
        void shouldGetCoursesByInstructorIdSuccessfully() throws Exception {
            // Given
            List<CourseResponse> courses = Arrays.asList(courseResponse);
            when(courseService.getCoursesByInstructorId(instructorId)).thenReturn(courses);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/instructor/{instructorId}", instructorId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(courseService).getCoursesByInstructorId(instructorId);
        }

        @Test
        @DisplayName("Should get courses by instructor ID with reviews successfully")
        void shouldGetCoursesByInstructorIdWithReviewsSuccessfully() throws Exception {
            // Given
            List<CourseResponse> courses = Arrays.asList(courseResponse);
            when(courseService.getCoursesByInstructorIdWithReviews(instructorId)).thenReturn(courses);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/instructor/{instructorId}/with-reviews", instructorId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(courseService).getCoursesByInstructorIdWithReviews(instructorId);
        }
    }

    @Nested
    @DisplayName("Course Statistics Tests")
    class CourseStatisticsTests {

        @Test
        @DisplayName("Should get course count by instructor successfully")
        void shouldGetCourseCountByInstructorSuccessfully() throws Exception {
            // Given
            long count = 5L;
            when(courseService.countCoursesByInstructorId(instructorId)).thenReturn(count);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/count/instructor/{instructorId}", instructorId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(5))
                    .andExpect(jsonPath("$.message").value("Total courses by instructor"));

            verify(courseService).countCoursesByInstructorId(instructorId);
        }

        @Test
        @DisplayName("Should check if course exists successfully")
        void shouldCheckIfCourseExistsSuccessfully() throws Exception {
            // Given
            when(courseService.existsById(courseId)).thenReturn(true);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/{id}/exists", courseId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exists").value(true));

            verify(courseService).existsById(courseId);
        }

        @Test
        @DisplayName("Should check if course exists by title and instructor successfully")
        void shouldCheckIfCourseExistsByTitleAndInstructorSuccessfully() throws Exception {
            // Given
            String title = "Java Basics";
            when(courseService.existsByTitleAndInstructorId(title, instructorId)).thenReturn(true);

            // When & Then
            mockMvc.perform(get("/api/v1/courses/exists")
                            .param("title", title)
                            .param("instructorId", instructorId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exists").value(true));

            verify(courseService).existsByTitleAndInstructorId(title, instructorId);
        }
    }
}