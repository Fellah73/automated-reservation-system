package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import lombok.Data;
import java.util.List;

@Data
public class ReservationAttachmentBatchDTO {
    private List<ReservationAttachmentDTO> attachments;
}