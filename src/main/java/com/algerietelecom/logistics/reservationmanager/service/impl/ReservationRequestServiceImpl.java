package com.algerietelecom.logistics.reservationmanager.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentBatchDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestOverviewDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestsStatsDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationAttachment;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationStatus;
import com.algerietelecom.logistics.reservationmanager.mappers.ReservationRequestMapper;
import com.algerietelecom.logistics.reservationmanager.repositories.ReservationAttachmentRepository;
import com.algerietelecom.logistics.reservationmanager.repositories.ReservationRequestRepository;
import com.algerietelecom.logistics.reservationmanager.service.ReservationRequestService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationRequestServiceImpl implements ReservationRequestService {

    private final ReservationRequestRepository reservationRequestRepository;
    private final ReservationAttachmentRepository attachmentRepository;
    private final ReservationRequestMapper reservationMapper;

    // create reservation request
    @Override
    @Transactional
    public ReservationRequest createRequest(ReservationRequest request, UserEntitiy currentUser) {
        int year = LocalDateTime.now().getYear();
        long total = reservationRequestRepository.count();
        long next = total + 1;
        String uniqueId = "R-" + year + "-" + next;
        request.setRequestNumber(uniqueId);

        request.setCreatedBy(currentUser);
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        request.setStatus(ReservationStatus.PENDING);

        if (request.getParticipants() != null) {
            request.getParticipants().forEach(participant -> participant.setRequest(request));
            request.setPeopleCount(request.getParticipants().size());
        }

        if (request.getPreferences() != null) {
            request.getPreferences().setRequest(request);
        }

        return reservationRequestRepository.save(request);
    }

    // save attachments for a reservation request
    @Override
    @Transactional
    public List<ReservationAttachmentDTO> saveAttachment(String id, ReservationAttachmentBatchDTO attachmentBatchDTO,
            UserEntitiy currentUser) {

        ReservationRequest reservationRequest = reservationRequestRepository.findByRequestNumber(id)
                .orElseThrow(() -> new RuntimeException("Reservation request not found"));

        List<ReservationAttachment> attachmentsToSave = attachmentBatchDTO.getAttachments().stream()
                .map(dto -> ReservationAttachment.builder()
                        .fileName(dto.getFileName())
                        .fileType(dto.getFileType())
                        .filePath(dto.getFilePath())
                        .reservationRequest(reservationRequest)
                        .uploadedBy(currentUser)
                        .build())
                .collect(Collectors.toList());

        List<ReservationAttachment> savedAttachments = attachmentRepository.saveAll(attachmentsToSave);

        return savedAttachments.stream()
                .map(attachment -> reservationMapper.toAttachmentDTO(attachment))
                .collect(Collectors.toList());
    }

    // get all my reservations
    @Override
    @Transactional
    public List<ReservationRequestOverviewDTO> getMyRequests(UserEntitiy currentUser) {
        List<ReservationRequest> reservationRequests = reservationRequestRepository
                .findByCreatedByOrderByCreatedAtDesc(currentUser);

        if (reservationRequests.isEmpty()) {
            throw new RuntimeException("No reservation requests found");
        }

        // convert to overview DTOs
        List<ReservationRequestOverviewDTO> overviewReservationRequestesDTOs = reservationRequests.stream()
                .map(reservationMapper::toOverviewDTO)
                .collect(Collectors.toList());

        return overviewReservationRequestesDTOs;
    }

    // get reservation requests stats
    @Override
    public ReservationRequestsStatsDTO getRequestStats() {
        long total = reservationRequestRepository.count();
        long pendingCount = reservationRequestRepository.countByStatus(ReservationStatus.PENDING);
        long acceptedCount = reservationRequestRepository.countByStatus(ReservationStatus.APPROVED);
        long rejectedCount = reservationRequestRepository.countByStatus(ReservationStatus.REJECTED);
        long toUpdateCount = reservationRequestRepository.countByStatus(ReservationStatus.MODIFICATION_REQUESTED);

        return ReservationRequestsStatsDTO.builder()
                .totalRequests(total)
                .pendingCount(pendingCount)
                .acceptedCount(acceptedCount)
                .rejectedCount(rejectedCount)
                .toUpdateCount(toUpdateCount)
                .build();
    }

    // get reservation request
    @Override
    @Transactional
    public ReservationRequestResponseDTO getRequestById(String id) {
        ReservationRequest reservationRequest = reservationRequestRepository.findByRequestNumber(id)
                .orElse(null);

        if (reservationRequest == null) {
            throw new RuntimeException("Reservation request not found");
        }

        return reservationMapper.toResponseDTO(reservationRequest);
    }
}