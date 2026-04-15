package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ReservationWithHistoryResponseDTO {
    private Long id;
    private String requestNumber;
    private String missionType;
    private String destination;
    private String employeeName; 
    private Integer peopleCount; 
    private LocalDateTime createdAt;

    private List<ReservationHistoryResponseDTO> fullHistory;
}