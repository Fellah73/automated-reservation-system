package com.algerietelecom.logistics.reservationmanager.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.repositories.ReservationRequestRepository;
import com.algerietelecom.logistics.reservationmanager.service.ReservationRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationRequestServiceImpl implements ReservationRequestService {

    private final ReservationRequestRepository reservationRequestRepository;

    @Override
    public ReservationRequest createRequest(ReservationRequest request, UserEntitiy currentUser) {
        request.setCreatedBy(currentUser);
        return reservationRequestRepository.save(request);
    }

    @Override
    public ReservationAttachmentDTO saveAttachment(Long id, MultipartFile file, String category,
            UserEntitiy currentUser) {
        return null;
    }

    @Override
    public List<ReservationRequest> getMyRequests(UserEntitiy currentUser) {
        return reservationRequestRepository.findByCreatedByOrderByCreatedAtDesc(currentUser);
    }

    @Override
    public ReservationRequest getRequestById(Long id) {
        return reservationRequestRepository.findById(id).orElse(null);
    }
}