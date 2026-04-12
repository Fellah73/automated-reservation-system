package com.algerietelecom.logistics.reservationmanager.service;

import java.util.List;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentBatchDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestOverviewDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestsStatsDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;

public interface ReservationRequestService {

    ReservationRequest createRequest(ReservationRequest request, UserEntitiy currentUser);

    List<ReservationAttachmentDTO> saveAttachment(
            String requestNumber, ReservationAttachmentBatchDTO attachmentBatchDTO, UserEntitiy currentUser);

    List<ReservationRequestOverviewDTO> getMyRequests(UserEntitiy currentUser);

    ReservationRequestResponseDTO getRequestById(String id);

    ReservationRequestsStatsDTO getRequestStats();
}