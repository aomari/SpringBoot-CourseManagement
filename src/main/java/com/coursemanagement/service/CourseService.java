package com.coursemanagement.service;

import com.coursemanagement.dto.CourseRequest;
import com.coursemanagement.dto.CourseResponse;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Course operations.
 */
public interface CourseService {

    /**
     * Create new course.
     */
    CourseResponse createCourse(CourseRequest request);

    /**
     * Get course by ID.
     */
    CourseResponse getCourseById(UUID id);

    /**
     * Get course by ID with reviews.
     */
    CourseResponse getCourseByIdWithReviews(UUID id);

    /**
     * Get all courses.
     */
    List<CourseResponse> getAllCourses();

    /**
     * Get all courses with reviews.
     */
    List<CourseResponse> getAllCoursesWithReviews();

    /**
     * Update course.
     */
    CourseResponse updateCourse(UUID id, CourseRequest request);

    /**
     * Delete course.
     */
    void deleteCourse(UUID id);

    /**
     * Get courses by instructor ID.
     */
    List<CourseResponse> getCoursesByInstructorId(UUID instructorId);

    /**
     * Get courses by instructor ID with reviews.
     */
    List<CourseResponse> getCoursesByInstructorIdWithReviews(UUID instructorId);

    /**
     * Search courses by title.
     */
    List<CourseResponse> searchCoursesByTitle(String title);

    /**
     * Search courses by instructor name.
     */
    List<CourseResponse> searchCoursesByInstructorName(String name);

    /**
     * Check if course exists by ID.
     */
    boolean existsById(UUID id);

    /**
     * Check if course exists by title and instructor ID.
     */
    boolean existsByTitleAndInstructorId(String title, UUID instructorId);

    /**
     * Count courses by instructor ID.
     */
    long countCoursesByInstructorId(UUID instructorId);
}