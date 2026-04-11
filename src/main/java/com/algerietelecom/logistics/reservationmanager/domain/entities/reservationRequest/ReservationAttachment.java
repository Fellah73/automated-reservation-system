package com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;

@Entity
@Table(name = "reservation_attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_request_id", nullable = false)
    private ReservationRequest reservationRequest;

    private String fileName;
    private String filePath;
    private String fileType;
    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id")
    private UserEntitiy uploadedBy;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
