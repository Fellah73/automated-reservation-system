package com.algerietelecom.logistics.reservationmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.ApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.UpdateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;
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

}
