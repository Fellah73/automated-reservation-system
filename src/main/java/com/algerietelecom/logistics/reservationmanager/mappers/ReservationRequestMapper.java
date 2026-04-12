package com.algerietelecom.logistics.reservationmanager.mappers;

import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequestPreference;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestCreateDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestOverviewDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationAttachment;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationParticipant;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReservationRequestMapper {

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

    public ReservationRequestResponseDTO toResponseDTO(ReservationRequest entity) {
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

        return dto;
    }
}
