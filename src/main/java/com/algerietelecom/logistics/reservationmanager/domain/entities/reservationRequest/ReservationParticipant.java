package com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest;

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
    private ReservationRequest reservationRequest;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String employeeId;

    private String role;
    private String department;
}