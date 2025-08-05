package com.coursemanagement.service.impl;

import com.coursemanagement.dto.ReviewRequest;
import com.coursemanagement.dto.ReviewResponse;
import com.coursemanagement.entity.Course;
import com.coursemanagement.entity.Review;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.ReviewRepository;
import com.coursemanagement.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for Review operations.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, CourseRepository courseRepository) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public ReviewResponse createReview(UUID courseId, ReviewRequest request) {
        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        Review review = new Review(request.getComment(), course);
        Review savedReview = reviewRepository.save(review);
        return mapToResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", id));
        
        return mapToResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAllWithCourseAndInstructor()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse updateReview(UUID id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", id));

        // Validate course exists if course is being changed
        if (!review.getCourse().getId().equals(request.getCourseId())) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));
            review.setCourse(course);
        }

        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);
        return mapToResponse(updatedReview);
    }

    @Override
    public void deleteReview(UUID id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review", "id", id);
        }
        reviewRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByCourseId(UUID courseId) {
        // Validate course exists
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }

        return reviewRepository.findByCourseId(courseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByCourseIdOrderedByDate(UUID courseId) {
        // Validate course exists
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }

        return reviewRepository.findByCourseIdOrderByCreatedAtDesc(courseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> searchReviewsByComment(String keyword) {
        return reviewRepository.findByCommentContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByInstructorId(UUID instructorId) {
        return reviewRepository.findByInstructorId(instructorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> searchReviewsByCourseTitle(String title) {
        return reviewRepository.findByCourseTitle(title)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getLatestReviews() {
        return reviewRepository.findLatestReviews()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return reviewRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countReviewsByCourseId(UUID courseId) {
        return reviewRepository.countByCourseId(courseId);
    }

    /**
     * Helper method to map Review entity to ReviewResponse DTO.
     */
    private ReviewResponse mapToResponse(Review review) {
        ReviewResponse.CourseInfo courseInfo = new ReviewResponse.CourseInfo(
                review.getCourse().getId(),
                review.getCourse().getTitle(),
                review.getCourse().getInstructor().getFullName()
        );

        return new ReviewResponse(
                review.getId(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                courseInfo
        );
    }
}