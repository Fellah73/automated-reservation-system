package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationRequestCreateDTO {

    private String missionType;
    private String destination;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private String reason;

    private List<ParticipantDTO> participants;

    private PreferenceDTO preferences;


    @Data
    public static class ParticipantDTO {
        private String firstName;
        private String lastName;
        private String employeeId; 
        private String role;
        private String department;
    }

    @Data
    public static class PreferenceDTO {
        private String flightPreference;
        private String hotelPreference;
        private String mealPreference;
        private String transportType;
    }
}