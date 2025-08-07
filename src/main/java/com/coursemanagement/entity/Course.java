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
 * Entity class representing a course.
 * Has a many-to-one relationship with Instructor, one-to-many relationship with Review,
 * and many-to-many relationship with Student.
 */
@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"reviews", "students"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Many-to-one relationship with Instructor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    // One-to-many relationship with Review
    // DO NOT cascade delete from Course to Review in entity level
    // The CASCADE DELETE is handled at database level
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    // Many-to-many relationship with Student
    // No cascade operations - removing a course doesn't delete students, and vice versa
    // Only the join table entries are managed
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    // Constructor with required fields (excluding id and timestamps)
    public Course(String title, Instructor instructor) {
        this.title = title;
        this.instructor = instructor;
    }

    // Helper method to add a review
    public void addReview(Review review) {
        reviews.add(review);
        review.setCourse(this);
    }

    // Helper method to remove a review
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setCourse(null);
    }

    // Helper method to enroll a student
    public void enrollStudent(Student student) {
        if (!this.students.contains(student)) {
            this.students.add(student);
            student.getCourses().add(this);
        }
    }

    // Helper method to unenroll a student
    public void unenrollStudent(Student student) {
        if (this.students.contains(student)) {
            this.students.remove(student);
            student.getCourses().remove(this);
        }
    }

    // Helper method to check if student is enrolled
    public boolean hasStudent(Student student) {
        return this.students.contains(student);
    }
}