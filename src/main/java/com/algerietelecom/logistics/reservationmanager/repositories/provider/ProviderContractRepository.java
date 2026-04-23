package com.algerietelecom.logistics.reservationmanager.repositories.provider;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderContractEntity;

@Repository
public interface ProviderContractRepository extends JpaRepository<ProviderContractEntity, Long> {
    Page<ProviderContractEntity> findByProviderIdOrderByStartDateDesc(Long providerId, Pageable pageable);

    long countByProviderId(Long providerId);

}
