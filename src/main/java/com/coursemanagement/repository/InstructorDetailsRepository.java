package com.coursemanagement.repository;

import com.coursemanagement.entity.InstructorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for InstructorDetails entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface InstructorDetailsRepository extends JpaRepository<InstructorDetails, UUID> {

    /**
     * Find instructor details by YouTube channel.
     */
    List<InstructorDetails> findByYoutubeChannelContainingIgnoreCase(String youtubeChannel);

    /**
     * Find instructor details by hobby.
     */
    List<InstructorDetails> findByHobbyContainingIgnoreCase(String hobby);

    /**
     * Find all instructor details that are not linked to any instructor.
     */
    @Query("SELECT id FROM InstructorDetails id WHERE id.instructor IS NULL")
    List<InstructorDetails> findOrphanedInstructorDetails();
}