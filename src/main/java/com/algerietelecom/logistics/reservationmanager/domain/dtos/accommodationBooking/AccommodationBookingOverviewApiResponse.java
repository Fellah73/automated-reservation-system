package com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationBookingOverviewApiResponse {
    private Page<AccommodationBookingOverviewResponseDto> bookings;
    private AccommodationBookingStatusDTO bookingStatus;

}
