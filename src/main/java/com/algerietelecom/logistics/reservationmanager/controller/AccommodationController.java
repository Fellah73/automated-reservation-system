package com.algerietelecom.logistics.reservationmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.ApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingRequestDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.service.AccommodationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/accommodation")
@AllArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    // seach hotels by city
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CreateProviderDto>>> getAvailableHotels(
            @RequestParam(required = false) String city) {
        try {
            List<CreateProviderDto> hotels = accommodationService.searchAccommodationsProvidersByCity(city);

            return ResponseEntity.ok(ApiResponse.<List<CreateProviderDto>>builder()
                    .success(true)
                    .message("Accommodation services found in city: " + city)
                    .statusCode(200)
                    .data(hotels)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<CreateProviderDto>>builder()
                            .success(false)
                            .message("Erreur lors de la récupération des hôtels : " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // create booking for accommodation
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AccommodationBookingResponseDto>> createBooking(
            @RequestBody AccommodationBookingRequestDto bookingRequestDto,
            @AuthenticationPrincipal UserEntitiy currentUser) {
        try {
            AccommodationBookingResponseDto result = accommodationService.createBooking(bookingRequestDto, currentUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<AccommodationBookingResponseDto>builder()
                    .success(true)
                    .message("Booking created successfully")
                    .statusCode(201)
                    .data(result)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<AccommodationBookingResponseDto>builder()
                            .success(false)
                            .message("Failed to create booking: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }
}
