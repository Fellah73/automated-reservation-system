package com.algerietelecom.logistics.reservationmanager.domain.entities.provider;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices.AccommodationServiceEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices.CateringServiceEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices.FlightServiceEntity;

@Entity
@Table(name = "provider_contracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_number", nullable = false, unique = true)
    private String contractNumber;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "contract_url", length = 500)
    private String contractUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ProviderEntity provider;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL)
    private AccommodationServiceEntity accommodationService;

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL)
    private FlightServiceEntity flightService;

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL)
    private CateringServiceEntity cateringService;

    public boolean hasValidServiceConfiguration() {
        int count = 0;
        if (accommodationService != null)
            count++;
        if (flightService != null)
            count++;
        if (cateringService != null)
            count++;
        return count == 1;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isValid() {
        LocalDate now = LocalDate.now();
        return (now.isEqual(startDate) || now.isAfter(startDate)) &&
                (now.isEqual(endDate) || now.isBefore(endDate));
    }
}
