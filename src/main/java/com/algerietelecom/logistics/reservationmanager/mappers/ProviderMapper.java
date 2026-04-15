package com.algerietelecom.logistics.reservationmanager.mappers;

import org.springframework.stereotype.Component;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts.ProviderContractRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts.ProviderContractResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts.ProviderContractUpdateRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.ProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.UpdateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderContractEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;

@Component
public class ProviderMapper {

    public CreateProviderDto toDto(ProviderEntity entity) {
        if (entity == null) {
            return null;
        }

        CreateProviderDto dto = new CreateProviderDto();
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setNif(entity.getNif());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setContactPerson(entity.getContactPerson());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setActive(entity.getActive());

        return dto;
    }

    public ProviderDto toDtoWithId(ProviderEntity entity) {
        if (entity == null) {
            return null;
        }

        ProviderDto dto = new ProviderDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setNif(entity.getNif());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setContactPerson(entity.getContactPerson());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setActive(entity.getActive());

        return dto;
    }

    public ProviderEntity CreationToEntity(CreateProviderDto dto) {
        if (dto == null) {
            return null;
        }

        ProviderEntity entity = new ProviderEntity();
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setNif(dto.getNif());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());
        entity.setContactPerson(dto.getContactPerson());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setActive(dto.getActive());

        return entity;
    }

    public ProviderEntity UpdateToEntity(UpdateProviderDto dto) {
        if (dto == null) {
            return null;
        }

        ProviderEntity entity = new ProviderEntity();
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setNif(dto.getNif());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());
        entity.setContactPerson(dto.getContactPerson());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setActive(dto.getActive());

        return entity;
    }

    public ProviderContractEntity ContractRequestToEntity(ProviderContractRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        ProviderContractEntity entity = new ProviderContractEntity();
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setContractUrl(dto.getPdfUrl());
        entity.setFileName(dto.getFileName());
        entity.setTermsAndConditions(dto.getTermsAndConditions());

        return entity;
    }

    public ProviderContractResponseDTO ContractToDto(ProviderContractEntity entity) {
        if (entity == null) {
            return null;
        }

        ProviderContractResponseDTO dto = new ProviderContractResponseDTO();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate().toString());
        dto.setEndDate(entity.getEndDate().toString());
        dto.setContractUrl(entity.getContractUrl());
        dto.setFileName(entity.getFileName());
        dto.setTermsAndConditions(entity.getTermsAndConditions());

        return dto;
    }

    public ProviderContractEntity ContractUpdateToEntity(ProviderContractUpdateRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        ProviderContractEntity entity = new ProviderContractEntity();
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setContractUrl(dto.getPdfUrl());
        entity.setFileName(dto.getFileName());
        entity.setTermsAndConditions(dto.getTermsAndConditions());

        return entity;
    }

    public ProviderContractResponseDTO toContractDto(ProviderContractEntity entity) {
        if (entity == null) {
            return null;
        }

        ProviderContractResponseDTO dto = new ProviderContractResponseDTO();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate().toString());
        dto.setEndDate(entity.getEndDate().toString());
        dto.setContractUrl(entity.getContractUrl());
        dto.setFileName(entity.getFileName());
        dto.setTermsAndConditions(entity.getTermsAndConditions());

        return dto;
    }
}
