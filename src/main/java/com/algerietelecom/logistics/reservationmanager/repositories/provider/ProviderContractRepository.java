package com.algerietelecom.logistics.reservationmanager.repositories.provider;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderContractEntity;

@Repository
public interface ProviderContractRepository extends JpaRepository<ProviderContractEntity, Long> {
    List<ProviderContractEntity> findByProviderIdOrderByStartDateDesc(Long providerId);

    long countByProviderId(Long providerId);

}
