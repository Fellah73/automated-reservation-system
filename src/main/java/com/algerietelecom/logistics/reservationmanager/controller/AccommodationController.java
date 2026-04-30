package com.algerietelecom.logistics.reservationmanager.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.ApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingDetailsResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingOverviewApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingOverviewResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingRequestDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingStatusDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.ProviderSearchDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.UnbookedReservationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;
import com.algerietelecom.logistics.reservationmanager.service.AccommodationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/accommodation")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    // seach hotels by city
    @GetMapping(path = "/search")
    public ResponseEntity<ApiResponse<List<ProviderSearchDTO>>> getAvailableHotels(
            @RequestParam(required = false) String city) {
        try {
            List<ProviderSearchDTO> hotels = accommodationService.searchAccommodationsProvidersByCity(city);

            return ResponseEntity.ok(ApiResponse.<List<ProviderSearchDTO>>builder()
                    .success(true)
                    .message("Accommodation services found in city: " + city)
                    .statusCode(200)
                    .data(hotels)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ProviderSearchDTO>>builder()
                            .success(false)
                            .message("Erreur lors de la récupération des hôtels : " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // get non booked reservations request for accommodation
    @GetMapping(path = "/pending")
    public ResponseEntity<ApiResponse<List<UnbookedReservationRequestDTO>>> getPendingBookings(
            @AuthenticationPrincipal UserEntity currentUser) {
        try {
            List<UnbookedReservationRequestDTO> pendingBookings = accommodationService
                    .getUnbookedReservationRequests(currentUser);
            return ResponseEntity.ok(ApiResponse.<List<UnbookedReservationRequestDTO>>builder()
                    .success(true)
                    .message("Pending bookings retrieved successfully")
                    .statusCode(200)
                    .data(pendingBookings)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<UnbookedReservationRequestDTO>>builder()
                            .success(false)
                            .message("Failed to retrieve pending bookings: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // create booking for accommodation
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse<AccommodationBookingResponseDto>> createBooking(
            @RequestBody AccommodationBookingRequestDto bookingRequestDto,
            @AuthenticationPrincipal UserEntity currentUser) {
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

    // update booking for accommodation
    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<AccommodationBookingResponseDto>> updateBooking(
            @PathVariable Long id,
            @RequestBody AccommodationBookingRequestDto bookingRequestDto,
            @AuthenticationPrincipal UserEntity currentUser) {
        try {
            AccommodationBookingResponseDto result = accommodationService.updateBooking(id, bookingRequestDto,
                    currentUser);

            return ResponseEntity.ok(ApiResponse.<AccommodationBookingResponseDto>builder()
                    .success(true)
                    .message("Booking updated successfully")
                    .statusCode(200)
                    .data(result)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<AccommodationBookingResponseDto>builder()
                            .success(false)
                            .message("Failed to update booking: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // get booking by id
    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<AccommodationBookingDetailsResponseDto>> getBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity currentUser) {
        try {
            AccommodationBookingDetailsResponseDto result = accommodationService.getBookingById(id, currentUser);
            return ResponseEntity.ok(ApiResponse.<AccommodationBookingDetailsResponseDto>builder()
                    .success(true)
                    .message("Booking retrieved successfully")
                    .statusCode(200)
                    .data(result)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<AccommodationBookingDetailsResponseDto>builder()
                            .success(false)
                            .message("Failed to retrieve booking: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }

    // get all accommodation bookings pagination
    @GetMapping(path = "/all")
    public ResponseEntity<ApiResponse<AccommodationBookingOverviewApiResponse>> getAllBookings(
            @PageableDefault(size = 20, sort = "checkInDate", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserEntity currentUser) {
        try {
            Page<AccommodationBookingOverviewResponseDto> result = accommodationService.getAllBookings(pageable,
                    currentUser);

            AccommodationBookingStatusDTO bookingStatus = accommodationService.getAllBookingStatus();

            AccommodationBookingOverviewApiResponse apiResponseData = new AccommodationBookingOverviewApiResponse();
            apiResponseData.setBookings(result);
            apiResponseData.setBookingStatus(bookingStatus);

            return ResponseEntity.ok(ApiResponse.<AccommodationBookingOverviewApiResponse>builder()
                    .success(true)
                    .message("Bookings retrieved successfully")
                    .statusCode(200)
                    .data(apiResponseData)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<AccommodationBookingOverviewApiResponse>builder()
                            .success(false)
                            .message("Failed to retrieve bookings: " + e.getMessage())
                            .statusCode(500)
                            .build());
        }
    }
}