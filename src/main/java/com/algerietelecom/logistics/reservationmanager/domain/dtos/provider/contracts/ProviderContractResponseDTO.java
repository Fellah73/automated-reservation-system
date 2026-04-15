package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProviderContractResponseDTO {
    private Long id;
    private String contractNumber;
    private String startDate;
    private String endDate;
    private String contractUrl;
    private String fileName;
    private String termsAndConditions;

}
