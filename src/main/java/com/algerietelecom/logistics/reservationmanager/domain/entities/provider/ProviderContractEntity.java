package com.algerietelecom.logistics.reservationmanager.domain.entities.provider;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AccommodationServiceEntity> accommodationServices = new ArrayList<>();

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FlightServiceEntity> flightServices = new ArrayList<>();

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CateringServiceEntity> cateringServices = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isValid() {
        LocalDate now = LocalDate.now();
        return (now.isEqual(startDate) || now.isAfter(startDate)) &&
                (now.isEqual(endDate) || now.isBefore(endDate));
    }

    public void addAccommodationService(AccommodationServiceEntity service) {
        accommodationServices.add(service);
        service.setContract(this);
    }

    public void addFlightService(FlightServiceEntity service) {
        flightServices.add(service);
        service.setContract(this);
    }

    public void addCateringService(CateringServiceEntity service) {
        cateringServices.add(service);
        service.setContract(this);
    }
}
