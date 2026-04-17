package com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationBookingRequestDto {

    @NotNull(message = "The mission request ID is mandatory")
    private Long requestId;

    @NotNull(message = "The provider ID is mandatory")
    private Long providerId;

    @NotEmpty(message = "You must select at least one room")
    private List<BookingItemRequestDto> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingItemRequestDto {

        @NotNull(message = "The service ID is mandatory")
        private Long serviceId;

        @NotNull(message = "The number of rooms is mandatory")
        private Integer count;
    }
}
