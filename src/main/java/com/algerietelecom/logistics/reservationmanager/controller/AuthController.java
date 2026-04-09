package com.algerietelecom.logistics.reservationmanager.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algerietelecom.logistics.reservationmanager.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping(path = "/auth/login")
    public String login() {

        return "alger";
    }

}

record AuthResponse(String token) {
}