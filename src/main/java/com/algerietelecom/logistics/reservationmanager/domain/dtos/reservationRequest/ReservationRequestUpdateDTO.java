package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ReservationRequestUpdateDTO {
    @Nullable
    private String destination;

    @Nullable
    private LocalDateTime departureDate;

    @Nullable
    private LocalDateTime returnDate;

    @Nullable
    private String reason;

    @Nullable
    private List<ParticipantDTO> participants;

    @Nullable
    private PreferenceDTO preferences;

    @Data
    public static class ParticipantDTO {
        @Nullable
        private String firstName;

        @Nullable
        private String lastName;

        @Nullable
        private String employeeId;

        @Nullable
        private String role;

        @Nullable
        private String department;
    }

    @Data
    public static class PreferenceDTO {
        @Nullable
        private String flightPreference;

        @Nullable
        private String hotelPreference;

        @Nullable
        private String mealPreference;

        @Nullable
        private String transportType;
    }
}
