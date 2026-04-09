package com.algerietelecom.logistics.reservationmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.ApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.auth.LoginDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.mappers.AuthMapper;
import com.algerietelecom.logistics.reservationmanager.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;

    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        try {
            UserEntitiy userEntity = authMapper.toEntity(loginDto);
            String token = authService.login(userEntity);

            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(1 * 60 * 60);

            response.addCookie(cookie);

            ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                    .success(true)
                    .message("Login successful")
                    .statusCode(200)
                    .data("Cookie set")
                    .build();

            return ResponseEntity.ok(apiResponse);

        } catch (RuntimeException e) {
            //
            return ResponseEntity.status(401).body(ApiResponse.<String>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(401)
                    .build());
        }
    }
}

record AuthResponse(String token) {
}