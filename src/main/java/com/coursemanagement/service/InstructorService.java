package com.coursemanagement.service;

import com.coursemanagement.dto.InstructorRequest;
import com.coursemanagement.dto.InstructorResponse;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Instructor operations.
 */
public interface InstructorService {

    /**
     * Create new instructor.
     */
    InstructorResponse createInstructor(InstructorRequest request);

    /**
     * Get instructor by ID.
     */
    InstructorResponse getInstructorById(UUID id);

    /**
     * Get all instructors.
     */
    List<InstructorResponse> getAllInstructors();

    /**
     * Update instructor.
     */
    InstructorResponse updateInstructor(UUID id, InstructorRequest request);

    /**
     * Delete instructor.
     */
    void deleteInstructor(UUID id);

    /**
     * Get instructor by email.
     */
    InstructorResponse getInstructorByEmail(String email);

    /**
     * Search instructors by name (first name, last name, or full name).
     */
    List<InstructorResponse> searchInstructorsByName(String name);

    /**
     * Get instructors with details.
     */
    List<InstructorResponse> getInstructorsWithDetails();

    /**
     * Get instructors without details.
     */
    List<InstructorResponse> getInstructorsWithoutDetails();

    /**
     * Add instructor details to existing instructor.
     */
    InstructorResponse addInstructorDetails(UUID instructorId, UUID instructorDetailsId);

    /**
     * Remove instructor details from instructor.
     */
    InstructorResponse removeInstructorDetails(UUID instructorId);

    /**
     * Check if instructor exists by ID.
     */
    boolean existsById(UUID id);

    /**
     * Check if instructor exists by email.
     */
    boolean existsByEmail(String email);
}