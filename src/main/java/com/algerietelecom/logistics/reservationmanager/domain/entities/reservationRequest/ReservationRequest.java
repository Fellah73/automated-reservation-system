package com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationBookingEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationRequest.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservation_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String requestNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity createdBy;

    private String missionType;
    private String destination;
    private String reason;

    private LocalDateTime departureDate;
    private LocalDateTime returnDate;

    private Integer duration;
    private Integer peopleCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;

    private LocalDateTime submissionDate;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT")
    private String modificationInstructions;
    
    @Column(columnDefinition = "TEXT")
    private String modificationAttachmentInstructions;

    // relations
    @Builder.Default
    @OneToMany(mappedBy = "reservationRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ReservationParticipant> participants = new ArrayList<>();

    public void setParticipants(List<ReservationParticipant> participants) {
        this.participants = participants;
        if (participants != null) {
            participants.forEach(p -> p.setReservationRequest(this));
        }
    }

    @OneToOne(mappedBy = "reservationRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private ReservationRequestPreference preferences;

    public void setPreferences(ReservationRequestPreference preferences) {
        this.preferences = preferences;
        if (preferences != null) {
            preferences.setReservationRequest(this);
        }
    }

    @Builder.Default
    @OneToMany(mappedBy = "reservationRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationAttachment> attachments = new ArrayList<>();

    @OneToMany(mappedBy = "reservationRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("timestamp DESC")
    private List<ReservationHistory> history;

    @OneToOne(mappedBy = "reservationRequest", cascade = CascadeType.ALL)
    private AccommodationBookingEntity accommodationBooking;

    @Column(columnDefinition = "TEXT")
    private String adminComment;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}