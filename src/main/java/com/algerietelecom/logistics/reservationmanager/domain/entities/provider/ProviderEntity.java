package com.algerietelecom.logistics.reservationmanager.domain.entities.provider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationBookingEntity;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.Provider.ProviderType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderType type;

    @Column(unique = true)
    private String nif;

    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String contactPerson;
    private String logoUrl;

    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<ProviderContractEntity> contracts;

    @OneToOne(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProviderEvaluationEntity evaluation;

    @Builder.Default
    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    private List<AccommodationBookingEntity> bookings = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (active == null)
            active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
