package com.algerietelecom.logistics.reservationmanager.controller;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.ApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestCreateDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.mappers.ReservationRequestMapper;
import com.algerietelecom.logistics.reservationmanager.service.ReservationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationRequestController {

    private final ReservationRequestService reservationService;
    private final ReservationRequestMapper reservationMapper;

    // request reservation creation
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse<ReservationRequest>> createReservation(
            @RequestBody ReservationRequestCreateDTO createDTO,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {
            ReservationRequest requestEntity = reservationMapper.toEntity(createDTO);

            ReservationRequest created = reservationService.createRequest(requestEntity, currentUser);

            return ResponseEntity.status(201).body(ApiResponse.<ReservationRequest>builder()
                    .success(true)
                    .message("reservation request created successfully")
                    .statusCode(201)
                    .data(created)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<ReservationRequest>builder()
                    .success(false)
                    .message("Failed to create reservation request")
                    .statusCode(500)
                    .build());
        }
    }

    // add attachment to reservation request // later
    @PostMapping(path = "/{id}/attachments")
    public ResponseEntity<ReservationAttachmentDTO> addAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", defaultValue = "OTHER") String category,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        ReservationAttachmentDTO savedAttachment = reservationService.saveAttachment(id, file, category, currentUser);
        return new ResponseEntity<>(savedAttachment, HttpStatus.CREATED);
    }

    // get all my reservations (for user) // later
    @GetMapping(path = "/my")
    public ResponseEntity<List<ReservationRequest>> getMyReservations(
            @AuthenticationPrincipal UserEntitiy currentUser) {

        List<ReservationRequest> requests = reservationService.getMyRequests(currentUser);
        return ResponseEntity.ok(requests);
    }

    // get reservation by id // later
    @GetMapping(path = "/{id}")
    public ResponseEntity<ReservationRequest> getReservationById(@PathVariable Long id) {
        ReservationRequest request = reservationService.getRequestById(id);
        return ResponseEntity.ok(request);
    }
}
