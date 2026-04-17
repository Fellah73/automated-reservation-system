package com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices;

import java.math.BigDecimal;
import java.util.List;

import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationReservationEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderContractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accommodation_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomType;
    private BigDecimal pricePerNight;
    private Boolean breakfastIncluded;
    private String amenities;
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private ProviderContractEntity contract;

    @OneToMany(mappedBy = "accommodationService")
    private List<AccommodationReservationEntity> reservations;
}
