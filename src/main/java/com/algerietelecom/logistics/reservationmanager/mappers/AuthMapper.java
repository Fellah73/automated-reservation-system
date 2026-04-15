package com.algerietelecom.logistics.reservationmanager.mappers;

import org.mapstruct.Mapper;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.auth.LoginDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;

@Mapper(componentModel = "spring")
public interface AuthMapper  {
     UserEntitiy toEntity(LoginDto loginDto);
}
