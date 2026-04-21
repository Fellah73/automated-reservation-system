package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReservationRequestOverviewDTO {
    private Long id;
    private String requestNumber;
    private String missionType;
    private String destination;
    private String reason;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private String status;
    private Integer peopleCount;
    private String modificationInstructions;
    private String modificationAttachmentInstructions;

}