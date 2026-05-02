package com.algerietelecom.logistics.reservationmanager.service;

import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;

public interface AuthService {
    String[] login(UserEntity user);

    UserEntity getMyInfo(String email);
}
