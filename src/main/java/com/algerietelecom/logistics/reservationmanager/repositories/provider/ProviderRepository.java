package com.algerietelecom.logistics.reservationmanager.repositories.provider;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.Provider.ProviderType;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    Page<ProviderEntity> findByType(ProviderType type, Pageable pageable);

    List<ProviderEntity> findByType(ProviderType type);

    boolean existsByNif(String nif);

    List<ProviderEntity> findByCityAndType(String city, ProviderType type);

    long countByType(ProviderType type);
}
