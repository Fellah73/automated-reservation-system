package com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationBookingDetailsResponseDto {
    private Long id;
    private String status;
    private Double totalAmount;

    private RequestDetailsDto request;

    private ProviderDetailsDto provider;

    private List<BookingItemDetailsDto> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestDetailsDto {
        private Long id;
        private String requestNumber;
        private String missionType;
        private String destination;
        private String purpose;
        private String departureDate;
        private String returnDate;
        private Integer peopleCount;
        private Integer duration;
    }

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
    public static class BookingItemDetailsDto {
        private Long reservationId;
        private Integer count;

        private Long serviceId;
        private String serviceName;
        private Integer capacity;
        private BigDecimal pricePerNight;
    }
}
