package com.algerietelecom.logistics.reservationmanager.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.contracts.ProviderContractResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.evaluation.ProviderEvaluationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.evaluation.ProviderEvaluationResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.CreateProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main.ProviderDto;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.services.ContractServicesResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntitiy;
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
    public List<ProviderDto> getAllProviders(ProviderType type) {
        try {
            List<ProviderEntity> providers = providerRepository.findByType(type);

            // the type filter returns no providers
            if (providers.isEmpty()) {
                providers = providerRepository.findAll();
            }

            // convert entities to DTOs
            List<ProviderDto> providerDtos = providers.stream()
                    .map(providerMapper::toDtoWithId)
                    .toList();

            return providerDtos;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve providers: " + e.getMessage());
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
                        createRoom(contract, "SIMPLE", 7500),
                        createRoom(contract, "DOUBLE", 9500),
                        createRoom(contract, "TRIPLE", 12000),
                        createRoom(contract, "JUNIOR", 15000),
                        createRoom(contract, "SENIOR", 22000));
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
    private AccommodationServiceEntity createRoom(ProviderContractEntity contract, String type, double price) {
        return AccommodationServiceEntity.builder()
                .roomType(type)
                .pricePerNight(BigDecimal.valueOf(price))
                .breakfastIncluded(true)
                .amenities("Standard AT Configuration")
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
    public List<ProviderContractResponseDTO> getAllContractsForProvider(Long providerId) {
        try {
            // optional check for provider
            providerRepository.findById(providerId)
                    .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));

            List<ProviderContractEntity> contracts = providerContractRepository
                    .findByProviderIdOrderByStartDateDesc(providerId);

            List<ProviderContractResponseDTO> contractDtos = contracts.stream()
                    .map(providerMapper::toContractDto)
                    .toList();
            return contractDtos;
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
            ProviderEvaluationRequestDTO evaluationDto, UserEntitiy currentUser) {
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
