package com.algerietelecom.logistics.reservationmanager.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;

public interface ReservationRequestService {

    ReservationRequest createRequest(ReservationRequest request, UserEntitiy currentUser);

    ReservationAttachmentDTO saveAttachment(
            Long id, MultipartFile file, String category, UserEntitiy currentUser);

    List<ReservationRequest> getMyRequests(UserEntitiy currentUser);

    ReservationRequest getRequestById(Long id);
}