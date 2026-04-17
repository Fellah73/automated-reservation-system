package com.algerietelecom.logistics.reservationmanager.repositories.accommodation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationBookingEntity;

@Repository
public interface AccommodationBookingRepository extends JpaRepository<AccommodationBookingEntity, Long> {
}
