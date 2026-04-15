package com.algerietelecom.logistics.reservationmanager.mappers;

import org.springframework.stereotype.Component;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.UpdateProviderDto;
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
}
