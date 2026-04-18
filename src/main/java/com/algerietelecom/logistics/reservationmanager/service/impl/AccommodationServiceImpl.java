package com.algerietelecom.logistics.reservationmanager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingDetailsResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingRequestDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.accommodationBooking.AccommodationBookingResponseDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationBookingEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.accommodation.AccommodationReservationEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;
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
            UserEntity currentUser) {
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
            Double[] totalCount = { 0.0 };

            bookingRequestDto.getItems().forEach(itemDto -> {
                AccommodationReservationEntity reservationEntity = new AccommodationReservationEntity();
                AccommodationServiceEntity service = accommodationServiceRepository.findById(itemDto.getServiceId())
                        .orElseThrow(() -> new RuntimeException(
                                "Accommodation service not found with id: " + itemDto.getServiceId()));

                // calculate total amount
                totalAmount[0] += itemDto.getCount().doubleValue() * service.getPricePerNight().doubleValue();

                // calculate total count
                totalCount[0] += itemDto.getCount().doubleValue() * service.getCapacity().doubleValue();

                reservationEntity.setAccommodationService(service);
                reservationEntity.setCount(itemDto.getCount());
                reservationEntity.setBooking(bookingEntity);

                bookingEntity.getItems().add(reservationEntity);
            });

            if (reservationRequest.getParticipants().size() != totalCount[0]) {
                throw new RuntimeException(
                        "Total count of accommodation services doesn't match the number of participants in the reservation request");
            }

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

    // update booking for accommodation
    @Override
    @Transactional
    public AccommodationBookingResponseDto updateBooking(Long id, AccommodationBookingRequestDto bookingRequestDto,
            UserEntity currentUser) {
        try {
            AccommodationBookingEntity existingBooking = accommodationBookingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Accommodation booking not found with id: " + id));

            ProviderEntity provider = providerRepository.findById(bookingRequestDto.getProviderId())
                    .orElseThrow(() -> new RuntimeException(
                            "Provider not found with id: " + bookingRequestDto.getProviderId()));

            existingBooking.setProvider(provider);

            // Update the booking details based on the request DTO
            existingBooking.setStatus(BookingStatus.PENDING);

            // Clear existing items and add new ones
            existingBooking.getItems().clear();
            Double[] totalAmount = { 0.0 };
            Double[] totalCount = { 0.0 };

            bookingRequestDto.getItems().forEach(itemDto -> {
                AccommodationReservationEntity reservationEntity = new AccommodationReservationEntity();
                AccommodationServiceEntity service = accommodationServiceRepository.findById(itemDto.getServiceId())
                        .orElseThrow(() -> new RuntimeException(
                                "Accommodation service not found with id: " + itemDto.getServiceId()));

                // calculate total amount
                totalAmount[0] += itemDto.getCount().doubleValue() * service.getPricePerNight().doubleValue();

                // calculate total count
                totalCount[0] += itemDto.getCount().doubleValue() * service.getCapacity().doubleValue();

                reservationEntity.setAccommodationService(service);
                reservationEntity.setCount(itemDto.getCount());
                reservationEntity.setBooking(existingBooking);

                existingBooking.getItems().add(reservationEntity);
            });

            ReservationRequest reservationRequest = existingBooking.getReservationRequest();

            if (reservationRequest.getParticipants().size() != totalCount[0]) {
                throw new RuntimeException(
                        "Total count of accommodation services doesn't match the number of participants in the reservation request");
            }

            existingBooking.setTotalAmount(totalAmount[0]);
            existingBooking.setCheckInDate(existingBooking.getReservationRequest().getDepartureDate().toLocalDate());
            existingBooking.setCheckOutDate(existingBooking.getReservationRequest().getReturnDate().toLocalDate());

            AccommodationBookingEntity updatedBooking = accommodationBookingRepository.save(existingBooking);

            // map to Dto
            AccommodationBookingResponseDto responseDto = bookingMapper.toResponseDto(updatedBooking);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // get booking by id
    @Override
    @Transactional
    public AccommodationBookingDetailsResponseDto getBookingById(Long id, UserEntity currentUser) {
        try {
            AccommodationBookingEntity bookingEntity = accommodationBookingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Accommodation booking not found with id: " + id));

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
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}