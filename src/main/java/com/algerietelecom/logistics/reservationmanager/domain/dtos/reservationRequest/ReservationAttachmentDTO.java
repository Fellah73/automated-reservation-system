package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationAttachmentDTO {

    private Long id;

    private String fileName;

    private String fileType;

    private String fileUrl;

    private LocalDateTime uploadedAt;

    private String category;
}