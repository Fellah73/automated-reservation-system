package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationRequestResponseDTO {
    private Long id;
    private String requestNumber;
    private String missionType;
    private String destination;
    private String reason;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private String status;
    private String modificationInstructions;
    private String modificationAttachmentInstructions;
    private List<ParticipantResponseDTO> participants;
    private PreferenceResponseDTO preferences;
    private List<ReservationAttachmentDTO> attachments;

    @Data
    public static class ParticipantResponseDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private String employeeId;
        private String role;
        private String department;
    }

    @Data
    public static class PreferenceResponseDTO {
        private Long id;
        private String flightPreference;
        private String hotelPreference;
        private String mealPreference;
        private String transportType;
    }

    public void setAttachments(List<ReservationAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

}