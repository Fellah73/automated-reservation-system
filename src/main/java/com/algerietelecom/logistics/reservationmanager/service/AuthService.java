package com.algerietelecom.logistics.reservationmanager.service;

import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;

public interface AuthService {
    String login(UserEntitiy user);

    UserEntitiy getMyInfo(String email);
}
