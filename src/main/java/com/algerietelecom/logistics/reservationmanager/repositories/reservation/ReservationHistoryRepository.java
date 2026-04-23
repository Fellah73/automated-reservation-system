package com.algerietelecom.logistics.reservationmanager.repositories.reservation;

import java.util.List;

import org.springframework.data.domain.Pageable; 
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationHistory;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {
    List<ReservationHistory> findByReservationRequestIdOrderByTimestampDesc(Long requestId);

    List<ReservationHistory> findByReservationRequestRequestNumberOrderByTimestampDesc(String requestNumber);

    Page<ReservationHistory> findByReservationRequestId(Long reservationRequestId, Pageable pageable);

    Page<ReservationHistory> findAllByOrderByTimestampDesc(Pageable pageable);
}
