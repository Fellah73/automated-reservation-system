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
public class FlightServiceDTO {
    private Long id;
    private String route;           
    private String cabinClass;      
    private BigDecimal price;
    private Integer baggageAllowance;
    private Boolean isDirect;
}
