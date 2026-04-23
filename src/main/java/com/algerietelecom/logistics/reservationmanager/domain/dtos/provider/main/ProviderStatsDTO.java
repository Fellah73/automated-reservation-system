package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderStatsDTO {
    private long totalProviders;
    private long totalHotels;
    private long totalAirlines;
    private long totalCatering;
 }
