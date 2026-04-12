package com.algerietelecom.logistics.reservationmanager.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationStatus;
import com.algerietelecom.logistics.reservationmanager.repositories.ReservationRequestRepository;
import com.algerietelecom.logistics.reservationmanager.service.ReservationRequestService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationRequestServiceImpl implements ReservationRequestService {

    private final ReservationRequestRepository reservationRequestRepository;

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