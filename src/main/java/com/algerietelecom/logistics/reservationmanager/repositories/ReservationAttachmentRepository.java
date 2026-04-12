package com.algerietelecom.logistics.reservationmanager.repositories;

import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationAttachment;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationAttachmentRepository extends JpaRepository<ReservationAttachment, Long> {

    List<ReservationAttachment> findByReservationRequest(ReservationRequest reservationRequest);

}
