package com.algerietelecom.logistics.reservationmanager.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

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
import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationHistory;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;

public interface ReservationRequestService {

        // user operations
        // CRUD operations for reservation requests & attachments
        ReservationRequest createRequest(ReservationRequest request, UserEntitiy currentUser);

        List<ReservationAttachmentDTO> saveAttachment(
                        String requestNumber, ReservationAttachmentBatchDTO attachmentBatchDTO,
                        UserEntitiy currentUser);

        ReservationRequestResponseDTO updateRequest(String id, ReservationRequestUpdateDTO updateDTO,
                        UserEntitiy currentUser);

        List<ReservationAttachmentDTO> updateAttachment(
                        String requestNumber, ReservationAttachmentBatchDTO attachmentBatchDTO,
                        UserEntitiy currentUser);

        // Retrieval operations
        List<ReservationRequestOverviewDTO> getMyRequests(UserEntitiy currentUser);

        ReservationRequestResponseDTO getRequestById(String id);

        ReservationRequestsStatsDTO getRequestStats();

        // admin operations
        // approve, reject, request changes
        ReservationRequestResponseDTO approveRequest(String id, AdminCommentDTO adminCommentDTO,
                        UserEntitiy currentUser);

        ReservationRequestResponseDTO rejectRequest(String id, AdminCommentDTO adminCommentDTO,
                        UserEntitiy currentUser);

        ReservationRequestResponseDTO requestChanges(String id, AdminModificationRequestDTO modificationRequest,
                        UserEntitiy currentUser);

        // history & audit log
        void saveHistory(ReservationHistory history);

        List<ReservationHistoryResponseDTO> getReservationRequestHistory(String requestId);

        Page<ReservationWithHistoryResponseDTO> getGlobalAuditLog(Pageable pageable);

}