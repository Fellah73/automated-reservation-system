package com.algerietelecom.logistics.reservationmanager.repositories.provider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEvaluationEntity;

@Repository
public interface ProviderEvaluationRepository extends JpaRepository<ProviderEvaluationEntity, Long> {
    ProviderEvaluationEntity findByProviderId(Long providerId);
}
