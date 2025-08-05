package com.coursemanagement.repository;

import com.coursemanagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Course entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    /**
     * Find courses by instructor ID.
     */
    List<Course> findByInstructorId(UUID instructorId);

    /**
     * Find courses by title containing keyword (case-insensitive).
     */
    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Course> findByTitleContainingIgnoreCase(@Param("title") String title);

    /**
     * Find courses by instructor name (first or last name).
     */
    @Query("SELECT c FROM Course c JOIN c.instructor i WHERE " +
           "LOWER(i.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(i.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(CONCAT(i.firstName, ' ', i.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Course> findByInstructorNameContaining(@Param("name") String name);

    /**
     * Check if course exists by title and instructor ID.
     */
    boolean existsByTitleAndInstructorId(String title, UUID instructorId);

    /**
     * Find courses with reviews count.
     */
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.reviews WHERE c.id = :courseId")
    Course findByIdWithReviews(@Param("courseId") UUID courseId);

    /**
     * Find all courses with their reviews.
     */
    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.reviews")
    List<Course> findAllWithReviews();

    /**
     * Find courses by instructor with reviews count.
     */
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.reviews WHERE c.instructor.id = :instructorId")
    List<Course> findByInstructorIdWithReviews(@Param("instructorId") UUID instructorId);

    /**
     * Count courses by instructor ID.
     */
    long countByInstructorId(UUID instructorId);
}