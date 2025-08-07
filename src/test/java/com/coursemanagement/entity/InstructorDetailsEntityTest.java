package com.coursemanagement.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("InstructorDetails Entity Tests")
class InstructorDetailsEntityTest {

    private InstructorDetails instructorDetails;
    private Instructor instructor;

    @BeforeEach
    void setUp() {
        instructorDetails = new InstructorDetails("https://youtube.com/@johndoe", "Playing guitar");
        instructorDetails.setId(UUID.randomUUID());
        
        instructor = new Instructor("John", "Doe", "john.doe@example.com");
        instructor.setId(UUID.randomUUID());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instructor details with default constructor")
        void shouldCreateInstructorDetailsWithDefaultConstructor() {
            // When
            InstructorDetails newDetails = new InstructorDetails();

            // Then
            assertThat(newDetails).isNotNull();
            assertThat(newDetails.getId()).isNull();
            assertThat(newDetails.getYoutubeChannel()).isNull();
            assertThat(newDetails.getHobby()).isNull();
            assertThat(newDetails.getInstructor()).isNull();
            assertThat(newDetails.getCreatedAt()).isNull();
            assertThat(newDetails.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("Should create instructor details with parameterized constructor")
        void shouldCreateInstructorDetailsWithParameterizedConstructor() {
            // When
            InstructorDetails newDetails = new InstructorDetails("https://youtube.com/@janedoe", "Photography");

            // Then
            assertThat(newDetails).isNotNull();
            assertThat(newDetails.getYoutubeChannel()).isEqualTo("https://youtube.com/@janedoe");
            assertThat(newDetails.getHobby()).isEqualTo("Photography");
            assertThat(newDetails.getInstructor()).isNull();
        }

        @Test
        @DisplayName("Should create instructor details with null values")
        void shouldCreateInstructorDetailsWithNullValues() {
            // When
            InstructorDetails newDetails = new InstructorDetails(null, null);

            // Then
            assertThat(newDetails).isNotNull();
            assertThat(newDetails.getYoutubeChannel()).isNull();
            assertThat(newDetails.getHobby()).isNull();
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get ID")
        void shouldSetAndGetId() {
            // Given
            UUID id = UUID.randomUUID();

            // When
            instructorDetails.setId(id);

            // Then
            assertThat(instructorDetails.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should set and get YouTube channel")
        void shouldSetAndGetYoutubeChannel() {
            // When
            instructorDetails.setYoutubeChannel("https://youtube.com/@newchannel");

            // Then
            assertThat(instructorDetails.getYoutubeChannel()).isEqualTo("https://youtube.com/@newchannel");
        }

        @Test
        @DisplayName("Should set and get YouTube channel with null")
        void shouldSetAndGetYoutubeChannelWithNull() {
            // When
            instructorDetails.setYoutubeChannel(null);

            // Then
            assertThat(instructorDetails.getYoutubeChannel()).isNull();
        }

        @Test
        @DisplayName("Should set and get YouTube channel with empty string")
        void shouldSetAndGetYoutubeChannelWithEmptyString() {
            // When
            instructorDetails.setYoutubeChannel("");

            // Then
            assertThat(instructorDetails.getYoutubeChannel()).isEqualTo("");
        }

        @Test
        @DisplayName("Should set and get hobby")
        void shouldSetAndGetHobby() {
            // When
            instructorDetails.setHobby("Reading books");

            // Then
            assertThat(instructorDetails.getHobby()).isEqualTo("Reading books");
        }

        @Test
        @DisplayName("Should set and get hobby with null")
        void shouldSetAndGetHobbyWithNull() {
            // When
            instructorDetails.setHobby(null);

            // Then
            assertThat(instructorDetails.getHobby()).isNull();
        }

        @Test
        @DisplayName("Should set and get hobby with empty string")
        void shouldSetAndGetHobbyWithEmptyString() {
            // When
            instructorDetails.setHobby("");

            // Then
            assertThat(instructorDetails.getHobby()).isEqualTo("");
        }

        @Test
        @DisplayName("Should set and get created at")
        void shouldSetAndGetCreatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            instructorDetails.setCreatedAt(now);

            // Then
            assertThat(instructorDetails.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get updated at")
        void shouldSetAndGetUpdatedAt() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            // When
            instructorDetails.setUpdatedAt(now);

            // Then
            assertThat(instructorDetails.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set and get instructor")
        void shouldSetAndGetInstructor() {
            // When
            instructorDetails.setInstructor(instructor);

            // Then
            assertThat(instructorDetails.getInstructor()).isEqualTo(instructor);
        }

        @Test
        @DisplayName("Should set and get instructor with null")
        void shouldSetAndGetInstructorWithNull() {
            // Given
            instructorDetails.setInstructor(instructor);
            assertThat(instructorDetails.getInstructor()).isNotNull();

            // When
            instructorDetails.setInstructor(null);

            // Then
            assertThat(instructorDetails.getInstructor()).isNull();
        }
    }

    @Nested
    @DisplayName("Object Methods Tests")
    class ObjectMethodsTests {

        @Test
        @DisplayName("Should generate toString correctly")
        void shouldGenerateToStringCorrectly() {
            // Given
            instructorDetails.setCreatedAt(LocalDateTime.of(2023, 1, 1, 10, 0));
            instructorDetails.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 10, 0));

            // When
            String toString = instructorDetails.toString();

            // Then
            assertThat(toString).contains("InstructorDetails(");
            assertThat(toString).contains("youtubeChannel=https://youtube.com/@johndoe");
            assertThat(toString).contains("hobby=Playing guitar");
        }

        @Test
        @DisplayName("Should generate toString with null values")
        void shouldGenerateToStringWithNullValues() {
            // Given
            InstructorDetails nullDetails = new InstructorDetails(null, null);

            // When
            String toString = nullDetails.toString();

            // Then
            assertThat(toString).contains("InstructorDetails(");
            assertThat(toString).contains("youtubeChannel=null");
            assertThat(toString).contains("hobby=null");
        }

        @Test
        @DisplayName("Should generate toString with empty values")
        void shouldGenerateToStringWithEmptyValues() {
            // Given
            InstructorDetails emptyDetails = new InstructorDetails("", "");

            // When
            String toString = emptyDetails.toString();

            // Then
            assertThat(toString).contains("InstructorDetails(");
            assertThat(toString).contains("youtubeChannel=");
            assertThat(toString).contains("hobby=");
        }
    }

    @Nested
    @DisplayName("Relationship Tests")
    class RelationshipTests {

        @Test
        @DisplayName("Should maintain bidirectional relationship with instructor")
        void shouldMaintainBidirectionalRelationshipWithInstructor() {
            // When
            instructorDetails.setInstructor(instructor);
            instructor.setInstructorDetails(instructorDetails);

            // Then
            assertThat(instructorDetails.getInstructor()).isEqualTo(instructor);
            assertThat(instructor.getInstructorDetails()).isEqualTo(instructorDetails);
        }

        @Test
        @DisplayName("Should handle changing instructor")
        void shouldHandleChangingInstructor() {
            // Given
            Instructor newInstructor = new Instructor("Jane", "Smith", "jane.smith@example.com");
            instructorDetails.setInstructor(instructor);

            // When
            instructorDetails.setInstructor(newInstructor);

            // Then
            assertThat(instructorDetails.getInstructor()).isEqualTo(newInstructor);
            assertThat(instructorDetails.getInstructor()).isNotEqualTo(instructor);
        }

        @Test
        @DisplayName("Should handle removing instructor relationship")
        void shouldHandleRemovingInstructorRelationship() {
            // Given
            instructorDetails.setInstructor(instructor);
            assertThat(instructorDetails.getInstructor()).isNotNull();

            // When
            instructorDetails.setInstructor(null);

            // Then
            assertThat(instructorDetails.getInstructor()).isNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long YouTube channel URL")
        void shouldHandleVeryLongYoutubeChannelUrl() {
            // Given
            String longUrl = "https://youtube.com/@" + "a".repeat(500);

            // When
            instructorDetails.setYoutubeChannel(longUrl);

            // Then
            assertThat(instructorDetails.getYoutubeChannel()).isEqualTo(longUrl);
            assertThat(instructorDetails.getYoutubeChannel()).hasSize(longUrl.length());
        }

        @Test
        @DisplayName("Should handle very long hobby description")
        void shouldHandleVeryLongHobbyDescription() {
            // Given
            String longHobby = "Playing guitar and singing and composing music and " + "performing ".repeat(50);

            // When
            instructorDetails.setHobby(longHobby);

            // Then
            assertThat(instructorDetails.getHobby()).isEqualTo(longHobby);
        }

        @Test
        @DisplayName("Should handle special characters in YouTube channel")
        void shouldHandleSpecialCharactersInYoutubeChannel() {
            // Given
            String specialChannel = "https://youtube.com/@john_doe-123";

            // When
            instructorDetails.setYoutubeChannel(specialChannel);

            // Then
            assertThat(instructorDetails.getYoutubeChannel()).isEqualTo(specialChannel);
        }

        @Test
        @DisplayName("Should handle special characters in hobby")
        void shouldHandleSpecialCharactersInHobby() {
            // Given
            String specialHobby = "Playing guitar 🎸 & singing 🎤 (rock music)";

            // When
            instructorDetails.setHobby(specialHobby);

            // Then
            assertThat(instructorDetails.getHobby()).isEqualTo(specialHobby);
        }

        @Test
        @DisplayName("Should handle hobby with line breaks")
        void shouldHandleHobbyWithLineBreaks() {
            // Given
            String multilineHobby = "Playing guitar\nComposing music\nPerforming live";

            // When
            instructorDetails.setHobby(multilineHobby);

            // Then
            assertThat(instructorDetails.getHobby()).isEqualTo(multilineHobby);
            assertThat(instructorDetails.getHobby()).contains("\n");
        }

        @Test
        @DisplayName("Should handle timestamps correctly")
        void shouldHandleTimestampsCorrectly() {
            // Given
            LocalDateTime createdTime = LocalDateTime.of(2023, 1, 1, 10, 0, 0);
            LocalDateTime updatedTime = LocalDateTime.of(2023, 1, 2, 15, 30, 45);

            // When
            instructorDetails.setCreatedAt(createdTime);
            instructorDetails.setUpdatedAt(updatedTime);

            // Then
            assertThat(instructorDetails.getCreatedAt()).isEqualTo(createdTime);
            assertThat(instructorDetails.getUpdatedAt()).isEqualTo(updatedTime);
            assertThat(instructorDetails.getUpdatedAt()).isAfter(instructorDetails.getCreatedAt());
        }

        @Test
        @DisplayName("Should handle non-standard YouTube URL formats")
        void shouldHandleNonStandardYoutubeUrlFormats() {
            // Given
            String[] urlFormats = {
                "youtube.com/@johndoe",
                "www.youtube.com/@johndoe",
                "http://youtube.com/@johndoe",
                "https://www.youtube.com/@johndoe",
                "@johndoe"
            };

            // When & Then
            for (String url : urlFormats) {
                instructorDetails.setYoutubeChannel(url);
                assertThat(instructorDetails.getYoutubeChannel()).isEqualTo(url);
            }
        }
    }
}