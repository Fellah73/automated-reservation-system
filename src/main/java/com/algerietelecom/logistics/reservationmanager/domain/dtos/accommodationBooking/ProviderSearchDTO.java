package com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderSearchDTO {
    private Long id;
    private String logoUrl;
    private String name;

    private List<ProviderServicesDto> services;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderServicesDto {
        private Long id;
        private String roomType;
        private BigDecimal pricePerNight;
        private Boolean breakfastIncluded;
        private String amenities;
    }

}
