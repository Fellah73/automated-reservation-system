package com.algerietelecom.logistics.reservationmanager.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingDetailsResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingOverviewResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationBookingEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationReservationEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;

@Component
public class AccommodationBookingMapper {
    public AccommodationBookingResponseDto toResponseDto(AccommodationBookingEntity bookingEntity) {
        if (bookingEntity == null) {
            return null;
        }

        AccommodationBookingResponseDto responseDto = new AccommodationBookingResponseDto();
        responseDto.setId(bookingEntity.getId());
        responseDto.setRequestId(bookingEntity.getReservationRequest().getId());
        responseDto.setStatus(bookingEntity.getStatus().name());
        responseDto.setTotalAmount(bookingEntity.getTotalAmount());
        responseDto.setProviderId(bookingEntity.getProvider().getId());

        return responseDto;
    }

    public AccommodationBookingDetailsResponseDto toDetailsResponseDto(AccommodationBookingEntity bookingEntity) {
        if (bookingEntity == null) {
            return null;
        }

        List<AccommodationReservationEntity> reservations = bookingEntity.getItems();
        ProviderEntity provider = bookingEntity.getProvider();

        AccommodationBookingDetailsResponseDto responseDto = new AccommodationBookingDetailsResponseDto();
        responseDto.setId(bookingEntity.getId());
        responseDto.setStatus(bookingEntity.getStatus().name());
        responseDto.setTotalAmount(bookingEntity.getTotalAmount());
        responseDto.setRequestId(bookingEntity.getReservationRequest().getId());

        // provider details
        AccommodationBookingDetailsResponseDto.ProviderDetailsDto providerDetails = new AccommodationBookingDetailsResponseDto.ProviderDetailsDto();

        providerDetails.setId(provider.getId());
        providerDetails.setName(provider.getName());
        providerDetails.setAddress(provider.getAddress());

        responseDto.setProvider(providerDetails);

        List<AccommodationBookingDetailsResponseDto.BookingItemDetailsDto> itemDtos = reservations.stream()
                .map(reservation -> {
                    AccommodationBookingDetailsResponseDto.BookingItemDetailsDto itemDto = new AccommodationBookingDetailsResponseDto.BookingItemDetailsDto();
                    itemDto.setReservationId(reservation.getId());
                    itemDto.setCount(reservation.getCount());
                    itemDto.setServiceId(reservation.getAccommodationService().getId());
                    itemDto.setServiceName(reservation.getAccommodationService().getRoomType());
                    itemDto.setCapacity(reservation.getAccommodationService().getCapacity());
                    itemDto.setPricePerNight(reservation.getAccommodationService().getPricePerNight());
                    return itemDto;
                })
                .toList();

        responseDto.setItems(itemDtos);
        return responseDto;
    }

    public AccommodationBookingOverviewResponseDto toOverviewResponseDto(AccommodationBookingEntity bookingEntity) {
        if (bookingEntity == null) {
            return null;
        }

        AccommodationBookingOverviewResponseDto responseDto = new AccommodationBookingOverviewResponseDto();
        responseDto.setId(bookingEntity.getId());
        responseDto.setStatus(bookingEntity.getStatus().name());
        responseDto.setTotalAmount(bookingEntity.getTotalAmount());

        // provider details
        AccommodationBookingOverviewResponseDto.ProviderDetailsDto providerDetails = new AccommodationBookingOverviewResponseDto.ProviderDetailsDto();
        providerDetails.setId(bookingEntity.getProvider().getId());
        providerDetails.setName(bookingEntity.getProvider().getName());
        providerDetails.setAddress(bookingEntity.getProvider().getAddress());
        providerDetails.setLogoUrl(bookingEntity.getProvider().getLogoUrl());
        responseDto.setProvider(providerDetails);

        // reservation request details
        AccommodationBookingOverviewResponseDto.ReservationRequestDetails requestDetails = new AccommodationBookingOverviewResponseDto.ReservationRequestDetails();
        requestDetails.setId(bookingEntity.getReservationRequest().getId());
        requestDetails.setRequestNumber(bookingEntity.getReservationRequest().getRequestNumber());
        requestDetails.setMissionType(bookingEntity.getReservationRequest().getMissionType());
        requestDetails.setDestination(bookingEntity.getReservationRequest().getDestination());
        requestDetails.setPeopleCount(bookingEntity.getReservationRequest().getParticipants().size());
        responseDto.setRequest(requestDetails);

        return responseDto;
    }
}
