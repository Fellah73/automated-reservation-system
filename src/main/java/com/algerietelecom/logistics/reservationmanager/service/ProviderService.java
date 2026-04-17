package com.algerietelecom.logistics.reservationmanager.service;

import java.util.List;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts.ProviderContractResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.evaluation.ProviderEvaluationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.evaluation.ProviderEvaluationResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.ProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.services.ContractServicesResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderContractEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.Provider.ProviderType;

public interface ProviderService {

    // create provider method
    CreateProviderDto createProvider(ProviderEntity providerToCreate);

    // update provider method
    CreateProviderDto updateProvider(Long id, ProviderEntity providerToUpdate);

    // soft delete provider method
    void softDeleteProvider(Long id);

    // restore provider method
    CreateProviderDto restoreProvider(Long id);

    // get all providers method
    List<ProviderDto> getAllProviders(ProviderType type);

    // contracts methods
    // create contract method
    ProviderContractResponseDTO createContract(Long providerId, ProviderContractEntity dto);

    // update contract method
    ProviderContractResponseDTO updateContract(Long providerId, Long contractId, ProviderContractEntity contractEntity);

    // soft delete contract method
    void softDeleteContract(Long providerId, Long contractId);

    // get all contracts for a provider
    List<ProviderContractResponseDTO> getAllContractsForProvider(Long providerId);

    // get all services for a contract
    ContractServicesResponseDTO getContractServices(Long contractId);

    // evaluation methods

    // create evaluations for a provider
    ProviderEvaluationRequestDTO createEvaluationForProvider(Long providerId,
            ProviderEvaluationRequestDTO evaluationDto, UserEntitiy currentUser);

    // get evaluations for a provider
    ProviderEvaluationResponseDTO getEvaluationsByProvider(Long providerId);
}
