package com.algerietelecom.logistics.reservationmanager.service;

import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;

public interface AuthService {
    String login(UserEntitiy user);

    UserEntitiy getMyInfo(String email);
}
