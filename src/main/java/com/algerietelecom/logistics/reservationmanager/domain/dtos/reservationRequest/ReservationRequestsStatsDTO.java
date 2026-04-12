package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationRequestsStatsDTO {
    private long totalRequests;
    private long pendingCount;
    private long acceptedCount;
    private long rejectedCount;
    private long toUpdateCount;
}