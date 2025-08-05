package com.coursemanagement.service;

import com.coursemanagement.dto.ReviewRequest;
import com.coursemanagement.dto.ReviewResponse;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Review operations.
 */
public interface ReviewService {

    /**
     * Create new review for a course.
     */
    ReviewResponse createReview(UUID courseId, ReviewRequest request);

    /**
     * Get review by ID.
     */
    ReviewResponse getReviewById(UUID id);

    /**
     * Get all reviews.
     */
    List<ReviewResponse> getAllReviews();

    /**
     * Update review.
     */
    ReviewResponse updateReview(UUID id, ReviewRequest request);

    /**
     * Delete review.
     */
    void deleteReview(UUID id);

    /**
     * Get reviews by course ID.
     */
    List<ReviewResponse> getReviewsByCourseId(UUID courseId);

    /**
     * Get reviews by course ID ordered by creation date (newest first).
     */
    List<ReviewResponse> getReviewsByCourseIdOrderedByDate(UUID courseId);

    /**
     * Search reviews by comment keyword.
     */
    List<ReviewResponse> searchReviewsByComment(String keyword);

    /**
     * Get reviews by instructor ID (through course relationship).
     */
    List<ReviewResponse> getReviewsByInstructorId(UUID instructorId);

    /**
     * Search reviews by course title.
     */
    List<ReviewResponse> searchReviewsByCourseTitle(String title);

    /**
     * Get latest reviews (most recent first).
     */
    List<ReviewResponse> getLatestReviews();

    /**
     * Check if review exists by ID.
     */
    boolean existsById(UUID id);

    /**
     * Count reviews by course ID.
     */
    long countReviewsByCourseId(UUID courseId);
}