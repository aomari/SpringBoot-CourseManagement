package com.coursemanagement.service.impl;

import com.coursemanagement.dto.EnrollmentRequest;
import com.coursemanagement.dto.StudentRequest;
import com.coursemanagement.dto.StudentResponse;
import com.coursemanagement.entity.Course;
import com.coursemanagement.entity.Student;
import com.coursemanagement.exception.ResourceAlreadyExistsException;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.StudentRepository;
import com.coursemanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for Student operations.
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        // Check if student with same email already exists
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Student", "email", request.getEmail());
        }

        Student student = new Student(request.getFirstName(), request.getLastName(), request.getEmail());
        Student savedStudent = studentRepository.save(student);
        return mapToResponse(savedStudent, false);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        return mapToResponse(student, false);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentByIdWithCourses(UUID id) {
        Student student = studentRepository.findByIdWithCourses(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        return mapToResponse(student, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(student -> mapToResponse(student, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudentsWithCourses() {
        return studentRepository.findAllWithCourses()
                .stream()
                .map(student -> mapToResponse(student, true))
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse updateStudent(UUID id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        // Check if email is being changed and if new email already exists
        if (!student.getEmail().equals(request.getEmail()) && 
            studentRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Student", "email", request.getEmail());
        }

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());

        Student savedStudent = studentRepository.save(student);
        return mapToResponse(savedStudent, false);
    }

    @Override
    public void deleteStudent(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        // Remove student from all courses (clear join table entries)
        student.getCourses().clear();
        studentRepository.save(student);
        
        // Delete the student
        studentRepository.delete(student);
    }

    @Override
    public void enrollStudentInCourse(UUID studentId, EnrollmentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));

        // Check if student is already enrolled
        if (student.isEnrolledInCourse(course)) {
            throw new ResourceAlreadyExistsException("Enrollment", 
                "student " + student.getFullName() + " in course", course.getTitle());
        }

        student.enrollInCourse(course);
        studentRepository.save(student);
    }

    @Override
    public void unenrollStudentFromCourse(UUID studentId, EnrollmentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));

        // Check if student is enrolled
        if (!student.isEnrolledInCourse(course)) {
            throw new ResourceNotFoundException("Enrollment", 
                "student " + student.getFullName() + " in course", course.getTitle());
        }

        student.unenrollFromCourse(course);
        studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse.CourseInfo> getStudentCourses(UUID studentId) {
        Student student = studentRepository.findByIdWithCourses(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        return student.getCourses().stream()
                .map(this::mapToCourseInfo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsEnrolledInCourse(UUID courseId) {
        // Verify course exists
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }

        return studentRepository.findStudentsEnrolledInCourse(courseId)
                .stream()
                .map(student -> mapToResponse(student, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> searchStudentsByName(String name) {
        return studentRepository.findByNameContaining(name)
                .stream()
                .map(student -> mapToResponse(student, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> searchStudentsByEmail(String email) {
        return studentRepository.findByEmailContaining(email)
                .stream()
                .map(student -> mapToResponse(student, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "email", email));
        
        return mapToResponse(student, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsWithNoCourses() {
        return studentRepository.findStudentsWithNoCourses()
                .stream()
                .map(student -> mapToResponse(student, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByInstructor(UUID instructorId) {
        return studentRepository.findStudentsByInstructor(instructorId)
                .stream()
                .map(student -> mapToResponse(student, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return studentRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolledInCourse(UUID studentId, UUID courseId) {
        return studentRepository.isStudentEnrolledInCourse(studentId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countStudentsInCourse(UUID courseId) {
        return studentRepository.countStudentsInCourse(courseId);
    }

    // Private helper methods for mapping

    /**
     * Map Student entity to StudentResponse DTO.
     */
    private StudentResponse mapToResponse(Student student, boolean includeCourses) {
        List<StudentResponse.CourseInfo> courses = null;
        
        if (includeCourses && student.getCourses() != null) {
            courses = student.getCourses().stream()
                    .map(this::mapToCourseInfo)
                    .collect(Collectors.toList());
        }

        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getCreatedAt(),
                student.getUpdatedAt(),
                courses
        );
    }

    /**
     * Map Course entity to CourseInfo DTO.
     */
    private StudentResponse.CourseInfo mapToCourseInfo(Course course) {
        String instructorName = course.getInstructor() != null ? 
                course.getInstructor().getFullName() : "Unknown";
        
        return new StudentResponse.CourseInfo(
                course.getId(),
                course.getTitle(),
                instructorName
        );
    }
}