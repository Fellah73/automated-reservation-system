package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.services;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationServiceDTO {
    private Long id;
    private String roomType;
    private BigDecimal pricePerNight;
    private Boolean breakfastIncluded;
    private String amenities;
}