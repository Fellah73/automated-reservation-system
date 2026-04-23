package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproverReservationRequestOverviewApiResponse {
    private Page<ReservationRequestOverviewDTO> reservationRequests;
    private ReservationRequestsStatsDTO stats;

    // Getters and Setters

}
