package com.algerietelecom.logistics.reservationmanager.repositories;

import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRequestRepository extends JpaRepository<ReservationRequest, Long> {

    List<ReservationRequest> findByCreatedByOrderByCreatedAtDesc(UserEntitiy user);

    Optional<ReservationRequest> findByRequestNumber(String requestNumber);

    List<ReservationRequest> findByStatus(ReservationStatus status);

    // advanced search: retrieve a request with all its loaded relationships
    @Query("SELECT r FROM ReservationRequest r " +
            "LEFT JOIN FETCH r.participants " +
            "LEFT JOIN FETCH r.preferences " +
            "LEFT JOIN FETCH r.attachments " +
            "WHERE r.id = :id")
    Optional<ReservationRequest> findByIdWithDetails(@Param("id") Long id);

    boolean existsByRequestNumber(String requestNumber);
}
