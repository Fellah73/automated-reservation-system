package com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class UnbookedReservationRequestDTO {
    private Long requestId;
    private String requestNumber;
    private String missionType;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private Integer peopleCount;
}
