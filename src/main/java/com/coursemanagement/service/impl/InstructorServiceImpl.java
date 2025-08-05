package com.coursemanagement.service.impl;

import com.coursemanagement.dto.InstructorDetailsResponse;
import com.coursemanagement.dto.InstructorRequest;
import com.coursemanagement.dto.InstructorResponse;
import com.coursemanagement.entity.Instructor;
import com.coursemanagement.entity.InstructorDetails;
import com.coursemanagement.exception.ResourceAlreadyExistsException;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.InstructorDetailsRepository;
import com.coursemanagement.repository.InstructorRepository;
import com.coursemanagement.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for Instructor operations.
 */
@Service
@Transactional
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorDetailsRepository instructorDetailsRepository;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository,
                               InstructorDetailsRepository instructorDetailsRepository) {
        this.instructorRepository = instructorRepository;
        this.instructorDetailsRepository = instructorDetailsRepository;
    }

    @Override
    public InstructorResponse createInstructor(InstructorRequest request) {
        // Check if instructor with email already exists
        if (instructorRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Instructor", "email", request.getEmail());
        }

        Instructor instructor = new Instructor(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()
        );

        // Handle instructor details if provided
        if (request.getInstructorDetails() != null) {
            InstructorDetails instructorDetails = new InstructorDetails(
                    request.getInstructorDetails().getYoutubeChannel(),
                    request.getInstructorDetails().getHobby()
            );
            instructor.setInstructorDetails(instructorDetails);
        }

        Instructor savedInstructor = instructorRepository.save(instructor);
        return mapToResponse(savedInstructor);
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponse getInstructorById(UUID id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", id));
        
        return mapToResponse(instructor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorResponse> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InstructorResponse updateInstructor(UUID id, InstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", id));

        // Check if email is being changed and if new email already exists
        if (!instructor.getEmail().equals(request.getEmail()) && 
            instructorRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Instructor", "email", request.getEmail());
        }

        instructor.setFirstName(request.getFirstName());
        instructor.setLastName(request.getLastName());
        instructor.setEmail(request.getEmail());

        // Handle instructor details update
        if (request.getInstructorDetails() != null) {
            if (instructor.getInstructorDetails() != null) {
                // Update existing instructor details
                instructor.getInstructorDetails().setYoutubeChannel(request.getInstructorDetails().getYoutubeChannel());
                instructor.getInstructorDetails().setHobby(request.getInstructorDetails().getHobby());
            } else {
                // Create new instructor details
                InstructorDetails instructorDetails = new InstructorDetails(
                        request.getInstructorDetails().getYoutubeChannel(),
                        request.getInstructorDetails().getHobby()
                );
                instructor.setInstructorDetails(instructorDetails);
            }
        } else {
            // Remove instructor details if not provided in request
            instructor.setInstructorDetails(null);
        }

        Instructor updatedInstructor = instructorRepository.save(instructor);
        return mapToResponse(updatedInstructor);
    }

    @Override
    public void deleteInstructor(UUID id) {
        if (!instructorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Instructor", "id", id);
        }
        instructorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponse getInstructorByEmail(String email) {
        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "email", email));
        
        return mapToResponse(instructor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorResponse> searchInstructorsByName(String name) {
        return instructorRepository.findByFullNameContaining(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorResponse> getInstructorsWithDetails() {
        return instructorRepository.findInstructorsWithDetails()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorResponse> getInstructorsWithoutDetails() {
        return instructorRepository.findInstructorsWithoutDetails()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InstructorResponse addInstructorDetails(UUID instructorId, UUID instructorDetailsId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", instructorId));

        InstructorDetails instructorDetails = instructorDetailsRepository.findById(instructorDetailsId)
                .orElseThrow(() -> new ResourceNotFoundException("InstructorDetails", "id", instructorDetailsId));

        instructor.setInstructorDetails(instructorDetails);
        Instructor updatedInstructor = instructorRepository.save(instructor);
        
        return mapToResponse(updatedInstructor);
    }

    @Override
    public InstructorResponse removeInstructorDetails(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", instructorId));

        instructor.setInstructorDetails(null);
        Instructor updatedInstructor = instructorRepository.save(instructor);
        
        return mapToResponse(updatedInstructor);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return instructorRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return instructorRepository.existsByEmail(email);
    }

    /**
     * Helper method to map Instructor entity to InstructorResponse DTO.
     */
    private InstructorResponse mapToResponse(Instructor instructor) {
        InstructorDetailsResponse instructorDetailsResponse = null;
        
        if (instructor.getInstructorDetails() != null) {
            InstructorDetails details = instructor.getInstructorDetails();
            instructorDetailsResponse = new InstructorDetailsResponse(
                    details.getId(),
                    details.getYoutubeChannel(),
                    details.getHobby(),
                    details.getCreatedAt(),
                    details.getUpdatedAt()
            );
        }

        return new InstructorResponse(
                instructor.getId(),
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getEmail(),
                instructor.getCreatedAt(),
                instructor.getUpdatedAt(),
                instructorDetailsResponse
        );
    }
}