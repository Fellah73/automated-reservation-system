package com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_request_id", nullable = false)
    @JsonBackReference
    private ReservationRequest reservationRequest;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String employeeId;

    private String role;
    private String department;

    public void setRequest(ReservationRequest reservationRequest) {
        this.reservationRequest = reservationRequest;
    }
}