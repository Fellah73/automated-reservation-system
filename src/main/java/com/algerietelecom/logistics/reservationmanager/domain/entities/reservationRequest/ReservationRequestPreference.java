package com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_request_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_request_id", nullable = false)
    @JsonBackReference
    private ReservationRequest reservationRequest;

    @Column(columnDefinition = "TEXT")
    private String flightPreference;

    @Column(columnDefinition = "TEXT")
    private String hotelPreference;

    @Column(columnDefinition = "TEXT")
    private String mealPreference;

    private String transportType;

    public void setRequest(ReservationRequest reservationRequest) {
        this.reservationRequest = reservationRequest;
    }
}