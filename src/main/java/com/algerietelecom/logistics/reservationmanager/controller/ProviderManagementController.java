package com.algerietelecom.logistics.reservationmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.ApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts.ProviderContractRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts.ProviderContractResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts.ProviderContractUpdateRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.ProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.UpdateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderContractEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.Provider.ProviderType;
import com.algerietelecom.logistics.reservationmanager.mappers.ProviderMapper;
import com.algerietelecom.logistics.reservationmanager.service.ProviderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
public class ProviderManagementController {

    private final ProviderService providerService;
    private final ProviderMapper providerMapper;

    // admin routes
    // create a new provider
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse<CreateProviderDto>> createProvider(
            @RequestBody CreateProviderDto createDTO,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {

            // convert DTO to entity
            ProviderEntity provider = providerMapper.CreationToEntity(createDTO);

            // create provider
            CreateProviderDto createdProvider = providerService.createProvider(provider);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<CreateProviderDto>builder()
                    .success(true)
                    .message("Provider created successfully")
                    .statusCode(201)
                    .data(createdProvider)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<CreateProviderDto>builder()
                    .success(false)
                    .message("Failed to create provider: " + e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // update a new provider
    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<CreateProviderDto>> updateProvider(
            @PathVariable Long id,
            @RequestBody UpdateProviderDto updateDTO,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {

            // convert DTO to entity
            ProviderEntity provider = providerMapper.UpdateToEntity(updateDTO);

            // update provider
            CreateProviderDto updatedProvider = providerService.updateProvider(id, provider);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<CreateProviderDto>builder()
                    .success(true)
                    .message("Provider updated successfully")
                    .statusCode(200)
                    .data(updatedProvider)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<CreateProviderDto>builder()
                    .success(false)
                    .message("Failed to update provider: " + e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // delete a provider
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProvider(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {
            providerService.softDeleteProvider(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.<Void>builder()
                    .success(true)
                    .message("Provider deleted successfully")
                    .statusCode(202)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<Void>builder()
                    .success(false)
                    .message("Failed to delete provider: " + e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // restore a provider
    @PatchMapping(path = "/{id}/restore")
    public ResponseEntity<ApiResponse<CreateProviderDto>> restoreProvider(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {
            CreateProviderDto restoredProvider = providerService.restoreProvider(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<CreateProviderDto>builder()
                    .success(true)
                    .message("Provider restored successfully")
                    .data(restoredProvider)
                    .statusCode(200)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<CreateProviderDto>builder()
                    .success(false)
                    .message("Failed to restore provider: " + e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // get all providers
    @GetMapping(path = "/all")
    public ResponseEntity<ApiResponse<List<ProviderDto>>> getAllProviders(
            @RequestParam(required = false) ProviderType type,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {
            List<ProviderDto> providers = providerService.getAllProviders(type);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<ProviderDto>>builder()
                    .success(true)
                    .message("Providers retrieved successfully")
                    .statusCode(200)
                    .data(providers)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ProviderDto>>builder()
                            .success(false)
                            .message("Failed to retrieve providers: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // contracts routes
    // create a new contract for a provider
    @PostMapping("/{id}/contracts")
    public ResponseEntity<ApiResponse<ProviderContractResponseDTO>> addContract(
            @PathVariable Long id,
            @RequestBody ProviderContractRequestDTO dto) {
        try {

            ProviderContractEntity contractToCreate = providerMapper.ContractRequestToEntity(dto);

            ProviderContractResponseDTO providerContract = providerService.createContract(id, contractToCreate);

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ProviderContractResponseDTO>builder()
                    .success(true)
                    .message("Contrat et service enregistrés.")
                    .statusCode(201)
                    .data(providerContract)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<ProviderContractResponseDTO>builder()
                            .success(false)
                            .message("Failed to create contract: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // update a contract for a provider
    @PutMapping("/{id}/contracts/{contractId}")
    public ResponseEntity<ApiResponse<ProviderContractResponseDTO>> updateContract(
            @PathVariable Long id,
            @PathVariable Long contractId,
            @RequestBody ProviderContractUpdateRequestDTO dto) {
        try {

            ProviderContractEntity contractToUpdate = providerMapper.ContractUpdateToEntity(dto);

            ProviderContractResponseDTO updatedContract = providerService.updateContract(id, contractId,
                    contractToUpdate);

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<ProviderContractResponseDTO>builder()
                    .success(true)
                    .message("Contrat et service mis à jour.")
                    .statusCode(200)
                    .data(updatedContract)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<ProviderContractResponseDTO>builder()
                            .success(false)
                            .message("Failed to update contract: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // soft delete a contract for a provider
    @DeleteMapping("/{id}/contracts/{contractId}")
    public ResponseEntity<ApiResponse<Void>> deleteContract(
            @PathVariable Long id,
            @PathVariable Long contractId) {
        try {
            providerService.softDeleteContract(id, contractId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.<Void>builder()
                    .success(true)
                    .message("Contrat supprimé avec succès.")
                    .statusCode(202)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message("Failed to delete contract: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // retrieve all contracts for a provider
    @GetMapping("/{id}/contracts")
    public ResponseEntity<ApiResponse<List<ProviderContractResponseDTO>>> getAllContracts(@PathVariable Long id) {
        try {
            List<ProviderContractResponseDTO> contracts = providerService.getAllContractsForProvider(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<ProviderContractResponseDTO>>builder()
                    .success(true)
                    .message("Contracts retrieved successfully")
                    .statusCode(200)
                    .data(contracts)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ProviderContractResponseDTO>>builder()
                            .success(false)
                            .message("Failed to retrieve contracts: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // contract services routes
    // retrieve services for a contract of a provider
    {
        /*
         * @GetMapping("/{contractId}/services")
         * public ResponseEntity<ApiResponse<ContractServicesResponseDTO>>
         * getServices(@PathVariable Long contractId) {
         * try {
         * ContractServicesResponseDTO services =
         * providerService.getContractServices(contractId);
         * return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<
         * ContractServicesResponseDTO>builder()
         * .success(true)
         * .message("Services retrieved successfully")
         * .statusCode(200)
         * .data(services)
         * .build());
         * } catch (Exception e) {
         * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
         * .body(ApiResponse.<ContractServicesResponseDTO>builder()
         * .success(false)
         * .message("Failed to retrieve services: " + e.getMessage())
         * .statusCode(500)
         * .build());
         * }
         * }
         */}
}