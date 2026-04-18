package com.algerietelecom.logistics.reservationmanager.service;

import java.util.List;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingDetailsResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingRequestDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;

public interface AccommodationService {
    // search hotels by city
    List<CreateProviderDto> searchAccommodationsProvidersByCity(String city);

    // create booking for accommodation
    AccommodationBookingResponseDto createBooking(AccommodationBookingRequestDto bookingRequestDto,
            UserEntitiy currentUser);

    // update booking for accommodation
    AccommodationBookingResponseDto updateBooking(Long id, AccommodationBookingRequestDto bookingRequestDto,
            UserEntitiy currentUser);

    // get booking by id
    AccommodationBookingDetailsResponseDto getBookingById(Long id, UserEntitiy currentUser);
}
