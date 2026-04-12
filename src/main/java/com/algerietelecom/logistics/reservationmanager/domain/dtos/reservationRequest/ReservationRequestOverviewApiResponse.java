package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestOverviewApiResponse {
    private List<ReservationRequestOverviewDTO> reservationRequests;
    private ReservationRequestsStatsDTO stats;

    // Getters and Setters
}
