package com.algerietelecom.logistics.reservationmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingDetailsResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingOverviewResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingRequestDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingStatusDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.ProviderSearchDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.UnbookedReservationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;

public interface AccommodationService {
        // search hotels by city
        List<ProviderSearchDTO> searchAccommodationsProvidersByCity(String city);

        // get unbooked reservation requests
        List<UnbookedReservationRequestDTO> getUnbookedReservationRequests(UserEntity currentUser);

        // create booking for accommodation
        AccommodationBookingResponseDto createBooking(AccommodationBookingRequestDto bookingRequestDto,
                        UserEntity currentUser);

        // update booking for accommodation
        AccommodationBookingResponseDto updateBooking(Long id, AccommodationBookingRequestDto bookingRequestDto,
                        UserEntity currentUser);

        // get booking by id
        AccommodationBookingDetailsResponseDto getBookingById(Long id, UserEntity currentUser);

        // get all bookings
        Page<AccommodationBookingOverviewResponseDto> getAllBookings(Pageable pageable, UserEntity currentUser);

        // get all bookings status
        AccommodationBookingStatusDTO getAllBookingStatus();
}
