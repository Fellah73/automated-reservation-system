package com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationBookingResponseDto {
    private Long id;
    private Long requestId;
    private String status;
    private Double totalAmount;
    
    private Long providerId;
   
}
