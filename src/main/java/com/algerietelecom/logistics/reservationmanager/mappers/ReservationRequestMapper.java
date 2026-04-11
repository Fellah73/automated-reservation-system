package com.algerietelecom.logistics.reservationmanager.mappers;

import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequestPreference;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestCreateDTO;
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
}
