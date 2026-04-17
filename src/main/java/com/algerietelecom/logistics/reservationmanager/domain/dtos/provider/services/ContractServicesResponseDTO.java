package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.services;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractServicesResponseDTO {
    private Long contractId;
    private String providerName;
    private String providerType;

    private List<AccommodationServiceDTO> accommodations;
    private List<CateringServiceDTO> catering;
    private List<FlightServiceDTO> flights;
}
