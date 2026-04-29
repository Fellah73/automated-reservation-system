package com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationBookingOverviewResponseDto {
    private Long id;
    private String status;
    private Double totalAmount;

    private ReservationRequestDetails request;
    private ProviderDetailsDto provider;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderDetailsDto {
        private Long id;
        private String name;
        private String address;
        private String logoUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationRequestDetails {
        private Long id;
        private String requestNumber;
        private String missionType;
        private String destination;
        private Integer peopleCount;

    }
}
