package com.coursemanagement.service.impl;

import com.coursemanagement.dto.ReviewRequest;
import com.coursemanagement.dto.ReviewResponse;
import com.coursemanagement.entity.Course;
import com.coursemanagement.entity.Review;
import com.coursemanagement.entity.Student;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.ReviewRepository;
import com.coursemanagement.repository.StudentRepository;
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
    private final StudentRepository studentRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, CourseRepository courseRepository, 
                           StudentRepository studentRepository) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public ReviewResponse createReview(UUID courseId, ReviewRequest request) {
        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        // Validate student exists
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));

        Review review = new Review(request.getComment(), course, student);
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
        return reviewRepository.findAllWithCourseInstructorAndStudent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse updateReview(UUID id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", id));

        // Validate student exists if student is being changed
        if (!review.getStudent().getId().equals(request.getStudentId())) {
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));
            review.setStudent(student);
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

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByStudentId(UUID studentId) {
        // Validate student exists
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student", "id", studentId);
        }

        return reviewRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByStudentIdOrderedByDate(UUID studentId) {
        // Validate student exists
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student", "id", studentId);
        }

        return reviewRepository.findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByCourseIdAndStudentId(UUID courseId, UUID studentId) {
        // Validate course and student exist
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student", "id", studentId);
        }

        return reviewRepository.findByCourseIdAndStudentId(courseId, studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countReviewsByStudentId(UUID studentId) {
        return reviewRepository.countByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> searchReviewsByStudentEmail(String email) {
        return reviewRepository.findByStudentEmail(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> searchReviewsByStudentName(String name) {
        return reviewRepository.findByStudentName(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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

        ReviewResponse.StudentInfo studentInfo = new ReviewResponse.StudentInfo(
                review.getStudent().getId(),
                review.getStudent().getFullName(),
                review.getStudent().getEmail()
        );

        return new ReviewResponse(
                review.getId(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                courseInfo,
                studentInfo
        );
    }
}