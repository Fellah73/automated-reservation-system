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
public class CateringServiceDTO {
    private Long id;
    private String menuName;
    private String mealType;
    private BigDecimal pricePerPerson;
    private Boolean drinksIncluded;
}