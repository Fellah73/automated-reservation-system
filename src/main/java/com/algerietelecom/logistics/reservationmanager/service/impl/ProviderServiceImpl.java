package com.algerietelecom.logistics.reservationmanager.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts.ProviderContractResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.evaluation.ProviderEvaluationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.evaluation.ProviderEvaluationResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.ProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.ProviderStatsDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.services.ContractServicesResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderContractEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.ProviderEvaluationEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices.AccommodationServiceEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices.CateringServiceEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.provider.providerServices.FlightServiceEntity;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.Provider.ProviderType;
import com.algerietelecom.logistics.reservationmanager.mappers.ProviderMapper;
import com.algerietelecom.logistics.reservationmanager.repositories.provider.ProviderContractRepository;
import com.algerietelecom.logistics.reservationmanager.repositories.provider.ProviderEvaluationRepository;
import com.algerietelecom.logistics.reservationmanager.repositories.provider.ProviderRepository;
import com.algerietelecom.logistics.reservationmanager.service.ProviderService;

import jakarta.transaction.Transactional;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final Random random = new Random();
    private final ProviderRepository providerRepository;
    private final ProviderContractRepository providerContractRepository;
    private final ProviderEvaluationRepository providerEvaluationRepository;
    private final ProviderMapper providerMapper;

    public ProviderServiceImpl(ProviderRepository providerRepository,
            ProviderContractRepository providerContractRepository,
            ProviderEvaluationRepository providerEvaluationRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.providerContractRepository = providerContractRepository;
        this.providerEvaluationRepository = providerEvaluationRepository;
        this.providerMapper = providerMapper;
    }

    // create provider method
    @Override
    @Transactional
    public CreateProviderDto createProvider(ProviderEntity providerToCreate) {
        try {
            // Vérification du NIF unique
            if (providerToCreate.getNif() != null && providerRepository.existsByNif(providerToCreate.getNif())) {
                throw new RuntimeException("Un prestataire avec ce NIF existe déjà.");
            }

            ProviderEntity provider = ProviderEntity.builder()
                    .name(providerToCreate.getName())
                    .type(providerToCreate.getType())
                    .nif(providerToCreate.getNif())
                    .email(providerToCreate.getEmail())
                    .phoneNumber(providerToCreate.getPhoneNumber())
                    .city(providerToCreate.getCity())
                    .address(providerToCreate.getAddress())
                    .contactPerson(providerToCreate.getContactPerson())
                    .logoUrl(providerToCreate.getLogoUrl())
                    .active(false)
                    .build();

            ProviderEntity savedProvider = providerRepository.save(provider);

            // convert entity to DTO
            CreateProviderDto responseDto = providerMapper.toDto(savedProvider);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create provider: " + e.getMessage());
        }
    }

    // get provider by id method
    @Override
    @Transactional
    public ProviderDto getProviderById(Long id) {
        try {
            ProviderEntity provider = providerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + id));

            // convert entity to DTO
            ProviderDto responseDto = providerMapper.toDtoWithId(provider);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve provider: " + e.getMessage());
        }
    }

    // update provider method
    @Override
    @Transactional
    public CreateProviderDto updateProvider(Long id, ProviderEntity providerToUpdate) {
        try {

            ProviderEntity existingProvider = providerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + id));

            ProviderEntity provider = ProviderEntity.builder()
                    .id(existingProvider.getId())
                    .name(providerToUpdate.getName() != null ? providerToUpdate.getName() : existingProvider.getName())
                    .type(providerToUpdate.getType() != null ? providerToUpdate.getType() : existingProvider.getType())
                    .nif(providerToUpdate.getNif() != null ? providerToUpdate.getNif() : existingProvider.getNif())
                    .email(providerToUpdate.getEmail() != null ? providerToUpdate.getEmail()
                            : existingProvider.getEmail())
                    .phoneNumber(providerToUpdate.getPhoneNumber() != null ? providerToUpdate.getPhoneNumber()
                            : existingProvider.getPhoneNumber())
                    .address(providerToUpdate.getAddress() != null ? providerToUpdate.getAddress()
                            : existingProvider.getAddress())
                    .contactPerson(providerToUpdate.getContactPerson() != null ? providerToUpdate.getContactPerson()
                            : existingProvider.getContactPerson())
                    .logoUrl(providerToUpdate.getLogoUrl() != null ? providerToUpdate.getLogoUrl()
                            : existingProvider.getLogoUrl())
                    .active(existingProvider.getActive())
                    .build();

            ProviderEntity savedProvider = providerRepository.save(provider);

            // convert entity to DTO
            CreateProviderDto responseDto = providerMapper.toDto(savedProvider);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create provider: " + e.getMessage());
        }
    }

    // soft delete provider method
    @Override
    @Transactional
    public void softDeleteProvider(Long id) {
        try {
            ProviderEntity existingProvider = providerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + id));

            // update active status to false
            existingProvider.setActive(false);
            providerRepository.save(existingProvider);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete provider: " + e.getMessage());
        }
    }

    // restore provider method
    @Override
    @Transactional
    public CreateProviderDto restoreProvider(Long id) {
        try {
            ProviderEntity existingProvider = providerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + id));

            // update active status to true
            existingProvider.setActive(true);
            ProviderEntity savedProvider = providerRepository.save(existingProvider);

            // convert entity to DTO
            CreateProviderDto responseDto = providerMapper.toDto(savedProvider);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore provider: " + e.getMessage());
        }
    }

    // get all providers method
    @Override
    @Transactional
    public Page<ProviderDto> getAllProviders(ProviderType type, Pageable pageable) {
        try {
            Page<ProviderEntity> providers = providerRepository.findByType(type, pageable);

            // the type filter returns no providers
            if (providers.isEmpty()) {
                providers = providerRepository.findAll(pageable);
            }

            // convert entities to DTOs
            List<ProviderDto> providerDtos = providers.stream()
                    .map(providerMapper::toDtoWithId)
                    .toList();

            // set hasEvaluation flag
            providerDtos
                    .forEach(dto -> dto.setHasEvaluation(providerEvaluationRepository.existsByProviderId(dto.getId())));

            return new PageImpl<>(providerDtos, pageable, providers.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve providers: " + e.getMessage());
        }

    }

    // get provider stats method
    @Override
    @Transactional
    public ProviderStatsDTO getRequestStats() {
        try {
            long totalProviders = providerRepository.count();
            long totalHotels = providerRepository.countByType(ProviderType.HOTEL);
            long totalAirlines = providerRepository.countByType(ProviderType.AIRLINE);
            long totalCatering = providerRepository.countByType(ProviderType.CATERING);

            ProviderStatsDTO stats = new ProviderStatsDTO();
            stats.setTotalProviders(totalProviders);
            stats.setTotalHotels(totalHotels);
            stats.setTotalAirlines(totalAirlines);
            stats.setTotalCatering(totalCatering);

            return stats;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve provider stats: " + e.getMessage());
        }
    }

    // contracts methods
    // generate contract number method
    private String generateContractNumber(Long providerId) {
        long count = providerContractRepository.countByProviderId(providerId);
        return "CTR-" + providerId + "-" + (count + 1);
    }

    // initialize default services
    private void initializeDefaultServices(ProviderContractEntity contract) {
        ProviderType type = contract.getProvider().getType();

        switch (type) {
            case HOTEL -> {
                List<AccommodationServiceEntity> defaultRooms = List.of(
                        createRoom(contract, "SIMPLE", 7000, 9000, 1),
                        createRoom(contract, "DOUBLE", 9000, 11000, 2),
                        createRoom(contract, "TRIPLE", 11000, 13500, 3),
                        createRoom(contract, "JUNIOR", 13500, 16500, 4),
                        createRoom(contract, "SENIOR", 20000, 24000, 5));
                contract.setAccommodationServices(defaultRooms);
            }
            case CATERING -> {
                List<CateringServiceEntity> defaultMenus = List.of(
                        createMenu(contract, "Menu Standard", "DEJEUNER", 1500, true),
                        createMenu(contract, "Menu Business", "DEJEUNER", 2500, true),
                        createMenu(contract, "Menu Dîner", "DINER", 2000, false));
                contract.setCateringServices(defaultMenus);
            }
            case AIRLINE -> {
                List<FlightServiceEntity> defaultFlights = List.of(
                        createFlight(contract, "Alger - Sétif", "ECONOMIQUE", 4500, 23),
                        createFlight(contract, "Alger - Sétif", "AFFAIRE", 8500, 40),
                        createFlight(contract, "Alger - Oran", "ECONOMIQUE", 6000, 23));
                contract.setFlightServices(defaultFlights);
            }
        }
    }

    // create room method
    private AccommodationServiceEntity createRoom(ProviderContractEntity contract, String type, double minPrice,
            double maxPrice, Integer capacity) {

        double randomPrice = Math.round((minPrice + (maxPrice - minPrice) * random.nextDouble()) / 100) * 100;
        return AccommodationServiceEntity.builder()
                .roomType(type)
                .pricePerNight(BigDecimal.valueOf(randomPrice))
                .breakfastIncluded(true)
                .amenities("Standard AT Configuration")
                .capacity(capacity)
                .contract(contract)
                .build();
    }

    // create menu method
    private CateringServiceEntity createMenu(ProviderContractEntity contract, String name, String mealType,
            double price, boolean drinks) {
        return CateringServiceEntity.builder()
                .menuName(name)
                .mealType(mealType)
                .pricePerPerson(BigDecimal.valueOf(price))
                .drinksIncluded(drinks)
                .contract(contract)
                .build();
    }

    // create flight method
    private FlightServiceEntity createFlight(ProviderContractEntity contract, String route, String cabin, double price,
            int baggage) {
        return FlightServiceEntity.builder()
                .route(route)
                .cabinClass(cabin)
                .price(BigDecimal.valueOf(price))
                .baggageAllowance(baggage)
                .isDirect(true)
                .contract(contract)
                .build();
    }

    // create contract method
    @Override
    @Transactional
    public ProviderContractResponseDTO createContract(Long providerId, ProviderContractEntity contract) {
        try {
            ProviderEntity provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));

            contract.setProvider(provider);

            // generate unique contract number
            contract.setContractNumber(generateContractNumber(providerId));

            // initialize default services for this contract
            initializeDefaultServices(contract);

            contract.setActive(contract.isValid());

            ProviderContractEntity savedContract = providerContractRepository.save(contract);

            // convert entity to DTO
            ProviderContractResponseDTO createdContractDTO = providerMapper.ContractToDto(savedContract);
            return createdContractDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create contract: " + e.getMessage());
        }
    }

    // update contract method
    @Override
    @Transactional
    public ProviderContractResponseDTO updateContract(Long providerId, Long contractId,
            ProviderContractEntity contract) {
        try {
            // optional check for provider
            providerRepository.findById(providerId)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));

            ProviderContractEntity existingContract = providerContractRepository.findById(contractId)
                    .orElseThrow(() -> new RuntimeException("Contract not found with id: " + contractId));

            // update if optional fields
            existingContract.setStartDate(
                    contract.getStartDate() != null ? contract.getStartDate() : existingContract.getStartDate());
            existingContract
                    .setEndDate(contract.getEndDate() != null ? contract.getEndDate() : existingContract.getEndDate());
            existingContract.setContractUrl(
                    contract.getContractUrl() != null ? contract.getContractUrl() : existingContract.getContractUrl());
            existingContract.setFileName(
                    contract.getFileName() != null ? contract.getFileName() : existingContract.getFileName());
            existingContract
                    .setTermsAndConditions(contract.getTermsAndConditions() != null ? contract.getTermsAndConditions()
                            : existingContract.getTermsAndConditions());

            existingContract.setContractNumber(existingContract.getContractNumber());

            ProviderContractEntity savedContract = providerContractRepository.save(existingContract);

            // convert entity to DTO
            ProviderContractResponseDTO createdContractDTO = providerMapper.ContractToDto(savedContract);
            return createdContractDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create contract: " + e.getMessage());
        }
    }

    // soft delete contract method
    @Override
    @Transactional
    public void softDeleteContract(Long providerId, Long contractId) {
        try {
            // optional check for provider
            providerRepository.findById(providerId)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));

            ProviderContractEntity existingContract = providerContractRepository.findById(contractId)
                    .orElseThrow(() -> new RuntimeException("Contract not found with id: " + contractId));

            // update active status to false
            existingContract.setActive(false);

            providerContractRepository.save(existingContract);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // retrieve all contracts for provider method
    @Override
    public Page<ProviderContractResponseDTO> getAllContractsForProvider(Long providerId, Pageable pageable) {
        try {
            providerRepository.findById(providerId)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));

            Page<ProviderContractEntity> contracts = providerContractRepository
                    .findByProviderIdOrderByStartDateDesc(providerId, pageable);

            List<ProviderContractResponseDTO> contractDtos = contracts.stream()
                    .map(providerMapper::toContractDto)
                    .toList();

            return new PageImpl<>(contractDtos, pageable, contracts.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve contracts: " + e.getMessage());
        }
    }

    // get all services for a contract method
    @Override
    @Transactional
    public ContractServicesResponseDTO getContractServices(Long contractId) {
        try {
            ProviderContractEntity contract = providerContractRepository.findById(contractId)
                    .orElseThrow(() -> new RuntimeException("Contract not found with id: " +
                            contractId));

            ContractServicesResponseDTO servicesDto = providerMapper.toContractServicesDto(contract);
            return servicesDto;
        } catch (Exception e) {
            throw new RuntimeException(
                    e.getMessage());
        }
    }

    // providers evaluation methods
    @Override
    @Transactional
    public ProviderEvaluationRequestDTO createEvaluationForProvider(Long providerId,
            ProviderEvaluationRequestDTO evaluationDto, UserEntity currentUser) {
        try {
            ProviderEntity provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));

            ProviderEvaluationEntity evaluation = ProviderEvaluationEntity.builder()
                    .delayRating(evaluationDto.getDelayRating())
                    .serviceQualityRating(evaluationDto.getServiceQualityRating())
                    .conformityRating(evaluationDto.getConformityRating())
                    .costRating(evaluationDto.getCostRating())
                    .overallRating((evaluationDto.getDelayRating() + evaluationDto.getServiceQualityRating()
                            + evaluationDto.getConformityRating() + evaluationDto.getCostRating()) / 4)
                    .comment(evaluationDto.getComment())
                    .provider(provider)
                    .evaluatedBy(currentUser)
                    .build();

            providerEvaluationRepository.save(evaluation);

            return evaluationDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve evaluations: " + e.getMessage());
        }
    }

    // retrieve evaluations for a provider method
    @Override
    @Transactional
    public ProviderEvaluationResponseDTO getEvaluationsByProvider(Long providerId) {
        try {
            ProviderEntity provider = providerRepository.findById(providerId)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));

            ProviderEvaluationEntity evaluations = providerEvaluationRepository
                    .findByProviderId(provider.getId());

            if (evaluations == null) {
                throw new RuntimeException("No evaluations found for this provider");
            }

            ProviderEvaluationResponseDTO responseDto = ProviderEvaluationResponseDTO.builder()
                    .delayRating(evaluations.getDelayRating())
                    .serviceQualityRating(evaluations.getServiceQualityRating())
                    .conformityRating(evaluations.getConformityRating())
                    .costRating(evaluations.getCostRating())
                    .overallRating(evaluations.getOverallRating())
                    .comment(evaluations.getComment())
                    .build();

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
