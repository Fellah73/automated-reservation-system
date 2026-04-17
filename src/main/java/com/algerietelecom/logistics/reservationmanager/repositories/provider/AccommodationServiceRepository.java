package com.algerietelecom.logistics.reservationmanager.repositories.provider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices.AccommodationServiceEntity;

@Repository
public interface AccommodationServiceRepository extends JpaRepository<AccommodationServiceEntity, Long> {
}
