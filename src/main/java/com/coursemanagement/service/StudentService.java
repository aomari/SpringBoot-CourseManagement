package com.coursemanagement.service;

import com.coursemanagement.dto.EnrollmentRequest;
import com.coursemanagement.dto.StudentRequest;
import com.coursemanagement.dto.StudentResponse;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Student operations.
 */
public interface StudentService {

    /**
     * Create new student.
     */
    StudentResponse createStudent(StudentRequest request);

    /**
     * Get student by ID.
     */
    StudentResponse getStudentById(UUID id);

    /**
     * Get student by ID with enrolled courses.
     */
    StudentResponse getStudentByIdWithCourses(UUID id);

    /**
     * Get all students.
     */
    List<StudentResponse> getAllStudents();

    /**
     * Get all students with their enrolled courses.
     */
    List<StudentResponse> getAllStudentsWithCourses();

    /**
     * Update student.
     */
    StudentResponse updateStudent(UUID id, StudentRequest request);

    /**
     * Delete student.
     */
    void deleteStudent(UUID id);

    /**
     * Enroll student in a course.
     */
    void enrollStudentInCourse(UUID studentId, EnrollmentRequest request);

    /**
     * Unenroll student from a course.
     */
    void unenrollStudentFromCourse(UUID studentId, EnrollmentRequest request);

    /**
     * Get courses that a student is enrolled in.
     */
    List<StudentResponse.CourseInfo> getStudentCourses(UUID studentId);

    /**
     * Get students enrolled in a specific course.
     */
    List<StudentResponse> getStudentsEnrolledInCourse(UUID courseId);

    /**
     * Search students by name.
     */
    List<StudentResponse> searchStudentsByName(String name);

    /**
     * Search students by email.
     */
    List<StudentResponse> searchStudentsByEmail(String email);

    /**
     * Get student by email.
     */
    StudentResponse getStudentByEmail(String email);

    /**
     * Get students with no course enrollments.
     */
    List<StudentResponse> getStudentsWithNoCourses();

    /**
     * Get students by instructor (students enrolled in courses taught by the instructor).
     */
    List<StudentResponse> getStudentsByInstructor(UUID instructorId);

    /**
     * Check if student exists by ID.
     */
    boolean existsById(UUID id);

    /**
     * Check if student exists by email.
     */
    boolean existsByEmail(String email);

    /**
     * Check if student is enrolled in a course.
     */
    boolean isStudentEnrolledInCourse(UUID studentId, UUID courseId);

    /**
     * Count students enrolled in a course.
     */
    long countStudentsInCourse(UUID courseId);
}