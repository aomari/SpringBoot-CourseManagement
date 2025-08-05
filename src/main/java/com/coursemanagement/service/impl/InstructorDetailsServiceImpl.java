package com.coursemanagement.service.impl;

import com.coursemanagement.dto.InstructorDetailsRequest;
import com.coursemanagement.dto.InstructorDetailsResponse;
import com.coursemanagement.entity.InstructorDetails;
import com.coursemanagement.exception.ResourceNotFoundException;
import com.coursemanagement.repository.InstructorDetailsRepository;
import com.coursemanagement.service.InstructorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for InstructorDetails operations.
 */
@Service
@Transactional
public class InstructorDetailsServiceImpl implements InstructorDetailsService {

    private final InstructorDetailsRepository instructorDetailsRepository;

    @Autowired
    public InstructorDetailsServiceImpl(InstructorDetailsRepository instructorDetailsRepository) {
        this.instructorDetailsRepository = instructorDetailsRepository;
    }

    @Override
    public InstructorDetailsResponse createInstructorDetails(InstructorDetailsRequest request) {
        InstructorDetails instructorDetails = new InstructorDetails(
                request.getYoutubeChannel(),
                request.getHobby()
        );

        InstructorDetails savedInstructorDetails = instructorDetailsRepository.save(instructorDetails);
        return mapToResponse(savedInstructorDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorDetailsResponse getInstructorDetailsById(UUID id) {
        InstructorDetails instructorDetails = instructorDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InstructorDetails", "id", id));
        
        return mapToResponse(instructorDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorDetailsResponse> getAllInstructorDetails() {
        return instructorDetailsRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InstructorDetailsResponse updateInstructorDetails(UUID id, InstructorDetailsRequest request) {
        InstructorDetails instructorDetails = instructorDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InstructorDetails", "id", id));

        instructorDetails.setYoutubeChannel(request.getYoutubeChannel());
        instructorDetails.setHobby(request.getHobby());

        InstructorDetails updatedInstructorDetails = instructorDetailsRepository.save(instructorDetails);
        return mapToResponse(updatedInstructorDetails);
    }

    @Override
    public void deleteInstructorDetails(UUID id) {
        if (!instructorDetailsRepository.existsById(id)) {
            throw new ResourceNotFoundException("InstructorDetails", "id", id);
        }
        instructorDetailsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorDetailsResponse> searchByYoutubeChannel(String youtubeChannel) {
        return instructorDetailsRepository.findByYoutubeChannelContainingIgnoreCase(youtubeChannel)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorDetailsResponse> searchByHobby(String hobby) {
        return instructorDetailsRepository.findByHobbyContainingIgnoreCase(hobby)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorDetailsResponse> getOrphanedInstructorDetails() {
        return instructorDetailsRepository.findOrphanedInstructorDetails()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return instructorDetailsRepository.existsById(id);
    }

    /**
     * Helper method to map InstructorDetails entity to InstructorDetailsResponse DTO.
     */
    private InstructorDetailsResponse mapToResponse(InstructorDetails instructorDetails) {
        return new InstructorDetailsResponse(
                instructorDetails.getId(),
                instructorDetails.getYoutubeChannel(),
                instructorDetails.getHobby(),
                instructorDetails.getCreatedAt(),
                instructorDetails.getUpdatedAt()
        );
    }
}