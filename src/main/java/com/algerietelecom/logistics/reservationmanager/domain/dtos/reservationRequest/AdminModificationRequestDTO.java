package com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AdminModificationRequestDTO {
    private String globalComment;

    private List<FieldUpdateDetail> fieldsToUpdate;

    @Data
    @AllArgsConstructor
    public static class FieldUpdateDetail {
        private String fieldName;
        private String instruction;
    }
}
