package com.algerietelecom.logistics.reservationmanager.service.impl;

import org.springframework.stereotype.Service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;
import com.algerietelecom.logistics.reservationmanager.mappers.ProviderMapper;
import com.algerietelecom.logistics.reservationmanager.repositories.provider.ProviderRepository;
import com.algerietelecom.logistics.reservationmanager.service.ProviderService;

import jakarta.transaction.Transactional;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    public ProviderServiceImpl(ProviderRepository providerRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    // create provider method
    @Override
    @Transactional
    public CreateProviderDto createProvider(ProviderEntity providerToCreate) {
        try {
            // Vérification du NIF unique
            if (providerToCreate.getNif() != null && providerRepository.existsByNif(providerToCreate.getNif())) {
                throw new RuntimeException("Un prestataire avec ce NIF existe déjà.");
            }

            ProviderEntity provider = ProviderEntity.builder()
                    .name(providerToCreate.getName())
                    .type(providerToCreate.getType())
                    .nif(providerToCreate.getNif())
                    .email(providerToCreate.getEmail())
                    .phoneNumber(providerToCreate.getPhoneNumber())
                    .address(providerToCreate.getAddress())
                    .contactPerson(providerToCreate.getContactPerson())
                    .logoUrl(providerToCreate.getLogoUrl())
                    .active(true)
                    .build();

            ProviderEntity savedProvider = providerRepository.save(provider);

            // convert entity to DTO
            CreateProviderDto responseDto = providerMapper.toDto(savedProvider);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create provider: " + e.getMessage());
        }
    }

    // update provider method
    @Override
    @Transactional
    public CreateProviderDto updateProvider(Long id, ProviderEntity providerToUpdate) {
        try {

            ProviderEntity existingProvider = providerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + id));

            ProviderEntity provider = ProviderEntity.builder()
                    .id(existingProvider.getId())
                    .name(providerToUpdate.getName() != null ? providerToUpdate.getName() : existingProvider.getName())
                    .type(providerToUpdate.getType() != null ? providerToUpdate.getType() : existingProvider.getType())
                    .nif(providerToUpdate.getNif() != null ? providerToUpdate.getNif() : existingProvider.getNif())
                    .email(providerToUpdate.getEmail() != null ? providerToUpdate.getEmail()
                            : existingProvider.getEmail())
                    .phoneNumber(providerToUpdate.getPhoneNumber() != null ? providerToUpdate.getPhoneNumber()
                            : existingProvider.getPhoneNumber())
                    .address(providerToUpdate.getAddress() != null ? providerToUpdate.getAddress()
                            : existingProvider.getAddress())
                    .contactPerson(providerToUpdate.getContactPerson() != null ? providerToUpdate.getContactPerson()
                            : existingProvider.getContactPerson())
                    .logoUrl(providerToUpdate.getLogoUrl() != null ? providerToUpdate.getLogoUrl()
                            : existingProvider.getLogoUrl())
                    .active(true)
                    .build();

            ProviderEntity savedProvider = providerRepository.save(provider);

            // convert entity to DTO
            CreateProviderDto responseDto = providerMapper.toDto(savedProvider);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create provider: " + e.getMessage());
        }
    }

    // soft delete provider method
    @Override
    @Transactional
    public void softDeleteProvider(Long id) {
        try {
            ProviderEntity existingProvider = providerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + id));

            // update active status to false
            existingProvider.setActive(false);
            providerRepository.save(existingProvider);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete provider: " + e.getMessage());
        }
    }

    // restore provider method
    @Override
    @Transactional
    public CreateProviderDto restoreProvider(Long id) {
        try {
            ProviderEntity existingProvider = providerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + id));

            // update active status to true
            existingProvider.setActive(true);
            ProviderEntity savedProvider = providerRepository.save(existingProvider);

            // convert entity to DTO
            CreateProviderDto responseDto = providerMapper.toDto(savedProvider);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore provider: " + e.getMessage());
        }
    }
}
