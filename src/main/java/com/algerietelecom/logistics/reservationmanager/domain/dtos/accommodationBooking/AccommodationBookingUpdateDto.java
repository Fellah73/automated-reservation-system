package com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationBookingUpdateDto {
    @NotNull(message = "The Booking ID is mandatory for update")
    private Long id; 

    @NotNull(message = "The provider ID is mandatory")
    private Long providerId;

    @NotEmpty(message = "You must select at least one room")
    private List<UpdateItemRequestDto> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateItemRequestDto {
        @NotNull(message = "The service ID is mandatory")
        private Long serviceId;

        @NotNull(message = "The number of rooms is mandatory")
        private Integer count;
    }
}
