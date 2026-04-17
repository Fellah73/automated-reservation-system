package com.algerietelecom.logistics.reservationmanager.mappers;

import org.springframework.stereotype.Component;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationBookingEntity;

@Component
public class AccommodationBookingMapper {
    public AccommodationBookingResponseDto toResponseDto(AccommodationBookingEntity bookingEntity) {
        if (bookingEntity == null) {
            return null;
        }

        AccommodationBookingResponseDto responseDto = new AccommodationBookingResponseDto();
        responseDto.setId(bookingEntity.getId());
        responseDto.setRequestId(bookingEntity.getReservationRequest().getId());
        responseDto.setStatus(bookingEntity.getStatus().name());
        responseDto.setTotalAmount(bookingEntity.getTotalAmount());
        responseDto.setProviderId(bookingEntity.getProvider().getId());

        return responseDto;
    }
}
