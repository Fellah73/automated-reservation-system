package com.algerietelecom.logistics.reservationmanager.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.AdminCommentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.AdminModificationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentBatchDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationHistoryResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestOverviewDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestUpdateDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestsStatsDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationWithHistoryResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationAttachment;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationHistory;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationRequest.AttachmentCategory;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationRequest.HistoryAction;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationRequest.ReservationStatus;
import com.algerietelecom.logistics.reservationmanager.mappers.ReservationRequestMapper;
import com.algerietelecom.logistics.reservationmanager.repositories.reservation.ReservationAttachmentRepository;
import com.algerietelecom.logistics.reservationmanager.repositories.reservation.ReservationHistoryRepository;
import com.algerietelecom.logistics.reservationmanager.repositories.reservation.ReservationRequestRepository;
import com.algerietelecom.logistics.reservationmanager.service.ReservationRequestService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ReservationRequestServiceImpl implements ReservationRequestService {

        private final ReservationRequestRepository reservationRequestRepository;
        private final ReservationAttachmentRepository attachmentRepository;
        private final ReservationHistoryRepository historyRepository;
        private final ReservationRequestMapper reservationMapper;
        private final ObjectMapper objectMapper;

        // reservation request history logging
        @Override
        @Transactional
        public void saveHistory(ReservationHistory history) {
                historyRepository.save(history);
        }

        // create reservation request
        @Override
        @Transactional
        public ReservationRequest createRequest(ReservationRequest request, UserEntity currentUser) {
                int year = LocalDateTime.now().getYear();
                long total = reservationRequestRepository.count();
                long next = total + 1;
                String uniqueId = "R-" + year + "-" + next;
                request.setRequestNumber(uniqueId);

                request.setCreatedBy(currentUser);
                request.setCreatedAt(LocalDateTime.now());
                request.setUpdatedAt(LocalDateTime.now());
                request.setStatus(ReservationStatus.PENDING);
                request.setDuration((int) java.time.Duration
                                .between(request.getDepartureDate(), request.getReturnDate()).toDays() + 1);

                if (request.getParticipants() != null) {
                        request.getParticipants().forEach(participant -> participant.setRequest(request));
                        request.setPeopleCount(request.getParticipants().size());
                }

                if (request.getPreferences() != null) {
                        request.getPreferences().setRequest(request);
                }

                ReservationRequest reservationRequest = reservationRequestRepository.save(request);

                // log creation in history
                ReservationHistory history = ReservationHistory.builder()
                                .reservationRequest(reservationRequest)
                                .performedBy(currentUser)
                                .action(HistoryAction.CREATION)
                                .previousStatus(null)
                                .newStatus(ReservationStatus.PENDING)
                                .adminComment("reservation request created number" + next)
                                .modificationInstructions(null)
                                .build();

                saveHistory(history);

                return reservationRequest;
        }

        // save attachments for a reservation request
        @Override
        @Transactional
        public List<ReservationAttachmentDTO> saveAttachment(String id,
                        ReservationAttachmentBatchDTO attachmentBatchDTO,
                        UserEntity currentUser) {

                ReservationRequest reservationRequest = reservationRequestRepository.findByRequestNumber(id)
                                .orElseThrow(() -> new RuntimeException("Reservation request not found"));

                List<ReservationAttachment> attachmentsToSave = attachmentBatchDTO.getAttachments().stream()
                                .map(dto -> ReservationAttachment.builder()
                                                .fileName(dto.getFileName())
                                                .fileType(dto.getFileType())
                                                .filePath(dto.getFilePath())
                                                .category(AttachmentCategory.valueOf(dto.getCategory()))
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
        public List<ReservationRequestOverviewDTO> getMyRequests(UserEntity currentUser) {
                List<ReservationRequest> reservationRequests = reservationRequestRepository
                                .findByCreatedByOrderByCreatedAtDesc(currentUser);

                // if (reservationRequests.isEmpty()) {
                // throw new RuntimeException("No reservation requests found");
                // }

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
                long toUpdateCount = reservationRequestRepository
                                .countByStatus(ReservationStatus.MODIFICATION_REQUESTED);

                long urgentCount = reservationRequestRepository
                                .countByDepartureDateLessThan(LocalDateTime.now().plusDays(3));
                return ReservationRequestsStatsDTO.builder()
                                .totalRequests(total)
                                .pendingCount(pendingCount)
                                .acceptedCount(acceptedCount)
                                .rejectedCount(rejectedCount)
                                .toUpdateCount(toUpdateCount)
                                .urgentCount(urgentCount)
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

                List<ReservationAttachment> requestAttachments = attachmentRepository
                                .findByReservationRequest(reservationRequest);

                return reservationMapper.toResponseDTO(reservationRequest, requestAttachments);
        }

        // approver routes
        // get all reservation requests
        @Override
        @Transactional
        public Page<ReservationRequestOverviewDTO> getAllRequests(Pageable pageable) {
                Page<ReservationRequest> reservationRequests = reservationRequestRepository
                                .findAllFilterByStatus(pageable, ReservationStatus.PENDING);

                if (reservationRequests.isEmpty()) {
                        throw new RuntimeException("No reservation requests found");
                }

                // convert to overview DTOs
                Page<ReservationRequestOverviewDTO> overviewDTOs = reservationRequests
                                .map(reservationMapper::toOverviewDTO);

                return overviewDTOs;
        }

        // approve reservation request
        @Override
        @Transactional
        public ReservationRequestResponseDTO approveRequest(String id,
                        UserEntity currentUser) {
                ReservationRequest reservationRequest = reservationRequestRepository.findByRequestNumber(id)
                                .orElseThrow(() -> new RuntimeException("Reservation request not found"));

                if (reservationRequest.getStatus() == ReservationStatus.APPROVED) {
                        throw new RuntimeException("Approved reservation request cannot be approved again");
                }

                reservationRequest.setStatus(ReservationStatus.APPROVED);
                reservationRequest.setUpdatedAt(LocalDateTime.now());

                ReservationRequest updatedRequest = reservationRequestRepository.save(reservationRequest);

                // log approval in history
                ReservationHistory history = ReservationHistory.builder()
                                .reservationRequest(updatedRequest)
                                .performedBy(currentUser)
                                .action(HistoryAction.APPROVAL)
                                .previousStatus(ReservationStatus.PENDING)
                                .newStatus(ReservationStatus.APPROVED)
                                .adminComment("reservation request approved " + updatedRequest.getRequestNumber())
                                .modificationInstructions(null)
                                .build();

                saveHistory(history);

                List<ReservationAttachment> requestAttachments = attachmentRepository
                                .findByReservationRequest(updatedRequest);

                return reservationMapper.toResponseDTO(updatedRequest, requestAttachments);
        }

        // reject reservation request
        @Override
        @Transactional
        public ReservationRequestResponseDTO rejectRequest(String id, AdminCommentDTO adminCommentDTO,
                        UserEntity currentUser) {
                ReservationRequest reservationRequest = reservationRequestRepository.findByRequestNumber(id)
                                .orElseThrow(() -> new RuntimeException("Reservation request not found"));

                if (reservationRequest.getStatus() == ReservationStatus.REJECTED) {
                        throw new RuntimeException("Rejected reservation request cannot be rejected again");
                }

                reservationRequest.setStatus(ReservationStatus.REJECTED);
                reservationRequest.setAdminComment(adminCommentDTO.getComment());
                reservationRequest.setUpdatedAt(LocalDateTime.now());

                ReservationRequest updatedRequest = reservationRequestRepository.save(reservationRequest);

                List<ReservationAttachment> requestAttachments = attachmentRepository
                                .findByReservationRequest(updatedRequest);

                // log rejection in history
                ReservationHistory history = ReservationHistory.builder()
                                .reservationRequest(updatedRequest)
                                .performedBy(currentUser)
                                .action(HistoryAction.REJECTION)
                                .previousStatus(ReservationStatus.PENDING)
                                .newStatus(ReservationStatus.REJECTED)
                                .adminComment("reservation request rejected " + updatedRequest.getRequestNumber())
                                .modificationInstructions(null)
                                .build();

                saveHistory(history);

                return reservationMapper.toResponseDTO(updatedRequest, requestAttachments);
        }

        // request changes for a reservation request
        @Override
        @Transactional
        public ReservationRequestResponseDTO requestChanges(String id, AdminModificationRequestDTO modificationRequest,
                        UserEntity currentUser) {
                ReservationRequest request = reservationRequestRepository.findByRequestNumber(id)
                                .orElseThrow(() -> new RuntimeException("Reservation request not found"));

                if (request.getStatus() == ReservationStatus.APPROVED) {
                        throw new RuntimeException("Approved reservation request cannot be modified");
                }

                try {
                        String jsonInstructions = objectMapper.writeValueAsString(modificationRequest);

                        request.setStatus(ReservationStatus.MODIFICATION_REQUESTED);
                        request.setModificationInstructions(jsonInstructions);
                        request.setAdminComment(modificationRequest.getGlobalComment());

                        ReservationRequest reservationRequest = reservationRequestRepository.save(request);

                        // log modification request in history
                        ReservationHistory history = ReservationHistory.builder()
                                        .reservationRequest(reservationRequest)
                                        .performedBy(currentUser)
                                        .action(HistoryAction.MODIFICATION_REQUESTED)
                                        .previousStatus(ReservationStatus.PENDING)
                                        .newStatus(ReservationStatus.MODIFICATION_REQUESTED)
                                        .adminComment("modification requested for reservation request "
                                                        + reservationRequest.getRequestNumber())
                                        .modificationInstructions(jsonInstructions)
                                        .build();

                        saveHistory(history);

                        return reservationMapper.toResponseDTO(reservationRequest, null);
                } catch (Exception e) {
                        throw new RuntimeException("Error processing request", e);
                }
        }

        // request attachment changes for a reservation request
        @Override
        @Transactional
        public ReservationRequestResponseDTO requestAttachmentChanges(String id,
                        AdminModificationRequestDTO modificationRequest,
                        UserEntity currentUser) {
                ReservationRequest request = reservationRequestRepository.findByRequestNumber(id)
                                .orElseThrow(() -> new RuntimeException("Reservation request not found"));

                if (request.getStatus() == ReservationStatus.APPROVED) {
                        throw new RuntimeException("Approved reservation request cannot be modified");
                }

                try {
                        String jsonInstructions = objectMapper.writeValueAsString(modificationRequest);

                        request.setStatus(ReservationStatus.MODIFICATION_REQUESTED);
                        request.setModificationAttachmentInstructions(jsonInstructions);
                        request.setAdminComment(modificationRequest.getGlobalComment());

                        ReservationRequest reservationRequest = reservationRequestRepository.save(request);

                        // log modification request in history
                        ReservationHistory history = ReservationHistory.builder()
                                        .reservationRequest(reservationRequest)
                                        .performedBy(currentUser)
                                        .action(HistoryAction.MODIFICATION_REQUESTED)
                                        .previousStatus(ReservationStatus.PENDING)
                                        .newStatus(ReservationStatus.MODIFICATION_REQUESTED)
                                        .adminComment("modification requested for reservation request attachments "
                                                        + reservationRequest.getRequestNumber())
                                        .modificationInstructions(jsonInstructions)
                                        .build();

                        saveHistory(history);

                        return reservationMapper.toResponseDTO(reservationRequest, null);
                } catch (Exception e) {
                        throw new RuntimeException("Error processing request", e);
                }
        }

        // get reservation request history
        @Override
        @Transactional
        public Page<ReservationHistoryResponseDTO> getReservationRequestHistory(String requestId, Pageable pageable) {
                ReservationRequest reservationRequest = reservationRequestRepository.findByRequestNumber(requestId)
                                .orElseThrow(() -> new RuntimeException("Reservation request not found"));

                try {

                        Page<ReservationHistory> history = historyRepository
                                        .findByReservationRequestId(reservationRequest.getId(), pageable);

                        List<ReservationHistoryResponseDTO> responseDTOs = history.stream()
                                        .map(reservationMapper::toHistoryResponseDTO)
                                        .collect(Collectors.toList());

                        return new PageImpl<>(responseDTOs, history.getPageable(), history.getTotalElements());
                } catch (Exception e) {
                        throw new RuntimeException("Error retrieving reservation request history: " + e.getMessage());
                }
        }

        // get global audit log
        @Override
        @Transactional
        public Page<ReservationWithHistoryResponseDTO> getGlobalAuditLog(Pageable pageable) {
                Page<ReservationRequest> reservationRequestsHistory = reservationRequestRepository
                                .findAll(pageable);

                if (reservationRequestsHistory.isEmpty()) {
                        throw new RuntimeException("No audit log entries found");
                }

                // convert to response DTOs
                Page<ReservationWithHistoryResponseDTO> responseDTO = reservationRequestsHistory
                                .map(reservationMapper::toWithHistoryResponseDTO);

                return responseDTO;
        }

        // update reservation request
        @Override
        @Transactional
        public ReservationRequestResponseDTO updateRequest(String id, ReservationRequestUpdateDTO updateDTO,
                        UserEntity currentUser) {
                ReservationRequest request = reservationRequestRepository.findByRequestNumber(id)
                                .orElseThrow(() -> new RuntimeException("Reservation request not found"));

                if (request.getStatus() == ReservationStatus.APPROVED) {
                        throw new RuntimeException("Approved reservation request cannot be modified");
                }

                try {
                        request.setReason(updateDTO.getReason() != null ? updateDTO.getReason() : request.getReason());
                        request.setDepartureDate(
                                        updateDTO.getDepartureDate() != null ? updateDTO.getDepartureDate()
                                                        : request.getDepartureDate());
                        request.setReturnDate(
                                        updateDTO.getReturnDate() != null ? updateDTO.getReturnDate()
                                                        : request.getReturnDate());
                        request.setDestination(
                                        updateDTO.getDestination() != null ? updateDTO.getDestination()
                                                        : request.getDestination());

                        if (updateDTO.getPreferences() != null && request.getPreferences() != null) {
                                request.getPreferences()
                                                .setHotelPreference(updateDTO.getPreferences().getHotelPreference());
                                request.getPreferences()
                                                .setTransportType(updateDTO.getPreferences().getTransportType());
                        }

                        if (updateDTO.getParticipants() != null) {
                                // clear existing participants
                                request.getParticipants().clear();

                                // add new participants
                                updateDTO.getParticipants().forEach(participantDTO -> {
                                        request.getParticipants().add(
                                                        reservationMapper.toParticipantEntity(participantDTO, request));
                                });

                                // count participants
                                request.setPeopleCount(request.getParticipants().size());
                        }

                        request.setUpdatedAt(LocalDateTime.now());
                        request.setModificationInstructions(null);

                        if (request.getModificationAttachmentInstructions() == null) {
                                request.setStatus(ReservationStatus.PENDING);
                        }

                        ReservationRequest updatedRequest = reservationRequestRepository.save(request);

                        List<ReservationAttachment> requestAttachments = attachmentRepository
                                        .findByReservationRequest(updatedRequest);

                        // log in history
                        ReservationHistory history = ReservationHistory.builder()
                                        .reservationRequest(updatedRequest)
                                        .performedBy(currentUser)
                                        .action(HistoryAction.UPDATE)
                                        .previousStatus(request.getStatus())
                                        .newStatus(ReservationStatus.PENDING)
                                        .adminComment("Reservation request updated "
                                                        + updatedRequest.getRequestNumber())
                                        .modificationInstructions("Details updated by user")
                                        .build();

                        saveHistory(history);

                        return reservationMapper.toResponseDTO(updatedRequest, requestAttachments);
                } catch (Exception e) {
                        throw new RuntimeException("Error updating reservation request", e);
                }
        }

        // save attachments for a reservation request
        @Override
        @Transactional
        public List<ReservationAttachmentDTO> updateAttachment(String id,
                        ReservationAttachmentBatchDTO attachmentBatchDTO,
                        UserEntity currentUser) {

                ReservationRequest reservationRequest = reservationRequestRepository.findByRequestNumber(id)
                                .orElseThrow(() -> new RuntimeException("Reservation request not found"));

                // clear reservation request attachments
                reservationRequest.getAttachments().clear();

                List<ReservationAttachment> attachmentsToUpdate = attachmentBatchDTO.getAttachments().stream()
                                .map(dto -> ReservationAttachment.builder()
                                                .fileName(dto.getFileName())
                                                .fileType(dto.getFileType())
                                                .filePath(dto.getFilePath())
                                                .category(AttachmentCategory.valueOf(dto.getCategory()))
                                                .reservationRequest(reservationRequest)
                                                .uploadedBy(currentUser)
                                                .build())
                                .collect(Collectors.toList());

                reservationRequest.setModificationAttachmentInstructions(null);

                if (reservationRequest.getModificationInstructions() == null) {
                        reservationRequest.setStatus(ReservationStatus.PENDING);
                }

                ReservationRequest updatedRequest = reservationRequestRepository.save(reservationRequest);

                List<ReservationAttachment> updatedAttachments = attachmentRepository.saveAll(attachmentsToUpdate);

                // log in history
                ReservationHistory history = ReservationHistory.builder()
                                .reservationRequest(reservationRequest)
                                .performedBy(currentUser)
                                .action(HistoryAction.UPDATE)
                                .previousStatus(reservationRequest.getStatus())
                                .newStatus(ReservationStatus.PENDING)
                                .adminComment("Reservation request attachments updated "
                                                + updatedRequest.getRequestNumber())
                                .modificationInstructions("Attachments updated by user")
                                .build();

                saveHistory(history);

                // convert to DTOs
                List<ReservationAttachmentDTO> updatedAttachmentDTOs = updatedAttachments.stream()
                                .map(attachment -> reservationMapper.toAttachmentDTO(attachment))
                                .collect(Collectors.toList());

                return updatedAttachmentDTOs;
        }
}