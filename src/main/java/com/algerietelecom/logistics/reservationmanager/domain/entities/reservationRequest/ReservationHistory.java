package com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.HistoryAction;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationStatus;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_request_id", nullable = false)
    private ReservationRequest reservationRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntitiy performedBy;

    @Enumerated(EnumType.STRING)
    private HistoryAction action;

    @Enumerated(EnumType.STRING)
    private ReservationStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private ReservationStatus newStatus;

    @Column(columnDefinition = "TEXT")
    private String adminComment;

    @Column(name = "modification_instructions", columnDefinition = "TEXT")
    private String modificationInstructions;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
