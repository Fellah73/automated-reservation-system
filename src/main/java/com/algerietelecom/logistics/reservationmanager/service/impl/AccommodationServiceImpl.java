package com.algerietelecom.logistics.reservationmanager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingRequestDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationBookingEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationReservationEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices.AccommodationServiceEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.AccommodationBooking.BookingStatus;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.Provider.ProviderType;
import com.algerietelecom.logistics.reservationmanager.mappers.AccommodationBookingMapper;
import com.algerietelecom.logistics.reservationmanager.mappers.ProviderMapper;
import com.algerietelecom.logistics.reservationmanager.repositories.accommodation.AccommodationBookingRepository;
import com.algerietelecom.logistics.reservationmanager.repositories.provider.AccommodationServiceRepository;
import com.algerietelecom.logistics.reservationmanager.repositories.provider.ProviderRepository;
import com.algerietelecom.logistics.reservationmanager.repositories.reservation.ReservationRequestRepository;
import com.algerietelecom.logistics.reservationmanager.service.AccommodationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final ProviderRepository providerRepository;
    private final ReservationRequestRepository reservationRequestRepository;
    private final AccommodationServiceRepository accommodationServiceRepository;
    private final AccommodationBookingRepository accommodationBookingRepository;
    private final ProviderMapper providerMapper;
    private final AccommodationBookingMapper bookingMapper;

    // get hostel providers by city
    @Override
    @Transactional
    public List<CreateProviderDto> searchAccommodationsProvidersByCity(String city) {
        try {
            List<ProviderEntity> providers;
            if (city != null) {
                providers = providerRepository.findByCityAndType(city, ProviderType.HOTEL);
            } else {
                providers = providerRepository.findByType(ProviderType.HOTEL);
            }

            List<CreateProviderDto> providerDtos = providers.stream()
                    .filter(provider -> provider.getContracts() != null && !provider.getContracts().isEmpty())
                    .map(providerMapper::toDto)
                    .toList();

            return providerDtos;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // create booking for accommodation
    @Override
    @Transactional
    public AccommodationBookingResponseDto createBooking(AccommodationBookingRequestDto bookingRequestDto,
            UserEntitiy currentUser) {
        try {
            // reseration check
            ReservationRequest reservationRequest = reservationRequestRepository
                    .findById(bookingRequestDto.getRequestId())
                    .orElseThrow(() -> new RuntimeException(
                            "Reservation request not found with id: " + bookingRequestDto.getRequestId()));

            // provider exists
            ProviderEntity provider = providerRepository.findById(bookingRequestDto.getProviderId())
                    .orElseThrow(() -> new RuntimeException(
                            "Provider not found with id: " + bookingRequestDto.getProviderId()));

            AccommodationBookingEntity bookingEntity = new AccommodationBookingEntity();
            bookingEntity.setReservationRequest(reservationRequest);
            bookingEntity.setProvider(provider);
            bookingEntity.setStatus(BookingStatus.PENDING);
            bookingEntity.setItems(new ArrayList<>());

            Double[] totalAmount = { 0.0 };

            bookingRequestDto.getItems().forEach(itemDto -> {
                AccommodationReservationEntity reservationEntity = new AccommodationReservationEntity();
                AccommodationServiceEntity service = accommodationServiceRepository.findById(itemDto.getServiceId())
                        .orElseThrow(() -> new RuntimeException(
                                "Accommodation service not found with id: " + itemDto.getServiceId()));

                // calculate total amount
                totalAmount[0] += itemDto.getCount().doubleValue() * service.getPricePerNight().doubleValue();

                reservationEntity.setAccommodationService(service);
                reservationEntity.setCount(itemDto.getCount());
                reservationEntity.setBooking(bookingEntity);

                bookingEntity.getItems().add(reservationEntity);
            });

            bookingEntity.setTotalAmount(totalAmount[0]);
            bookingEntity.setCheckInDate(reservationRequest.getDepartureDate().toLocalDate());
            bookingEntity.setCheckOutDate(reservationRequest.getReturnDate().toLocalDate());

            AccommodationBookingEntity bookingReservation = accommodationBookingRepository.save(bookingEntity);

            AccommodationBookingResponseDto responseDto = bookingMapper.toResponseDto(bookingReservation);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}