package com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices;

import java.math.BigDecimal;

import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderContractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "catering_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CateringServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;
    private BigDecimal pricePerPerson;
    private String mealType;
    private Boolean drinksIncluded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private ProviderContractEntity contract;
}