package com.coursemanagement.repository;

import com.coursemanagement.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Review entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    /**
     * Find reviews by course ID.
     */
    List<Review> findByCourseId(UUID courseId);

    /**
     * Find reviews by course ID ordered by creation date (newest first).
     */
    List<Review> findByCourseIdOrderByCreatedAtDesc(UUID courseId);

    /**
     * Find reviews containing keyword in comment (case-insensitive).
     */
    @Query("SELECT r FROM Review r WHERE LOWER(r.comment) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Review> findByCommentContainingIgnoreCase(@Param("keyword") String keyword);

    /**
     * Find reviews by instructor (through course relationship).
     */
    @Query("SELECT r FROM Review r JOIN r.course c WHERE c.instructor.id = :instructorId")
    List<Review> findByInstructorId(@Param("instructorId") UUID instructorId);

    /**
     * Find reviews by course title containing keyword.
     */
    @Query("SELECT r FROM Review r JOIN r.course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Review> findByCourseTitle(@Param("title") String title);

    /**
     * Count reviews by course ID.
     */
    long countByCourseId(UUID courseId);

    /**
     * Find latest reviews (most recent first).
     */
    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
    List<Review> findLatestReviews();

    /**
     * Find reviews with course and instructor information.
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.course c JOIN FETCH c.instructor")
    List<Review> findAllWithCourseAndInstructor();
}