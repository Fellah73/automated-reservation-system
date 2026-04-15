package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts;

import java.time.LocalDate;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProviderContractUpdateRequestDTO {
    @Nullable
    private LocalDate startDate;

    @Nullable
    private LocalDate endDate;

    @Nullable
    private String pdfUrl;

    @Nullable
    private String fileName;

    @Nullable
    private String termsAndConditions;
}
