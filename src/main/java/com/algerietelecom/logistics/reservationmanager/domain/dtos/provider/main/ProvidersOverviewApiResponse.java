package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvidersOverviewApiResponse {
    private Page<ProviderDto> providers;
    private ProviderStatsDTO stats;
}
