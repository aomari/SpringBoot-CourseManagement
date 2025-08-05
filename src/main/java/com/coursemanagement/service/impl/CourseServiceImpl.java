package com.coursemanagement.service.impl;

import com.coursemanagement.dto.CourseRequest;
import com.coursemanagement.dto.CourseResponse;
import com.coursemanagement.dto.ReviewResponse;
import com.coursemanagement.entity.Course;
import com.coursemanagement.entity.Instructor;
import com.coursemanagement.entity.Review;
import com.coursemanagement.exception.ResourceAlreadyExistsException;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.InstructorRepository;
import com.coursemanagement.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for Course operations.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    @Override
    public CourseResponse createCourse(CourseRequest request) {
        // Validate instructor exists
        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", request.getInstructorId()));

        // Check if course with same title already exists for this instructor
        if (courseRepository.existsByTitleAndInstructorId(request.getTitle(), request.getInstructorId())) {
            throw new ResourceAlreadyExistsException("Course", "title", request.getTitle() + " for instructor " + instructor.getFullName());
        }

        Course course = new Course(request.getTitle(), instructor);
        Course savedCourse = courseRepository.save(course);
        return mapToResponse(savedCourse, false);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        return mapToResponse(course, false);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseByIdWithReviews(UUID id) {
        Course course = courseRepository.findByIdWithReviews(id);
        if (course == null) {
            throw new ResourceNotFoundException("Course", "id", id);
        }
        
        return mapToResponse(course, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(course -> mapToResponse(course, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCoursesWithReviews() {
        return courseRepository.findAllWithReviews()
                .stream()
                .map(course -> mapToResponse(course, true))
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse updateCourse(UUID id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        // Validate instructor exists
        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", request.getInstructorId()));

        // Check if title is being changed and if new title already exists for this instructor
        if (!course.getTitle().equals(request.getTitle()) && 
            courseRepository.existsByTitleAndInstructorId(request.getTitle(), request.getInstructorId())) {
            throw new ResourceAlreadyExistsException("Course", "title", request.getTitle() + " for instructor " + instructor.getFullName());
        }

        course.setTitle(request.getTitle());
        course.setInstructor(instructor);

        Course updatedCourse = courseRepository.save(course);
        return mapToResponse(updatedCourse, false);
    }

    @Override
    public void deleteCourse(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", "id", id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByInstructorId(UUID instructorId) {
        // Validate instructor exists
        if (!instructorRepository.existsById(instructorId)) {
            throw new ResourceNotFoundException("Instructor", "id", instructorId);
        }

        return courseRepository.findByInstructorId(instructorId)
                .stream()
                .map(course -> mapToResponse(course, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByInstructorIdWithReviews(UUID instructorId) {
        // Validate instructor exists
        if (!instructorRepository.existsById(instructorId)) {
            throw new ResourceNotFoundException("Instructor", "id", instructorId);
        }

        return courseRepository.findByInstructorIdWithReviews(instructorId)
                .stream()
                .map(course -> mapToResponse(course, true))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> searchCoursesByTitle(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(course -> mapToResponse(course, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> searchCoursesByInstructorName(String name) {
        return courseRepository.findByInstructorNameContaining(name)
                .stream()
                .map(course -> mapToResponse(course, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return courseRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTitleAndInstructorId(String title, UUID instructorId) {
        return courseRepository.existsByTitleAndInstructorId(title, instructorId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countCoursesByInstructorId(UUID instructorId) {
        return courseRepository.countByInstructorId(instructorId);
    }

    /**
     * Helper method to map Course entity to CourseResponse DTO.
     */
    private CourseResponse mapToResponse(Course course, boolean includeReviews) {
        CourseResponse.InstructorInfo instructorInfo = new CourseResponse.InstructorInfo(
                course.getInstructor().getId(),
                course.getInstructor().getFullName(),
                course.getInstructor().getEmail()
        );

        List<ReviewResponse> reviewResponses = null;
        if (includeReviews && course.getReviews() != null) {
            reviewResponses = course.getReviews().stream()
                    .map(this::mapReviewToResponse)
                    .collect(Collectors.toList());
        }

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCreatedAt(),
                course.getUpdatedAt(),
                instructorInfo,
                reviewResponses
        );
    }

    /**
     * Helper method to map Review entity to ReviewResponse DTO.
     */
    private ReviewResponse mapReviewToResponse(Review review) {
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