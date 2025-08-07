package com.coursemanagement.repository;

import com.coursemanagement.entity.Instructor;
import com.coursemanagement.entity.InstructorDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for InstructorRepository.
 * Tests actual database interactions using @DataJpaTest.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Instructor Repository Tests")
class InstructorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InstructorRepository instructorRepository;

    // Test data
    private Instructor instructor1;
    private Instructor instructor2;
    private Instructor instructor3;
    private InstructorDetails instructorDetails1;
    private InstructorDetails instructorDetails2;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        entityManager.clear();
        
        // Setup instructor details
        instructorDetails1 = new InstructorDetails("https://youtube.com/@johndoe", "Playing guitar");
        instructorDetails2 = new InstructorDetails("https://youtube.com/@janesmith", "Painting");
        
        // Setup instructors
        instructor1 = new Instructor("John", "Doe", "john.doe@example.com");
        instructor1.setInstructorDetails(instructorDetails1);
        
        instructor2 = new Instructor("Jane", "Smith", "jane.smith@example.com");
        instructor2.setInstructorDetails(instructorDetails2);
        
        instructor3 = new Instructor("Bob", "Johnson", "bob.johnson@example.com");
        // instructor3 has no details
        
        // Persist test data
        entityManager.persistAndFlush(instructor1);
        entityManager.persistAndFlush(instructor2);
        entityManager.persistAndFlush(instructor3);
        entityManager.clear(); // Clear persistence context
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class BasicCrudOperations {

        @Test
        @DisplayName("Should save instructor successfully")
        void shouldSaveInstructorSuccessfully() {
            // Given
            Instructor newInstructor = new Instructor("Alice", "Brown", "alice.brown@example.com");
            
            // When
            Instructor savedInstructor = instructorRepository.save(newInstructor);
            
            // Then
            assertAll("Save instructor",
                () -> assertNotNull(savedInstructor.getId()),
                () -> assertEquals("Alice", savedInstructor.getFirstName()),
                () -> assertEquals("Brown", savedInstructor.getLastName()),
                () -> assertEquals("alice.brown@example.com", savedInstructor.getEmail()),
                () -> assertNotNull(savedInstructor.getCreatedAt()),
                () -> assertNotNull(savedInstructor.getUpdatedAt())
            );
        }

        @Test
        @DisplayName("Should save instructor with details successfully")
        void shouldSaveInstructorWithDetailsSuccessfully() {
            // Given
            InstructorDetails details = new InstructorDetails("https://youtube.com/@alice", "Coding");
            Instructor newInstructor = new Instructor("Alice", "Brown", "alice.brown@example.com");
            newInstructor.setInstructorDetails(details);
            
            // When
            Instructor savedInstructor = instructorRepository.save(newInstructor);
            entityManager.flush();
            entityManager.clear();
            
            // Then
            Optional<Instructor> foundInstructor = instructorRepository.findById(savedInstructor.getId());
            assertTrue(foundInstructor.isPresent());
            
            Instructor instructor = foundInstructor.get();
            assertAll("Save instructor with details",
                () -> assertNotNull(instructor.getInstructorDetails()),
                () -> assertEquals("https://youtube.com/@alice", instructor.getInstructorDetails().getYoutubeChannel()),
                () -> assertEquals("Coding", instructor.getInstructorDetails().getHobby()),
                () -> assertNotNull(instructor.getInstructorDetails().getId())
            );
        }

        @Test
        @DisplayName("Should find instructor by ID successfully")
        void shouldFindInstructorByIdSuccessfully() {
            // When
            Optional<Instructor> foundInstructor = instructorRepository.findById(instructor1.getId());
            
            // Then
            assertTrue(foundInstructor.isPresent());
            Instructor instructor = foundInstructor.get();
            
            assertAll("Find instructor by ID",
                () -> assertEquals(instructor1.getId(), instructor.getId()),
                () -> assertEquals("John", instructor.getFirstName()),
                () -> assertEquals("Doe", instructor.getLastName()),
                () -> assertEquals("john.doe@example.com", instructor.getEmail()),
                () -> assertNotNull(instructor.getInstructorDetails())
            );
        }

        @Test
        @DisplayName("Should return empty when instructor not found by ID")
        void shouldReturnEmptyWhenInstructorNotFoundById() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            
            // When
            Optional<Instructor> foundInstructor = instructorRepository.findById(nonExistentId);
            
            // Then
            assertFalse(foundInstructor.isPresent());
        }

        @Test
        @DisplayName("Should find all instructors successfully")
        void shouldFindAllInstructorsSuccessfully() {
            // When
            List<Instructor> allInstructors = instructorRepository.findAll();
            
            // Then
            assertAll("Find all instructors",
                () -> assertEquals(3, allInstructors.size()),
                () -> assertTrue(allInstructors.stream().anyMatch(i -> "John".equals(i.getFirstName()))),
                () -> assertTrue(allInstructors.stream().anyMatch(i -> "Jane".equals(i.getFirstName()))),
                () -> assertTrue(allInstructors.stream().anyMatch(i -> "Bob".equals(i.getFirstName())))
            );
        }

        @Test
        @DisplayName("Should update instructor successfully")
        void shouldUpdateInstructorSuccessfully() {
            // Given
            instructor1.setFirstName("John Updated");
            instructor1.setLastName("Doe Updated");
            instructor1.setEmail("john.updated@example.com");
            
            // When
            instructorRepository.save(instructor1);
            entityManager.flush();
            entityManager.clear();
            
            // Then
            Optional<Instructor> foundInstructor = instructorRepository.findById(instructor1.getId());
            assertTrue(foundInstructor.isPresent());
            
            Instructor instructor = foundInstructor.get();
            assertAll("Update instructor",
                () -> assertEquals("John Updated", instructor.getFirstName()),
                () -> assertEquals("Doe Updated", instructor.getLastName()),
                () -> assertEquals("john.updated@example.com", instructor.getEmail()),
                () -> assertNotNull(instructor.getUpdatedAt())
            );
        }

        @Test
        @DisplayName("Should delete instructor successfully")
        void shouldDeleteInstructorSuccessfully() {
            // Given
            UUID instructorId = instructor3.getId();
            assertTrue(instructorRepository.existsById(instructorId));
            
            // When
            instructorRepository.deleteById(instructorId);
            entityManager.flush();
            
            // Then
            assertFalse(instructorRepository.existsById(instructorId));
            Optional<Instructor> deletedInstructor = instructorRepository.findById(instructorId);
            assertFalse(deletedInstructor.isPresent());
        }

        @Test
        @DisplayName("Should cascade delete instructor details when deleting instructor")
        void shouldCascadeDeleteInstructorDetailsWhenDeletingInstructor() {
            // Given
            UUID instructorId = instructor1.getId();
            UUID detailsId = instructor1.getInstructorDetails().getId();
            
            assertTrue(instructorRepository.existsById(instructorId));
            
            // When
            instructorRepository.deleteById(instructorId);
            entityManager.flush();
            
            // Then
            assertFalse(instructorRepository.existsById(instructorId));
            // The details should also be deleted due to cascade
            InstructorDetails deletedDetails = entityManager.find(InstructorDetails.class, detailsId);
            assertNull(deletedDetails);
        }
    }

    @Nested
    @DisplayName("Custom Query Methods")
    class CustomQueryMethods {

        @Test
        @DisplayName("Should find instructor by email successfully")
        void shouldFindInstructorByEmailSuccessfully() {
            // When
            Optional<Instructor> foundInstructor = instructorRepository.findByEmail("john.doe@example.com");
            
            // Then
            assertTrue(foundInstructor.isPresent());
            Instructor instructor = foundInstructor.get();
            
            assertAll("Find instructor by email",
                () -> assertEquals("john.doe@example.com", instructor.getEmail()),
                () -> assertEquals("John", instructor.getFirstName()),
                () -> assertEquals("Doe", instructor.getLastName())
            );
        }

        @Test
        @DisplayName("Should return empty when instructor not found by email")
        void shouldReturnEmptyWhenInstructorNotFoundByEmail() {
            // When
            Optional<Instructor> foundInstructor = instructorRepository.findByEmail("nonexistent@example.com");
            
            // Then
            assertFalse(foundInstructor.isPresent());
        }

        @Test
        @DisplayName("Should check if instructor exists by email")
        void shouldCheckIfInstructorExistsByEmail() {
            // When & Then
            assertTrue(instructorRepository.existsByEmail("john.doe@example.com"));
            assertTrue(instructorRepository.existsByEmail("jane.smith@example.com"));
            assertFalse(instructorRepository.existsByEmail("nonexistent@example.com"));
        }

        @Test
        @DisplayName("Should find instructors by full name containing search term")
        void shouldFindInstructorsByFullNameContaining() {
            // When
            List<Instructor> johnResults = instructorRepository.findByFullNameContaining("John");
            List<Instructor> doeResults = instructorRepository.findByFullNameContaining("Doe");
            List<Instructor> johnDoeResults = instructorRepository.findByFullNameContaining("John Doe");
            List<Instructor> smithResults = instructorRepository.findByFullNameContaining("Smith");
            List<Instructor> nonExistentResults = instructorRepository.findByFullNameContaining("NonExistent");
            
            // Then
            assertAll("Find instructors by full name containing",
                () -> assertEquals(1, johnResults.size()),
                () -> assertEquals("John", johnResults.get(0).getFirstName()),
                
                () -> assertEquals(1, doeResults.size()),
                () -> assertEquals("Doe", doeResults.get(0).getLastName()),
                
                () -> assertEquals(1, johnDoeResults.size()),
                () -> assertEquals("John Doe", johnDoeResults.get(0).getFullName()),
                
                () -> assertEquals(1, smithResults.size()),
                () -> assertEquals("Jane", smithResults.get(0).getFirstName()),
                
                () -> assertEquals(0, nonExistentResults.size())
            );
        }

        @Test
        @DisplayName("Should find instructors by full name containing case insensitive")
        void shouldFindInstructorsByFullNameContainingCaseInsensitive() {
            // When
            List<Instructor> lowerCaseResults = instructorRepository.findByFullNameContaining("john");
            List<Instructor> upperCaseResults = instructorRepository.findByFullNameContaining("JOHN");
            List<Instructor> mixedCaseResults = instructorRepository.findByFullNameContaining("JoHn");
            
            // Then
            assertAll("Case insensitive search",
                () -> assertEquals(1, lowerCaseResults.size()),
                () -> assertEquals("John", lowerCaseResults.get(0).getFirstName()),
                
                () -> assertEquals(1, upperCaseResults.size()),
                () -> assertEquals("John", upperCaseResults.get(0).getFirstName()),
                
                () -> assertEquals(1, mixedCaseResults.size()),
                () -> assertEquals("John", mixedCaseResults.get(0).getFirstName())
            );
        }
    }

    @Nested
    @DisplayName("Instructor Details Filter Queries")
    class InstructorDetailsFilterQueries {

        @Test
        @DisplayName("Should find instructors with details")
        void shouldFindInstructorsWithDetails() {
            // When
            List<Instructor> instructorsWithDetails = instructorRepository.findInstructorsWithDetails();
            
            // Then
            assertAll("Find instructors with details",
                () -> assertEquals(2, instructorsWithDetails.size()),
                () -> assertTrue(instructorsWithDetails.stream().allMatch(i -> i.getInstructorDetails() != null)),
                () -> assertTrue(instructorsWithDetails.stream().anyMatch(i -> "John".equals(i.getFirstName()))),
                () -> assertTrue(instructorsWithDetails.stream().anyMatch(i -> "Jane".equals(i.getFirstName()))),
                () -> assertFalse(instructorsWithDetails.stream().anyMatch(i -> "Bob".equals(i.getFirstName())))
            );
        }

        @Test
        @DisplayName("Should find instructors without details")
        void shouldFindInstructorsWithoutDetails() {
            // When
            List<Instructor> instructorsWithoutDetails = instructorRepository.findInstructorsWithoutDetails();
            
            // Then
            assertAll("Find instructors without details",
                () -> assertEquals(1, instructorsWithoutDetails.size()),
                () -> assertTrue(instructorsWithoutDetails.stream().allMatch(i -> i.getInstructorDetails() == null)),
                () -> assertEquals("Bob", instructorsWithoutDetails.get(0).getFirstName()),
                () -> assertEquals("Johnson", instructorsWithoutDetails.get(0).getLastName())
            );
        }

        @Test
        @DisplayName("Should return empty list when no instructors with details exist")
        void shouldReturnEmptyListWhenNoInstructorsWithDetailsExist() {
            // Given - Remove all instructor details
            instructor1.setInstructorDetails(null);
            instructor2.setInstructorDetails(null);
            instructorRepository.save(instructor1);
            instructorRepository.save(instructor2);
            entityManager.flush();
            entityManager.clear();
            
            // When
            List<Instructor> instructorsWithDetails = instructorRepository.findInstructorsWithDetails();
            
            // Then
            assertTrue(instructorsWithDetails.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list when no instructors without details exist")
        void shouldReturnEmptyListWhenNoInstructorsWithoutDetailsExist() {
            // Given - Add details to instructor3
            InstructorDetails details = new InstructorDetails("https://youtube.com/@bob", "Fishing");
            instructor3.setInstructorDetails(details);
            instructorRepository.save(instructor3);
            entityManager.flush();
            entityManager.clear();
            
            // When
            List<Instructor> instructorsWithoutDetails = instructorRepository.findInstructorsWithoutDetails();
            
            // Then
            assertTrue(instructorsWithoutDetails.isEmpty());
        }
    }

    @Nested
    @DisplayName("Constraint and Validation Tests")
    class ConstraintAndValidationTests {

        @Test
        @DisplayName("Should enforce unique email constraint")
        void shouldEnforceUniqueEmailConstraint() {
            // Given
            Instructor duplicateEmailInstructor = new Instructor("Another", "User", "john.doe@example.com");
            
            // When & Then
            assertThrows(Exception.class, () -> {
                instructorRepository.save(duplicateEmailInstructor);
                entityManager.flush();
            });
        }

        @Test
        @DisplayName("Should handle null instructor details gracefully")
        void shouldHandleNullInstructorDetailsGracefully() {
            // Given
            Instructor instructorWithNullDetails = new Instructor("Test", "User", "test.user@example.com");
            instructorWithNullDetails.setInstructorDetails(null);
            
            // When
            Instructor savedInstructor = instructorRepository.save(instructorWithNullDetails);
            entityManager.flush();
            entityManager.clear();
            
            // Then
            Optional<Instructor> foundInstructor = instructorRepository.findById(savedInstructor.getId());
            assertTrue(foundInstructor.isPresent());
            assertNull(foundInstructor.get().getInstructorDetails());
        }

        @Test
        @DisplayName("Should maintain bidirectional relationship consistency")
        void shouldMaintainBidirectionalRelationshipConsistency() {
            // Given
            InstructorDetails details = new InstructorDetails("https://youtube.com/@test", "Testing");
            Instructor instructor = new Instructor("Test", "User", "test.user@example.com");
            instructor.setInstructorDetails(details);
            
            // When
            Instructor savedInstructor = instructorRepository.save(instructor);
            entityManager.flush();
            entityManager.clear();
            
            // Then
            Optional<Instructor> foundInstructor = instructorRepository.findById(savedInstructor.getId());
            assertTrue(foundInstructor.isPresent());
            
            Instructor retrievedInstructor = foundInstructor.get();
            InstructorDetails retrievedDetails = retrievedInstructor.getInstructorDetails();
            
            assertAll("Bidirectional relationship consistency",
                () -> assertNotNull(retrievedDetails),
                () -> assertEquals(retrievedInstructor, retrievedDetails.getInstructor()),
                () -> assertEquals(retrievedDetails, retrievedInstructor.getInstructorDetails())
            );
        }
    }

    @Nested
    @DisplayName("Performance and Edge Cases")
    class PerformanceAndEdgeCases {

        @Test
        @DisplayName("Should handle large number of instructors efficiently")
        void shouldHandleLargeNumberOfInstructorsEfficiently() {
            // Given - Create multiple instructors
            for (int i = 0; i < 100; i++) {
                Instructor instructor = new Instructor("First" + i, "Last" + i, "email" + i + "@example.com");
                instructorRepository.save(instructor);
            }
            entityManager.flush();
            entityManager.clear();
            
            // When
            List<Instructor> allInstructors = instructorRepository.findAll();
            
            // Then
            assertEquals(103, allInstructors.size()); // 3 original + 100 new
        }

        @Test
        @DisplayName("Should handle empty search results gracefully")
        void shouldHandleEmptySearchResultsGracefully() {
            // When
            List<Instructor> emptyResults = instructorRepository.findByFullNameContaining("NonExistentName");
            
            // Then
            assertNotNull(emptyResults);
            assertTrue(emptyResults.isEmpty());
        }

        @Test
        @DisplayName("Should handle special characters in search")
        void shouldHandleSpecialCharactersInSearch() {
            // Given
            Instructor specialCharInstructor = new Instructor("José", "García-López", "jose@example.com");
            instructorRepository.save(specialCharInstructor);
            entityManager.flush();
            
            // When
            List<Instructor> results = instructorRepository.findByFullNameContaining("José");
            List<Instructor> hyphenResults = instructorRepository.findByFullNameContaining("García-López");
            
            // Then
            assertAll("Special characters in search",
                () -> assertEquals(1, results.size()),
                () -> assertEquals("José", results.get(0).getFirstName()),
                () -> assertEquals(1, hyphenResults.size()),
                () -> assertEquals("García-López", hyphenResults.get(0).getLastName())
            );
        }

        @Test
        @DisplayName("Should handle partial name matches correctly")
        void shouldHandlePartialNameMatchesCorrectly() {
            // When
            List<Instructor> partialFirstName = instructorRepository.findByFullNameContaining("Jo");
            List<Instructor> partialLastName = instructorRepository.findByFullNameContaining("oh");
            List<Instructor> partialFullName = instructorRepository.findByFullNameContaining("n D");
            
            // Then
            assertAll("Partial name matches",
                () -> assertEquals(2, partialFirstName.size()), // John and Johnson
                () -> assertTrue(partialFirstName.stream().anyMatch(i -> "John".equals(i.getFirstName()))),
                () -> assertTrue(partialFirstName.stream().anyMatch(i -> "Johnson".equals(i.getLastName()))),
                
                () -> assertEquals(2, partialLastName.size()), // John and Johnson
                () -> assertTrue(partialLastName.stream().anyMatch(i -> "John".equals(i.getFirstName()))),
                () -> assertTrue(partialLastName.stream().anyMatch(i -> "Johnson".equals(i.getLastName()))),
                
                () -> assertEquals(1, partialFullName.size()), // "John Doe" contains "n D"
                () -> assertEquals("John", partialFullName.get(0).getFirstName())
            );
        }
    }
}