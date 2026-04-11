package com.algerietelecom.logistics.reservationmanager.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.repositories.AuthRepository;
import com.algerietelecom.logistics.reservationmanager.service.AuthService;
import com.algerietelecom.logistics.reservationmanager.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // login method
    @Override
    public String login(UserEntitiy userEntity) {
        UserEntitiy userRecord = userRepository.findAllByEmail(userEntity.getEmail());
        if (userRecord == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(userEntity.getPassword(), userRecord.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return jwtService.generateToken(userRecord.getUsername(), userRecord.getEmail());
    }

    // me method
    @Override
    public UserEntitiy getMyInfo(String email) {
        UserEntitiy user = userRepository.findAllByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setPassword(null);
        return user;
    }
}
