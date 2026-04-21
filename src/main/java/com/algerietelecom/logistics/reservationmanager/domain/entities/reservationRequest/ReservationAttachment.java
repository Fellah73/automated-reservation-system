package com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationRequest.AttachmentCategory;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttachmentCategory category;

    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id")
    private UserEntity uploadedBy;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
