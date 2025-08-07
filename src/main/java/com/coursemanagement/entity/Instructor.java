package com.coursemanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity class representing an instructor.
 * Has a one-to-one relationship with InstructorDetails (optional).
 */
@Entity
@Table(name = "instructor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"courses"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // One-to-one relationship with InstructorDetails (optional)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_details_id", referencedColumnName = "id")
    private InstructorDetails instructorDetails;

    // One-to-many relationship with Course
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    // Constructor with required fields (excluding id and timestamps)
    public Instructor(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Constructor with all fields (excluding id and timestamps)
    public Instructor(String firstName, String lastName, String email, InstructorDetails instructorDetails) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.instructorDetails = instructorDetails;
    }

    // Custom setter for instructorDetails to maintain bidirectional relationship
    public void setInstructorDetails(InstructorDetails instructorDetails) {
        this.instructorDetails = instructorDetails;
        // Set bidirectional relationship
        if (instructorDetails != null) {
            instructorDetails.setInstructor(this);
        }
    }

    // Helper method to add a course
    public void addCourse(Course course) {
        courses.add(course);
        course.setInstructor(this);
    }

    // Helper method to remove a course
    public void removeCourse(Course course) {
        courses.remove(course);
        course.setInstructor(null);
    }

    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}