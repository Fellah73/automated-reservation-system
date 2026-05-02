package com.algerietelecom.logistics.reservationmanager.config;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationParticipant;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequestPreference;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.Auth.UserRole;
import com.algerietelecom.logistics.reservationmanager.domain.enumeration.ReservationRequest.ReservationStatus;
import com.algerietelecom.logistics.reservationmanager.repositories.auth.AuthRepository;
import com.algerietelecom.logistics.reservationmanager.service.ReservationRequestService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

        private final AuthRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final ReservationRequestService reservationRequestService;

        @Override
        @Transactional
        public void run(String... args) throws Exception {

                UserEntity admin = userRepository.findByUsername("admin_at")
                                .orElseGet(() -> {
                                        UserEntity newAdmin = new UserEntity();
                                        newAdmin.setUsername("admin_at");
                                        newAdmin.setPassword(passwordEncoder.encode("admin123"));
                                        newAdmin.setEmail("admin@algerietelecom.dz");
                                        newAdmin.setRole(UserRole.ADMIN);
                                        return userRepository.save(newAdmin);
                                });

                UserEntity approver = userRepository.findByUsername("approver_at")
                                .orElseGet(() -> {
                                        UserEntity newApprover = new UserEntity();
                                        newApprover.setUsername("approver_at");
                                        newApprover.setPassword(passwordEncoder.encode("admin123"));
                                        newApprover.setEmail("approver@algerietelecom.dz");
                                        newApprover.setRole(UserRole.APPROVER);
                                        return userRepository.save(newApprover);
                                });

                UserEntity agent = userRepository.findByUsername("agent_at")
                                .orElseGet(() -> {
                                        UserEntity newAgent = new UserEntity();
                                        newAgent.setUsername("agent_at");
                                        newAgent.setPassword(passwordEncoder.encode("admin123"));
                                        newAgent.setEmail("agent@algerietelecom.dz");
                                        newAgent.setRole(UserRole.AGENT);
                                        return userRepository.save(newAgent);
                                });
                if (reservationRequestService.getMyRequests(agent).isEmpty()) {

                        ReservationRequest request = ReservationRequest.builder()
                                        .requestNumber("REQ-2026-001")
                                        .missionType("FORMATION_TECHNIQUE")
                                        .status(ReservationStatus.PENDING)
                                        .createdBy(agent)
                                        .destination("Oran - Direction Régionale")
                                        .departureDate(LocalDateTime.now().plusDays(7))
                                        .returnDate(LocalDateTime.now().plusDays(10))
                                        .reason("Formation sur les nouveaux équipements fibre optique")
                                        .build();

                        ReservationParticipant p1 = ReservationParticipant.builder()
                                        .firstName("Mohamed").lastName("Fellah").employeeId("AT_12345")
                                        .role("Ingénieur SE").department("IT")
                                        .reservationRequest(request)
                                        .build();

                        ReservationParticipant p2 = ReservationParticipant.builder()
                                        .firstName("Amine").lastName("Telecom").employeeId("AT_67890")
                                        .role("Technicien").department("Réseaux")
                                        .reservationRequest(request)
                                        .build();

                        request.setParticipants(Arrays.asList(p1, p2));

                        ReservationRequestPreference prefs = ReservationRequestPreference.builder()
                                        .flightPreference("Matin - Air Algérie")
                                        .hotelPreference("Hôtel 4 étoiles proche du site")
                                        .mealPreference("Standard")
                                        .transportType("AVION")
                                        .reservationRequest(request)
                                        .build();

                        request.setPreferences(prefs);
                        ReservationRequest saved = reservationRequestService.createRequest(request, agent);

                        System.out.println("Réservation de test créée avec succès pour : " + agent.getUsername());
                        System.out.println(" Destination : " + saved.getDestination());
                }
        }
}