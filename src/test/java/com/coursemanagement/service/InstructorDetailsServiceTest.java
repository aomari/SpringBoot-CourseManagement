package com.coursemanagement.service;

import com.coursemanagement.dto.InstructorDetailsRequest;
import com.coursemanagement.dto.InstructorDetailsResponse;
import com.coursemanagement.entity.InstructorDetails;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.InstructorDetailsRepository;
import com.coursemanagement.service.impl.InstructorDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InstructorDetailsService Tests")
class InstructorDetailsServiceTest {

    @Mock
    private InstructorDetailsRepository instructorDetailsRepository;

    @InjectMocks
    private InstructorDetailsServiceImpl instructorDetailsService;

    private InstructorDetails testInstructorDetails;
    private InstructorDetailsRequest instructorDetailsRequest;

    @BeforeEach
    void setUp() {
        testInstructorDetails = new InstructorDetails("https://youtube.com/@johndoe", "Playing guitar");
        testInstructorDetails.setId(UUID.randomUUID());

        instructorDetailsRequest = new InstructorDetailsRequest("https://youtube.com/@johndoe", "Playing guitar");
    }

    @Nested
    @DisplayName("Create InstructorDetails Tests")
    class CreateInstructorDetailsTests {

        @Test
        @DisplayName("Should create instructor details successfully")
        void shouldCreateInstructorDetailsSuccessfully() {
            // Given
            when(instructorDetailsRepository.save(any(InstructorDetails.class))).thenReturn(testInstructorDetails);

            // When
            InstructorDetailsResponse result = instructorDetailsService.createInstructorDetails(instructorDetailsRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getYoutubeChannel()).isEqualTo(testInstructorDetails.getYoutubeChannel());
            assertThat(result.getHobby()).isEqualTo(testInstructorDetails.getHobby());

            verify(instructorDetailsRepository).save(any(InstructorDetails.class));
        }

        @Test
        @DisplayName("Should create instructor details with empty hobby successfully")
        void shouldCreateInstructorDetailsWithEmptyHobbySuccessfully() {
            // Given
            InstructorDetailsRequest request = new InstructorDetailsRequest("https://youtube.com/@test", null);
            InstructorDetails details = new InstructorDetails("https://youtube.com/@test", null);
            details.setId(UUID.randomUUID());
            
            when(instructorDetailsRepository.save(any(InstructorDetails.class))).thenReturn(details);

            // When
            InstructorDetailsResponse result = instructorDetailsService.createInstructorDetails(request);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getYoutubeChannel()).isEqualTo("https://youtube.com/@test");
            assertThat(result.getHobby()).isNull();

