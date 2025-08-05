package com.coursemanagement.service;

import com.coursemanagement.dto.InstructorDetailsRequest;
import com.coursemanagement.dto.InstructorDetailsResponse;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for InstructorDetails operations.
 */
public interface InstructorDetailsService {

    /**
     * Create new instructor details.
     */
    InstructorDetailsResponse createInstructorDetails(InstructorDetailsRequest request);

    /**
     * Get instructor details by ID.
     */
    InstructorDetailsResponse getInstructorDetailsById(UUID id);

    /**
     * Get all instructor details.
     */
    List<InstructorDetailsResponse> getAllInstructorDetails();

    /**
     * Update instructor details.
     */
    InstructorDetailsResponse updateInstructorDetails(UUID id, InstructorDetailsRequest request);

    /**
     * Delete instructor details.
     */
    void deleteInstructorDetails(UUID id);

    /**
     * Search instructor details by YouTube channel.
     */
    List<InstructorDetailsResponse> searchByYoutubeChannel(String youtubeChannel);

    /**
     * Search instructor details by hobby.
     */
    List<InstructorDetailsResponse> searchByHobby(String hobby);

    /**
     * Get orphaned instructor details (not linked to any instructor).
     */
    List<InstructorDetailsResponse> getOrphanedInstructorDetails();

    /**
     * Check if instructor details exist by ID.
     */
    boolean existsById(UUID id);
}