package com.coursemanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class representing instructor details.
 * Contains additional information about an instructor like YouTube channel and hobby.
 */
@Entity
@Table(name = "instructor_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"instructor"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InstructorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "youtube_channel", nullable = false)
    private String youtubeChannel;

    @Column(name = "hobby")
    private String hobby;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Bidirectional relationship with Instructor
    @OneToOne(mappedBy = "instructorDetails", cascade = CascadeType.ALL)
    private Instructor instructor;

    // Constructor with required fields (excluding id and timestamps)
    public InstructorDetails(String youtubeChannel, String hobby) {
        this.youtubeChannel = youtubeChannel;
        this.hobby = hobby;
    }
}