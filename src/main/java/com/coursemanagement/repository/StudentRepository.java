package com.coursemanagement.repository;

import com.coursemanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Student entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    /**
     * Find student by email (since email is unique).
     */
    Optional<Student> findByEmail(String email);

    /**
     * Check if student exists by email.
     */
    boolean existsByEmail(String email);

    /**
     * Find students by first or last name containing keyword (case-insensitive).
     */
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByNameContaining(@Param("name") String name);

    /**
     * Find student by ID with enrolled courses.
     */
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses c LEFT JOIN FETCH c.instructor WHERE s.id = :studentId")
    Optional<Student> findByIdWithCourses(@Param("studentId") UUID studentId);

    /**
     * Find all students with their enrolled courses.
     */
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses c LEFT JOIN FETCH c.instructor")
    List<Student> findAllWithCourses();

    /**
     * Find students enrolled in a specific course.
     */
    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId")
    List<Student> findStudentsEnrolledInCourse(@Param("courseId") UUID courseId);

    /**
     * Find students not enrolled in a specific course.
     */
    @Query("SELECT s FROM Student s WHERE s.id NOT IN " +
           "(SELECT st.id FROM Student st JOIN st.courses c WHERE c.id = :courseId)")
    List<Student> findStudentsNotEnrolledInCourse(@Param("courseId") UUID courseId);

    /**
     * Check if student is enrolled in a specific course.
     */
    @Query("SELECT COUNT(s) > 0 FROM Student s JOIN s.courses c WHERE s.id = :studentId AND c.id = :courseId")
    boolean isStudentEnrolledInCourse(@Param("studentId") UUID studentId, @Param("courseId") UUID courseId);

    /**
     * Count students enrolled in a specific course.
     */
    @Query("SELECT COUNT(s) FROM Student s JOIN s.courses c WHERE c.id = :courseId")
    long countStudentsInCourse(@Param("courseId") UUID courseId);

    /**
     * Find students by instructor (students enrolled in courses taught by the instructor).
     */
    @Query("SELECT DISTINCT s FROM Student s JOIN s.courses c WHERE c.instructor.id = :instructorId")
    List<Student> findStudentsByInstructor(@Param("instructorId") UUID instructorId);

    /**
     * Search students by email containing keyword (case-insensitive).
     */
    @Query("SELECT s FROM Student s WHERE LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Student> findByEmailContaining(@Param("email") String email);

    /**
     * Find students with no course enrollments.
     */
    @Query("SELECT s FROM Student s WHERE s.courses IS EMPTY")
    List<Student> findStudentsWithNoCourses();

    /**
     * Find students enrolled in multiple courses (more than specified count).
     */
    @Query("SELECT s FROM Student s WHERE SIZE(s.courses) > :minCourseCount")
    List<Student> findStudentsWithMoreThanNCourses(@Param("minCourseCount") int minCourseCount);
}