package com.algerietelecom.logistics.reservationmanager.mappers;

import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequestPreference;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationRequest.HistoryAction;

import tools.jackson.databind.ObjectMapper;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.AdminModificationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationHistoryResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestCreateDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestOverviewDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestUpdateDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationWithHistoryResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationAttachment;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationHistory;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationParticipant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationRequestMapper {

    private final ObjectMapper objectMapper;

    public ReservationRequestMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ReservationRequest toEntity(ReservationRequestCreateDTO dto) {
        if (dto == null)
            return null;

        ReservationRequest entity = ReservationRequest.builder()
                .missionType(dto.getMissionType())
                .destination(dto.getDestination())
                .departureDate(dto.getDepartureDate())
                .returnDate(dto.getReturnDate())
                .reason(dto.getReason())
                .build();

        if (dto.getParticipants() != null) {
            entity.setParticipants(dto.getParticipants().stream()
                    .map(p -> ReservationParticipant.builder()
                            .firstName(p.getFirstName())
                            .lastName(p.getLastName())
                            .employeeId(p.getEmployeeId())
                            .role(p.getRole())
                            .department(p.getDepartment())
                            .reservationRequest(entity)
                            .build())
                    .collect(Collectors.toList()));
        }

        if (dto.getPreferences() != null) {
            ReservationRequestPreference prefEntity = ReservationRequestPreference.builder()
                    .flightPreference(dto.getPreferences().getFlightPreference())
                    .hotelPreference(dto.getPreferences().getHotelPreference())
                    .mealPreference(dto.getPreferences().getMealPreference())
                    .transportType(dto.getPreferences().getTransportType())
                    .reservationRequest(entity)
                    .build();

            entity.setPreferences(prefEntity);
        }

        return entity;
    }

    public ReservationRequestResponseDTO toResponseDTO(ReservationRequest entity,
            List<ReservationAttachment> requestAttachments) {
        if (entity == null)
            return null;

        ReservationRequestResponseDTO dto = new ReservationRequestResponseDTO();
        dto.setId(entity.getId());
        dto.setRequestNumber(entity.getRequestNumber());
        dto.setMissionType(entity.getMissionType());
        dto.setDestination(entity.getDestination());
        dto.setReason(entity.getReason());
        dto.setDepartureDate(entity.getDepartureDate());
        dto.setReturnDate(entity.getReturnDate());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setModificationInstructions(entity.getModificationInstructions());
        dto.setModificationAttachmentInstructions(entity.getModificationAttachmentInstructions());

        if (entity.getParticipants() != null) {
            dto.setParticipants(entity.getParticipants().stream().map(p -> {
                ReservationRequestResponseDTO.ParticipantResponseDTO pDto = new ReservationRequestResponseDTO.ParticipantResponseDTO();
                pDto.setId(p.getId());
                pDto.setFirstName(p.getFirstName());
                pDto.setLastName(p.getLastName());
                pDto.setEmployeeId(p.getEmployeeId());
                pDto.setRole(p.getRole());
                pDto.setDepartment(p.getDepartment());
                return pDto;
            }).collect(Collectors.toList()));
        }

        if (entity.getPreferences() != null) {
            ReservationRequestResponseDTO.PreferenceResponseDTO prefDto = new ReservationRequestResponseDTO.PreferenceResponseDTO();
            prefDto.setId(entity.getPreferences().getId());
            prefDto.setFlightPreference(entity.getPreferences().getFlightPreference());
            prefDto.setHotelPreference(entity.getPreferences().getHotelPreference());
            prefDto.setMealPreference(entity.getPreferences().getMealPreference());
            prefDto.setTransportType(entity.getPreferences().getTransportType());
            dto.setPreferences(prefDto);
        }

        if (requestAttachments != null) {
            dto.setAttachments(requestAttachments.stream().map(att -> {
                ReservationAttachmentDTO attDto = new ReservationAttachmentDTO();
                attDto.setFileName(att.getFileName());
                attDto.setFileType(att.getFileType());
                attDto.setFilePath(att.getFilePath());
                attDto.setCategory(att.getCategory() != null ? att.getCategory().name() : null);
                return attDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }

    public ReservationRequestOverviewDTO toOverviewDTO(ReservationRequest entity) {
        if (entity == null)
            return null;

        ReservationRequestOverviewDTO dto = new ReservationRequestOverviewDTO();
        dto.setId(entity.getId());
        dto.setRequestNumber(entity.getRequestNumber());
        dto.setMissionType(entity.getMissionType());
        dto.setDestination(entity.getDestination());
        dto.setReason(entity.getReason());
        dto.setDepartureDate(entity.getDepartureDate());
        dto.setReturnDate(entity.getReturnDate());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setModificationInstructions(entity.getModificationInstructions());
        dto.setModificationAttachmentInstructions(entity.getModificationAttachmentInstructions());

        if (entity.getParticipants() != null) {
            dto.setPeopleCount(entity.getParticipants().size());
        }

        return dto;
    }

    public ReservationAttachmentDTO toAttachmentDTO(ReservationAttachment entity) {
        if (entity == null)
            return null;

        ReservationAttachmentDTO dto = new ReservationAttachmentDTO();
        dto.setFileName(entity.getFileName());
        dto.setFileType(entity.getFileType());
        dto.setFilePath(entity.getFilePath());
        dto.setCategory(entity.getCategory() != null ? entity.getCategory().name() : null);

        return dto;
    }

    public ReservationHistoryResponseDTO toHistoryResponseDTO(ReservationHistory entity) {
        if (entity == null)
            return null;

        ReservationHistoryResponseDTO dto = new ReservationHistoryResponseDTO();
        dto.setId(entity.getId());
        dto.setAction(entity.getAction().name());
        dto.setPerformerName(entity.getPerformedBy() != null
                ? entity.getPerformedBy().getUsername()
                : null);

        dto.setPreviousStatus(entity.getPreviousStatus() != null ? entity.getPreviousStatus().name() : null);
        dto.setNewStatus(entity.getNewStatus().name());
        dto.setAdminComment(entity.getAdminComment());
        dto.setTimestamp(entity.getTimestamp());

        if (entity.getAction() == HistoryAction.MODIFICATION_REQUESTED
                && entity.getModificationInstructions() != null) {
            try {
                AdminModificationRequestDTO modificationRequestDTO = objectMapper.readValue(
                        entity.getModificationInstructions(),
                        AdminModificationRequestDTO.class);

                dto.setModificationInstructions(modificationRequestDTO);
            } catch (Exception e) {
                System.err.println("Reading modification instructions failed: " + e.getMessage());
            }
        }

        return dto;
    }

    public ReservationWithHistoryResponseDTO toWithHistoryResponseDTO(ReservationRequest entity) {
        if (entity == null)
            return null;

        ReservationWithHistoryResponseDTO dto = new ReservationWithHistoryResponseDTO();
        dto.setId(entity.getId());
        dto.setRequestNumber(entity.getRequestNumber());
        dto.setMissionType(entity.getMissionType());
        dto.setDestination(entity.getDestination());
        dto.setPeopleCount(entity.getPeopleCount());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getHistory() != null) {
            dto.setHistoryLength(entity.getHistory().size());
        }

        return dto;
    }

    public ReservationParticipant toParticipantEntity(ReservationRequestUpdateDTO.ParticipantDTO dto,
            ReservationRequest request) {
        if (dto == null)
            return null;

        ReservationParticipant entity = new ReservationParticipant();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setRole(dto.getRole());
        entity.setDepartment(dto.getDepartment());
        entity.setRequest(request);

        return entity;
    }
}
