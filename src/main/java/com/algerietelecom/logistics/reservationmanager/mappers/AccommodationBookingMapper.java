package com.algerietelecom.logistics.reservationmanager.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingDetailsResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingOverviewResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.ProviderSearchDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.ProviderSearchDTO.ProviderServicesDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.UnbookedReservationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationBookingEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationReservationEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderContractEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices.AccommodationServiceEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;

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

        // request details
        AccommodationBookingDetailsResponseDto.RequestDetailsDto requestDetails = new AccommodationBookingDetailsResponseDto.RequestDetailsDto();
        requestDetails.setId(bookingEntity.getReservationRequest().getId());
        requestDetails.setRequestNumber(bookingEntity.getReservationRequest().getRequestNumber());
        requestDetails.setMissionType(bookingEntity.getReservationRequest().getMissionType());
        requestDetails.setDestination(bookingEntity.getReservationRequest().getDestination());
        requestDetails.setPurpose(bookingEntity.getReservationRequest().getReason());
        requestDetails.setDepartureDate(bookingEntity.getReservationRequest().getDepartureDate().toString());
        requestDetails.setReturnDate(bookingEntity.getReservationRequest().getReturnDate().toString());
        requestDetails.setPeopleCount(bookingEntity.getReservationRequest().getParticipants().size());
        requestDetails.setDuration(bookingEntity.getReservationRequest().getDuration());
        responseDto.setRequest(requestDetails);

        // provider details
        AccommodationBookingDetailsResponseDto.ProviderDetailsDto providerDetails = new AccommodationBookingDetailsResponseDto.ProviderDetailsDto();

        providerDetails.setId(provider.getId());
        providerDetails.setName(provider.getName());
        providerDetails.setAddress(provider.getAddress());
        providerDetails.setLogoUrl(provider.getLogoUrl());

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

    public ProviderSearchDTO toProviderSearchDto(ProviderEntity providerEntity) {
        if (providerEntity == null) {
            return null;
        }

        ProviderSearchDTO dto = new ProviderSearchDTO();
        dto.setId(providerEntity.getId());
        dto.setName(providerEntity.getName());
        dto.setLogoUrl(providerEntity.getLogoUrl());

        ProviderContractEntity providerActiveContracts = providerEntity.getContracts().get(0);

        List<AccommodationServiceEntity> services = providerActiveContracts.getAccommodationServices();

        List<ProviderServicesDto> dtoServices = services.stream()
                .map(service -> {
                    ProviderServicesDto serviceDto = new ProviderServicesDto();
                    serviceDto.setId(service.getId());
                    serviceDto.setRoomType(service.getRoomType());
                    serviceDto.setPricePerNight(service.getPricePerNight());
                    serviceDto.setBreakfastIncluded(service.getBreakfastIncluded());
                    serviceDto.setAmenities(service.getAmenities());
                    serviceDto.setCapacity(service.getCapacity());
                    return serviceDto;
                })
                .toList();

        dto.setServices(dtoServices);
        return dto;
    }

    public UnbookedReservationRequestDTO toReservationRequestListDto(ReservationRequest reservationRequestEntity) {
        if (reservationRequestEntity == null) {
            return null;
        }

        UnbookedReservationRequestDTO dto = new UnbookedReservationRequestDTO();
        dto.setRequestId(reservationRequestEntity.getId());
        dto.setRequestNumber(reservationRequestEntity.getRequestNumber());
        dto.setMissionType(reservationRequestEntity.getMissionType());
        dto.setDepartureDate(reservationRequestEntity.getDepartureDate());
        dto.setReturnDate(reservationRequestEntity.getReturnDate());
        dto.setPeopleCount(reservationRequestEntity.getParticipants().size());

        return dto;
    }

}
