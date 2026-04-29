package com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class AccommodationBookingStatusDTO {
    private Long totalBookings;
    private Long totalRequestsWithoutBooking;
    private Long totalPendingRequests;
    private Double totalToPay;

}
