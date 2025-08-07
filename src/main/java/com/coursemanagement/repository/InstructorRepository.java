package com.coursemanagement.repository;

import com.coursemanagement.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Instructor entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    /**
     * Find instructor by email address.
     */
    Optional<Instructor> findByEmail(String email);

    /**
     * Find instructors by full name (case-insensitive search in both first and last names).
     * Searches for the term in the full name, first name, or last name.
     */
    @Query("SELECT i FROM Instructor i WHERE " +
           "LOWER(CONCAT(i.firstName, ' ', i.lastName)) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(i.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(i.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Instructor> findByFullNameContaining(@Param("name") String name);

    /**
     * Check if instructor exists by email.
     */
    boolean existsByEmail(String email);

    /**
     * Find all instructors with instructor details.
     */
    @Query("SELECT i FROM Instructor i WHERE i.instructorDetails IS NOT NULL")
    List<Instructor> findInstructorsWithDetails();

    /**
     * Find all instructors without instructor details.
     */
    @Query("SELECT i FROM Instructor i WHERE i.instructorDetails IS NULL")
    List<Instructor> findInstructorsWithoutDetails();
}