            verify(instructorDetailsRepository).save(any(InstructorDetails.class));
        }
    }

    @Nested
    @DisplayName("Get InstructorDetails Tests")
    class GetInstructorDetailsTests {

        @Test
        @DisplayName("Should get instructor details by ID successfully")
        void shouldGetInstructorDetailsByIdSuccessfully() {
            // Given
            UUID detailsId = testInstructorDetails.getId();
            when(instructorDetailsRepository.findById(detailsId)).thenReturn(Optional.of(testInstructorDetails));

            // When
            InstructorDetailsResponse result = instructorDetailsService.getInstructorDetailsById(detailsId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testInstructorDetails.getId());
            assertThat(result.getYoutubeChannel()).isEqualTo(testInstructorDetails.getYoutubeChannel());
            assertThat(result.getHobby()).isEqualTo(testInstructorDetails.getHobby());

            verify(instructorDetailsRepository).findById(detailsId);
        }

        @Test
        @DisplayName("Should throw exception when instructor details not found by ID")
        void shouldThrowExceptionWhenInstructorDetailsNotFoundById() {
            // Given
            UUID detailsId = UUID.randomUUID();
            when(instructorDetailsRepository.findById(detailsId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> instructorDetailsService.getInstructorDetailsById(detailsId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("InstructorDetails")
                    .hasMessageContaining("id")
                    .hasMessageContaining(detailsId.toString());

            verify(instructorDetailsRepository).findById(detailsId);
        }

        @Test
        @DisplayName("Should get all instructor details successfully")
        void shouldGetAllInstructorDetailsSuccessfully() {
            // Given
            List<InstructorDetails> detailsList = Arrays.asList(testInstructorDetails);
            when(instructorDetailsRepository.findAll()).thenReturn(detailsList);

            // When
            List<InstructorDetailsResponse> result = instructorDetailsService.getAllInstructorDetails();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testInstructorDetails.getId());
            assertThat(result.get(0).getYoutubeChannel()).isEqualTo(testInstructorDetails.getYoutubeChannel());
            assertThat(result.get(0).getHobby()).isEqualTo(testInstructorDetails.getHobby());

            verify(instructorDetailsRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no instructor details exist")
        void shouldReturnEmptyListWhenNoInstructorDetailsExist() {
            // Given
            when(instructorDetailsRepository.findAll()).thenReturn(Collections.emptyList());

            // When
            List<InstructorDetailsResponse> result = instructorDetailsService.getAllInstructorDetails();

            // Then
            assertThat(result).isEmpty();
            verify(instructorDetailsRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Update InstructorDetails Tests")
    class UpdateInstructorDetailsTests {

        @Test
        @DisplayName("Should update instructor details successfully")
        void shouldUpdateInstructorDetailsSuccessfully() {
            // Given
            UUID detailsId = testInstructorDetails.getId();
            InstructorDetailsRequest updateRequest = new InstructorDetailsRequest(
                "https://youtube.com/@johnupdated", 
                "Updated hobby"
            );
            
            when(instructorDetailsRepository.findById(detailsId)).thenReturn(Optional.of(testInstructorDetails));
            when(instructorDetailsRepository.save(testInstructorDetails)).thenReturn(testInstructorDetails);

            // When
            InstructorDetailsResponse result = instructorDetailsService.updateInstructorDetails(detailsId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(instructorDetailsRepository).findById(detailsId);
            verify(instructorDetailsRepository).save(testInstructorDetails);
        }

        @Test
        @DisplayName("Should throw exception when instructor details not found for update")
        void shouldThrowExceptionWhenInstructorDetailsNotFoundForUpdate() {
            // Given
            UUID detailsId = UUID.randomUUID();
            InstructorDetailsRequest updateRequest = new InstructorDetailsRequest(
                "https://youtube.com/@updated", 
                "Updated hobby"
            );
            
            when(instructorDetailsRepository.findById(detailsId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> instructorDetailsService.updateInstructorDetails(detailsId, updateRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("InstructorDetails")
                    .hasMessageContaining("id")
                    .hasMessageContaining(detailsId.toString());

            verify(instructorDetailsRepository).findById(detailsId);
            verify(instructorDetailsRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should update instructor details with empty hobby successfully")
        void shouldUpdateInstructorDetailsWithEmptyHobbySuccessfully() {
            // Given
            UUID detailsId = testInstructorDetails.getId();
            InstructorDetailsRequest updateRequest = new InstructorDetailsRequest("https://youtube.com/@updated", null);
            
            when(instructorDetailsRepository.findById(detailsId)).thenReturn(Optional.of(testInstructorDetails));
            when(instructorDetailsRepository.save(testInstructorDetails)).thenReturn(testInstructorDetails);

            // When
            InstructorDetailsResponse result = instructorDetailsService.updateInstructorDetails(detailsId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(instructorDetailsRepository).findById(detailsId);
            verify(instructorDetailsRepository).save(testInstructorDetails);
        }
    }

    @Nested
    @DisplayName("Delete InstructorDetails Tests")
    class DeleteInstructorDetailsTests {

        @Test
        @DisplayName("Should delete instructor details successfully")
        void shouldDeleteInstructorDetailsSuccessfully() {
            // Given
            UUID detailsId = testInstructorDetails.getId();
            when(instructorDetailsRepository.existsById(detailsId)).thenReturn(true);

            // When
            instructorDetailsService.deleteInstructorDetails(detailsId);

            // Then
            verify(instructorDetailsRepository).existsById(detailsId);
            verify(instructorDetailsRepository).deleteById(detailsId);
        }

        @Test
        @DisplayName("Should throw exception when instructor details not found for deletion")
        void shouldThrowExceptionWhenInstructorDetailsNotFoundForDeletion() {
            // Given
            UUID detailsId = UUID.randomUUID();
            when(instructorDetailsRepository.existsById(detailsId)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> instructorDetailsService.deleteInstructorDetails(detailsId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("InstructorDetails")
                    .hasMessageContaining("id")
                    .hasMessageContaining(detailsId.toString());

            verify(instructorDetailsRepository).existsById(detailsId);
            verify(instructorDetailsRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Search InstructorDetails Tests")
    class SearchInstructorDetailsTests {

        @Test
        @DisplayName("Should search instructor details by hobby successfully")
        void shouldSearchInstructorDetailsByHobbySuccessfully() {
            // Given
            String hobby = "guitar";
            List<InstructorDetails> detailsList = Arrays.asList(testInstructorDetails);
            when(instructorDetailsRepository.findByHobbyContainingIgnoreCase(hobby)).thenReturn(detailsList);

            // When
            List<InstructorDetailsResponse> result = instructorDetailsService.searchByHobby(hobby);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testInstructorDetails.getId());
            assertThat(result.get(0).getHobby()).isEqualTo(testInstructorDetails.getHobby());

            verify(instructorDetailsRepository).findByHobbyContainingIgnoreCase(hobby);
        }

        @Test
        @DisplayName("Should return empty list when no instructor details match hobby search")
        void shouldReturnEmptyListWhenNoInstructorDetailsMatchHobbySearch() {
            // Given
            String hobby = "nonexistent";
            when(instructorDetailsRepository.findByHobbyContainingIgnoreCase(hobby)).thenReturn(Collections.emptyList());

            // When
            List<InstructorDetailsResponse> result = instructorDetailsService.searchByHobby(hobby);

            // Then
            assertThat(result).isEmpty();
            verify(instructorDetailsRepository).findByHobbyContainingIgnoreCase(hobby);
        }

        @Test
        @DisplayName("Should search instructor details by YouTube channel successfully")
        void shouldSearchInstructorDetailsByYouTubeChannelSuccessfully() {
            // Given
            String channel = "johndoe";
            List<InstructorDetails> detailsList = Arrays.asList(testInstructorDetails);
            when(instructorDetailsRepository.findByYoutubeChannelContainingIgnoreCase(channel)).thenReturn(detailsList);

            // When
            List<InstructorDetailsResponse> result = instructorDetailsService.searchByYoutubeChannel(channel);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(testInstructorDetails.getId());
            assertThat(result.get(0).getYoutubeChannel()).isEqualTo(testInstructorDetails.getYoutubeChannel());

            verify(instructorDetailsRepository).findByYoutubeChannelContainingIgnoreCase(channel);
        }
    }

    @Nested
    @DisplayName("Existence Check Tests")
    class ExistenceCheckTests {

        @Test
        @DisplayName("Should return true when instructor details exist by ID")
        void shouldReturnTrueWhenInstructorDetailsExistById() {
            // Given
            UUID detailsId = testInstructorDetails.getId();
            when(instructorDetailsRepository.existsById(detailsId)).thenReturn(true);

            // When
            boolean result = instructorDetailsService.existsById(detailsId);

            // Then
            assertThat(result).isTrue();
            verify(instructorDetailsRepository).existsById(detailsId);
        }

        @Test
        @DisplayName("Should return false when instructor details do not exist by ID")
        void shouldReturnFalseWhenInstructorDetailsDoNotExistById() {
            // Given
            UUID detailsId = UUID.randomUUID();
            when(instructorDetailsRepository.existsById(detailsId)).thenReturn(false);

            // When
            boolean result = instructorDetailsService.existsById(detailsId);

            // Then
            assertThat(result).isFalse();
            verify(instructorDetailsRepository).existsById(detailsId);
        }
    }

    @Nested
    @DisplayName("Count Tests")
    class CountTests {

        // Note: InstructorDetailsService doesn't have a count method in the interface
        // This test would need to be removed or the method would need to be added to the interface
    }
}