package com.algerietelecom.logistics.reservationmanager.service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.auth.LoginDto;

public interface AuthService {
    String login(LoginDto author);
}
