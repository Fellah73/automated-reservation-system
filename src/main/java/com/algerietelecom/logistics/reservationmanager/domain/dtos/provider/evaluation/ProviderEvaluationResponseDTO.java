package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.evaluation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderEvaluationResponseDTO {
    @NotNull(message = "Le score de délai est obligatoire")
    private Integer delayRating;

    @NotNull(message = "Le score de qualité est obligatoire")
    private Integer serviceQualityRating;

    @NotNull(message = "Le score de conformité est obligatoire")
    private Integer conformityRating;

    @NotNull(message = "Le score de coût est obligatoire")
    private Integer costRating;

    private Integer overallRating;

    private String comment;
}
