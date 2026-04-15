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
@Table(name = "flight_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String route;
    private BigDecimal price;
    private String cabinClass;
    private Integer baggageAllowance;
    private Boolean isDirect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private ProviderContractEntity contract;
}
