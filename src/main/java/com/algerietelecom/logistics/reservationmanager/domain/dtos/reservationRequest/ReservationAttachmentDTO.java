package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationAttachmentDTO {
    private String fileName;
    private String fileType;
    private String filePath;
    private String category;
}