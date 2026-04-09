package com.algerietelecom.logistics.reservationmanager.service.impl;

import org.springframework.stereotype.Service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.auth.LoginDto;
import com.algerietelecom.logistics.reservationmanager.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public String login(LoginDto author) {
        return "Login successful for user: " + author.getMail();
    }

}
