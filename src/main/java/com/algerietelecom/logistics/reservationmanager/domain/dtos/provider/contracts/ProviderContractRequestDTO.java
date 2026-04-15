package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProviderContractRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private String pdfUrl;
    private String fileName;
    private String termsAndConditions;

}
