package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.evaluation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProviderEvaluationRequestDTO {

    @NotNull(message = "Le score de délai est obligatoire")
    @Min(1)
    @Max(10)
    private Integer delayRating;

    @NotNull(message = "Le score de qualité est obligatoire")
    @Min(1)
    @Max(10)
    private Integer serviceQualityRating;

    @NotNull(message = "Le score de conformité est obligatoire")
    @Min(1)
    @Max(10)
    private Integer conformityRating;

    @NotNull(message = "Le score de coût est obligatoire")
    @Min(1)
    @Max(10)
    private Integer costRating; 

    private String comment; 
}
