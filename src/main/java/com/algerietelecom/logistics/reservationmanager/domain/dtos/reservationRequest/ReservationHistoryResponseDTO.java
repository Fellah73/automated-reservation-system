package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationHistoryResponseDTO {
    private Long id;
    private String action; 
    private String performerName; 
    private String previousStatus;
    private String newStatus;
    private String adminComment;
    private LocalDateTime timestamp;
    private AdminModificationRequestDTO modificationInstructions;
}