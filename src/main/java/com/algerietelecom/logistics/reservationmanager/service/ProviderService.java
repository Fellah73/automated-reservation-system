package com.algerietelecom.logistics.reservationmanager.service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;

public interface ProviderService {

    // create provider method
    CreateProviderDto createProvider(ProviderEntity providerToCreate);

    // update provider method
    CreateProviderDto updateProvider(Long id, ProviderEntity providerToUpdate);

    // soft delete provider method
    void softDeleteProvider(Long id);

    // restore provider method
    CreateProviderDto restoreProvider(Long id);
}
