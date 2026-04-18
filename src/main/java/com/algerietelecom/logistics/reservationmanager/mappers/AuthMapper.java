package com.algerietelecom.logistics.reservationmanager.mappers;

import org.mapstruct.Mapper;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.auth.LoginDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;

@Mapper(componentModel = "spring")
public interface AuthMapper {
     UserEntity toEntity(LoginDto loginDto);
}
