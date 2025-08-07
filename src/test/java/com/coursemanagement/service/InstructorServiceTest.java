package com.coursemanagement.service;

import com.coursemanagement.dto.InstructorDetailsRequest;
import com.coursemanagement.dto.InstructorRequest;
import com.coursemanagement.dto.InstructorResponse;
import com.coursemanagement.entity.Instructor;
import com.coursemanagement.entity.InstructorDetails;
import com.coursemanagement.exception.ResourceAlreadyExistsException;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.InstructorDetailsRepository;
import com.coursemanagement.repository.InstructorRepository;
import com.coursemanagement.service.impl.InstructorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

/**
 * Unit tests for InstructorService implementation.
 * Tests all CRUD operations and business logic with mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Instructor Service Tests")
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private InstructorDetailsRepository instructorDetailsRepository;

    @InjectMocks
    private InstructorServiceImpl instructorService;

    // Test data
    private UUID instructorId;
    private UUID instructorDetailsId;
    private Instructor instructor;
    private InstructorDetails instructorDetails;
    private InstructorRequest instructorRequest;
    private InstructorDetailsRequest instructorDetailsRequest;

    @BeforeEach
    void setUp() {
        instructorId = UUID.randomUUID();
        instructorDetailsId = UUID.randomUUID();
        
        // Setup instructor details
        instructorDetails = new InstructorDetails("https://youtube.com/@johndoe", "Playing guitar");
        instructorDetails.setId(instructorDetailsId);
        instructorDetails.setCreatedAt(LocalDateTime.now());
        instructorDetails.setUpdatedAt(LocalDateTime.now());
        
        // Setup instructor
        instructor = new Instructor("John", "Doe", "john.doe@example.com");
        instructor.setId(instructorId);
        instructor.setCreatedAt(LocalDateTime.now());
        instructor.setUpdatedAt(LocalDateTime.now());
        instructor.setInstructorDetails(instructorDetails);
        
        // Setup DTOs
        instructorDetailsRequest = new InstructorDetailsRequest("https://youtube.com/@johndoe", "Playing guitar");
        instructorRequest = new InstructorRequest("John", "Doe", "john.doe@example.com", instructorDetailsRequest);
    }

    @Nested
    @DisplayName("Create Instructor Tests")
    class CreateInstructorTests {

        @Test
        @DisplayName("Should create instructor successfully without details")
        void shouldCreateInstructorWithoutDetails() {
            // Given
            InstructorRequest requestWithoutDetails = new InstructorRequest("Jane", "Smith", "jane.smith@example.com");
            Instructor instructorWithoutDetails = new Instructor("Jane", "Smith", "jane.smith@example.com");
            instructorWithoutDetails.setId(UUID.randomUUID());
            instructorWithoutDetails.setCreatedAt(LocalDateTime.now());
            instructorWithoutDetails.setUpdatedAt(LocalDateTime.now());
            
            when(instructorRepository.existsByEmail("jane.smith@example.com")).thenReturn(false);
            when(instructorRepository.save(any(Instructor.class))).thenReturn(instructorWithoutDetails);

            // When
            InstructorResponse response = instructorService.createInstructor(requestWithoutDetails);

            // Then
            assertAll("Instructor creation without details",
                () -> assertNotNull(response),
                () -> assertEquals("Jane", response.getFirstName()),
                () -> assertEquals("Smith", response.getLastName()),
                () -> assertEquals("jane.smith@example.com", response.getEmail()),
                () -> assertEquals("Jane Smith", response.getFullName()),
                () -> assertNull(response.getInstructorDetails())
            );
            
            verify(instructorRepository).existsByEmail("jane.smith@example.com");
            verify(instructorRepository).save(any(Instructor.class));
        }

        @Test
        @DisplayName("Should create instructor successfully with details")
        void shouldCreateInstructorWithDetails() {
            // Given
            when(instructorRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
            when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

            // When
            InstructorResponse response = instructorService.createInstructor(instructorRequest);

            // Then
            assertAll("Instructor creation with details",
                () -> assertNotNull(response),
                () -> assertEquals("John", response.getFirstName()),
                () -> assertEquals("Doe", response.getLastName()),
                () -> assertEquals("john.doe@example.com", response.getEmail()),
                () -> assertEquals("John Doe", response.getFullName()),
                () -> assertNotNull(response.getInstructorDetails()),
                () -> assertEquals("https://youtube.com/@johndoe", response.getInstructorDetails().getYoutubeChannel()),
                () -> assertEquals("Playing guitar", response.getInstructorDetails().getHobby())
            );
            
            verify(instructorRepository).existsByEmail("john.doe@example.com");
            verify(instructorRepository).save(any(Instructor.class));
        }

        @Test
        @DisplayName("Should throw exception when instructor with email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // Given
            when(instructorRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

            // When & Then
            ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> instructorService.createInstructor(instructorRequest)
            );

            assertEquals("Instructor already exists with email: john.doe@example.com", exception.getMessage());
            verify(instructorRepository).existsByEmail("john.doe@example.com");
            verify(instructorRepository, never()).save(any(Instructor.class));
        }
    }

    @Nested
    @DisplayName("Get Instructor Tests")
    class GetInstructorTests {

        @Test
        @DisplayName("Should get instructor by ID successfully")
        void shouldGetInstructorById() {
            // Given
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

            // When
            InstructorResponse response = instructorService.getInstructorById(instructorId);

            // Then
            assertAll("Get instructor by ID",
                () -> assertNotNull(response),
                () -> assertEquals(instructorId, response.getId()),
                () -> assertEquals("John", response.getFirstName()),
                () -> assertEquals("Doe", response.getLastName()),
                () -> assertEquals("john.doe@example.com", response.getEmail()),
                () -> assertNotNull(response.getInstructorDetails())
            );
            
            verify(instructorRepository).findById(instructorId);
        }

        @Test
        @DisplayName("Should throw exception when instructor not found by ID")
        void shouldThrowExceptionWhenInstructorNotFoundById() {
            // Given
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> instructorService.getInstructorById(instructorId)
            );

            assertTrue(exception.getMessage().contains("Instructor not found"));
            verify(instructorRepository).findById(instructorId);
        }

        @Test
        @DisplayName("Should get instructor by email successfully")
        void shouldGetInstructorByEmail() {
            // Given
            when(instructorRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(instructor));

            // When
            InstructorResponse response = instructorService.getInstructorByEmail("john.doe@example.com");

            // Then
            assertAll("Get instructor by email",
                () -> assertNotNull(response),
                () -> assertEquals("john.doe@example.com", response.getEmail()),
                () -> assertEquals("John", response.getFirstName()),
                () -> assertEquals("Doe", response.getLastName())
            );
            
            verify(instructorRepository).findByEmail("john.doe@example.com");
        }

        @Test
        @DisplayName("Should throw exception when instructor not found by email")
        void shouldThrowExceptionWhenInstructorNotFoundByEmail() {
            // Given
            String email = "nonexistent@example.com";
            when(instructorRepository.findByEmail(email)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> instructorService.getInstructorByEmail(email)
            );

            assertTrue(exception.getMessage().contains("Instructor not found"));
            verify(instructorRepository).findByEmail(email);
        }

        @Test
        @DisplayName("Should get all instructors successfully")
        void shouldGetAllInstructors() {
            // Given
            Instructor instructor2 = new Instructor("Jane", "Smith", "jane.smith@example.com");
            instructor2.setId(UUID.randomUUID());
            instructor2.setCreatedAt(LocalDateTime.now());
            instructor2.setUpdatedAt(LocalDateTime.now());
            
            List<Instructor> instructors = Arrays.asList(instructor, instructor2);
            when(instructorRepository.findAll()).thenReturn(instructors);

            // When
            List<InstructorResponse> responses = instructorService.getAllInstructors();

            // Then
            assertAll("Get all instructors",
                () -> assertNotNull(responses),
                () -> assertEquals(2, responses.size()),
                () -> assertEquals("John", responses.get(0).getFirstName()),
                () -> assertEquals("Jane", responses.get(1).getFirstName())
            );
            
            verify(instructorRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Update Instructor Tests")
    class UpdateInstructorTests {

        @Test
        @DisplayName("Should update instructor successfully")
        void shouldUpdateInstructorSuccessfully() {
            // Given
            InstructorRequest updateRequest = new InstructorRequest("John Updated", "Doe Updated", "john.updated@example.com");
            Instructor updatedInstructor = new Instructor("John Updated", "Doe Updated", "john.updated@example.com");
            updatedInstructor.setId(instructorId);
            updatedInstructor.setCreatedAt(instructor.getCreatedAt());
            updatedInstructor.setUpdatedAt(LocalDateTime.now());
            
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
            when(instructorRepository.existsByEmail("john.updated@example.com")).thenReturn(false);
            when(instructorRepository.save(any(Instructor.class))).thenReturn(updatedInstructor);

            // When
            InstructorResponse response = instructorService.updateInstructor(instructorId, updateRequest);

            // Then
            assertAll("Update instructor",
                () -> assertNotNull(response),
                () -> assertEquals("John Updated", response.getFirstName()),
                () -> assertEquals("Doe Updated", response.getLastName()),
                () -> assertEquals("john.updated@example.com", response.getEmail())
            );
            
            verify(instructorRepository).findById(instructorId);
            verify(instructorRepository).existsByEmail("john.updated@example.com");
            verify(instructorRepository).save(any(Instructor.class));
        }

        @Test
        @DisplayName("Should update instructor with new details")
        void shouldUpdateInstructorWithNewDetails() {
            // Given
            Instructor instructorWithoutDetails = new Instructor("John", "Doe", "john.doe@example.com");
            instructorWithoutDetails.setId(instructorId);
            instructorWithoutDetails.setCreatedAt(LocalDateTime.now());
            instructorWithoutDetails.setUpdatedAt(LocalDateTime.now());
            
            InstructorDetailsRequest newDetailsRequest = new InstructorDetailsRequest("https://youtube.com/@newchannel", "New hobby");
            InstructorRequest updateRequest = new InstructorRequest("John", "Doe", "john.doe@example.com", newDetailsRequest);
            
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructorWithoutDetails));
            when(instructorRepository.save(any(Instructor.class))).thenReturn(instructorWithoutDetails);

            // When
            InstructorResponse response = instructorService.updateInstructor(instructorId, updateRequest);

            // Then
            assertNotNull(response);
            verify(instructorRepository).findById(instructorId);
            verify(instructorRepository).save(any(Instructor.class));
        }

        @Test
        @DisplayName("Should throw exception when updating to existing email")
        void shouldThrowExceptionWhenUpdatingToExistingEmail() {
            // Given
            InstructorRequest updateRequest = new InstructorRequest("John", "Doe", "existing@example.com");
            
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
            when(instructorRepository.existsByEmail("existing@example.com")).thenReturn(true);

            // When & Then
            ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> instructorService.updateInstructor(instructorId, updateRequest)
            );

            assertTrue(exception.getMessage().contains("Instructor already exists with email: existing@example.com"));
            verify(instructorRepository).findById(instructorId);
            verify(instructorRepository).existsByEmail("existing@example.com");
            verify(instructorRepository, never()).save(any(Instructor.class));
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent instructor")
        void shouldThrowExceptionWhenUpdatingNonExistentInstructor() {
            // Given
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> instructorService.updateInstructor(instructorId, instructorRequest)
            );

            assertTrue(exception.getMessage().contains("Instructor not found"));
            verify(instructorRepository).findById(instructorId);
            verify(instructorRepository, never()).save(any(Instructor.class));
        }
    }

    @Nested
    @DisplayName("Delete Instructor Tests")
    class DeleteInstructorTests {

        @Test
        @DisplayName("Should delete instructor successfully")
        void shouldDeleteInstructorSuccessfully() {
            // Given
            when(instructorRepository.existsById(instructorId)).thenReturn(true);
            doNothing().when(instructorRepository).deleteById(instructorId);

            // When
            assertDoesNotThrow(() -> instructorService.deleteInstructor(instructorId));

            // Then
            verify(instructorRepository).existsById(instructorId);
            verify(instructorRepository).deleteById(instructorId);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent instructor")
        void shouldThrowExceptionWhenDeletingNonExistentInstructor() {
            // Given
            when(instructorRepository.existsById(instructorId)).thenReturn(false);

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> instructorService.deleteInstructor(instructorId)
            );

            assertTrue(exception.getMessage().contains("Instructor not found"));
            verify(instructorRepository).existsById(instructorId);
            verify(instructorRepository, never()).deleteById(instructorId);
        }
    }

    @Nested
    @DisplayName("Search and Filter Tests")
    class SearchAndFilterTests {

        @Test
        @DisplayName("Should search instructors by name successfully")
        void shouldSearchInstructorsByName() {
            // Given
            List<Instructor> searchResults = Arrays.asList(instructor);
            when(instructorRepository.findByFullNameContaining("John")).thenReturn(searchResults);

            // When
            List<InstructorResponse> responses = instructorService.searchInstructorsByName("John");

            // Then
            assertAll("Search instructors by name",
                () -> assertNotNull(responses),
                () -> assertEquals(1, responses.size()),
                () -> assertEquals("John", responses.get(0).getFirstName())
            );
            
            verify(instructorRepository).findByFullNameContaining("John");
        }

        @Test
        @DisplayName("Should get instructors with details successfully")
        void shouldGetInstructorsWithDetails() {
            // Given
            List<Instructor> instructorsWithDetails = Arrays.asList(instructor);
            when(instructorRepository.findInstructorsWithDetails()).thenReturn(instructorsWithDetails);

            // When
            List<InstructorResponse> responses = instructorService.getInstructorsWithDetails();

            // Then
            assertAll("Get instructors with details",
                () -> assertNotNull(responses),
                () -> assertEquals(1, responses.size()),
                () -> assertNotNull(responses.get(0).getInstructorDetails())
            );
            
            verify(instructorRepository).findInstructorsWithDetails();
        }

        @Test
        @DisplayName("Should get instructors without details successfully")
        void shouldGetInstructorsWithoutDetails() {
            // Given
            Instructor instructorWithoutDetails = new Instructor("Jane", "Smith", "jane.smith@example.com");
            instructorWithoutDetails.setId(UUID.randomUUID());
            instructorWithoutDetails.setCreatedAt(LocalDateTime.now());
            instructorWithoutDetails.setUpdatedAt(LocalDateTime.now());
            
            List<Instructor> instructorsWithoutDetails = Arrays.asList(instructorWithoutDetails);
            when(instructorRepository.findInstructorsWithoutDetails()).thenReturn(instructorsWithoutDetails);

            // When
            List<InstructorResponse> responses = instructorService.getInstructorsWithoutDetails();

            // Then
            assertAll("Get instructors without details",
                () -> assertNotNull(responses),
                () -> assertEquals(1, responses.size()),
                () -> assertNull(responses.get(0).getInstructorDetails())
            );
            
            verify(instructorRepository).findInstructorsWithoutDetails();
        }
    }

    @Nested
    @DisplayName("Instructor Details Management Tests")
    class InstructorDetailsManagementTests {

        @Test
        @DisplayName("Should add instructor details successfully")
        void shouldAddInstructorDetailsSuccessfully() {
            // Given
            Instructor instructorWithoutDetails = new Instructor("John", "Doe", "john.doe@example.com");
            instructorWithoutDetails.setId(instructorId);
            instructorWithoutDetails.setCreatedAt(LocalDateTime.now());
            instructorWithoutDetails.setUpdatedAt(LocalDateTime.now());
            
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructorWithoutDetails));
            when(instructorDetailsRepository.findById(instructorDetailsId)).thenReturn(Optional.of(instructorDetails));
            when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

            // When
            InstructorResponse response = instructorService.addInstructorDetails(instructorId, instructorDetailsId);

            // Then
            assertAll("Add instructor details",
                () -> assertNotNull(response),
                () -> assertNotNull(response.getInstructorDetails()),
                () -> assertEquals("https://youtube.com/@johndoe", response.getInstructorDetails().getYoutubeChannel())
            );
            
            verify(instructorRepository).findById(instructorId);
            verify(instructorDetailsRepository).findById(instructorDetailsId);
            verify(instructorRepository).save(any(Instructor.class));
        }

        @Test
        @DisplayName("Should remove instructor details successfully")
        void shouldRemoveInstructorDetailsSuccessfully() {
            // Given
            Instructor updatedInstructor = new Instructor("John", "Doe", "john.doe@example.com");
            updatedInstructor.setId(instructorId);
            updatedInstructor.setCreatedAt(instructor.getCreatedAt());
            updatedInstructor.setUpdatedAt(LocalDateTime.now());
            
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
            when(instructorRepository.save(any(Instructor.class))).thenReturn(updatedInstructor);

            // When
            InstructorResponse response = instructorService.removeInstructorDetails(instructorId);

            // Then
            assertAll("Remove instructor details",
                () -> assertNotNull(response),
                () -> assertNull(response.getInstructorDetails())
            );
            
            verify(instructorRepository).findById(instructorId);
            verify(instructorRepository).save(any(Instructor.class));
        }

        @Test
        @DisplayName("Should throw exception when adding details to non-existent instructor")
        void shouldThrowExceptionWhenAddingDetailsToNonExistentInstructor() {
            // Given
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> instructorService.addInstructorDetails(instructorId, instructorDetailsId)
            );

            assertTrue(exception.getMessage().contains("Instructor not found"));
            verify(instructorRepository).findById(instructorId);
            verify(instructorDetailsRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should throw exception when adding non-existent details")
        void shouldThrowExceptionWhenAddingNonExistentDetails() {
            // Given
            when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
            when(instructorDetailsRepository.findById(instructorDetailsId)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> instructorService.addInstructorDetails(instructorId, instructorDetailsId)
            );

            assertTrue(exception.getMessage().contains("InstructorDetails not found"));
            verify(instructorRepository).findById(instructorId);
            verify(instructorDetailsRepository).findById(instructorDetailsId);
        }
    }

    @Nested
    @DisplayName("Existence Check Tests")
    class ExistenceCheckTests {

        @Test
        @DisplayName("Should return true when instructor exists by ID")
        void shouldReturnTrueWhenInstructorExistsById() {
            // Given
            when(instructorRepository.existsById(instructorId)).thenReturn(true);

            // When
            boolean exists = instructorService.existsById(instructorId);

            // Then
            assertTrue(exists);
            verify(instructorRepository).existsById(instructorId);
        }

        @Test
        @DisplayName("Should return false when instructor does not exist by ID")
        void shouldReturnFalseWhenInstructorDoesNotExistById() {
            // Given
            when(instructorRepository.existsById(instructorId)).thenReturn(false);

            // When
            boolean exists = instructorService.existsById(instructorId);

            // Then
            assertFalse(exists);
            verify(instructorRepository).existsById(instructorId);
        }

        @Test
        @DisplayName("Should return true when instructor exists by email")
        void shouldReturnTrueWhenInstructorExistsByEmail() {
            // Given
            when(instructorRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

            // When
            boolean exists = instructorService.existsByEmail("john.doe@example.com");

            // Then
            assertTrue(exists);
            verify(instructorRepository).existsByEmail("john.doe@example.com");
        }

        @Test
        @DisplayName("Should return false when instructor does not exist by email")
        void shouldReturnFalseWhenInstructorDoesNotExistByEmail() {
            // Given
            when(instructorRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

            // When
            boolean exists = instructorService.existsByEmail("nonexistent@example.com");

            // Then
            assertFalse(exists);
            verify(instructorRepository).existsByEmail("nonexistent@example.com");
        }
    }
}