package com.algerietelecom.logistics.reservationmanager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.ApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.AdminCommentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.AdminModificationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentBatchDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationHistoryResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestCreateDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestOverviewApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestOverviewDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestsStatsDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.UserEntitiy;
import com.algerietelecom.logistics.reservationmanager.domain.entities.reservationRequest.ReservationRequest;
import com.algerietelecom.logistics.reservationmanager.mappers.ReservationRequestMapper;
import com.algerietelecom.logistics.reservationmanager.service.ReservationRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationRequestController {

    private final ReservationRequestService reservationService;
    private final ReservationRequestMapper reservationMapper;

    // user routes
    // request reservation creation
    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> createReservation(
            @RequestBody ReservationRequestCreateDTO createDTO,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {
            // convert DTO to entity
            ReservationRequest requestEntity = reservationMapper.toEntity(createDTO);

            // create reservation request
            ReservationRequest created = reservationService.createRequest(requestEntity, currentUser);

            // convert created entity to response DTO
            ReservationRequestResponseDTO responseDTO = reservationMapper.toResponseDTO(created, null);

            return ResponseEntity.status(201).body(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(true)
                    .message("reservation request created successfully")
                    .statusCode(201)
                    .data(responseDTO)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(false)
                    .message("Failed to create reservation request")
                    .statusCode(500)
                    .build());
        }
    }

    // add attachment to reservation request
    @PostMapping(path = "/{id}/attachments")
    public ResponseEntity<ApiResponse<List<ReservationAttachmentDTO>>> addAttachment(
            @PathVariable String id,
            @RequestBody ReservationAttachmentBatchDTO attachmentBatchDTO,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {
            List<ReservationAttachmentDTO> savedRequestAttachments = reservationService.saveAttachment(id,
                    attachmentBatchDTO,
                    currentUser);

            return ResponseEntity.ok(ApiResponse.<List<ReservationAttachmentDTO>>builder()
                    .success(true)
                    .message("Attachments added successfully")
                    .statusCode(200)
                    .data(savedRequestAttachments)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<List<ReservationAttachmentDTO>>builder()
                    .success(false)
                    .message("Failed to add attachments: " + e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // get all user reservations
    @GetMapping(path = "/my")
    public ResponseEntity<ApiResponse<ReservationRequestOverviewApiResponse>> getMyReservations(
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {
            // get my reservation requests
            List<ReservationRequestOverviewDTO> reservationRequests = reservationService.getMyRequests(currentUser);

            // get their stats
            ReservationRequestsStatsDTO stats = reservationService.getRequestStats();

            ReservationRequestOverviewApiResponse response = new ReservationRequestOverviewApiResponse();
            response.setReservationRequests(reservationRequests);
            response.setStats(stats);

            return ResponseEntity.ok(ApiResponse.<ReservationRequestOverviewApiResponse>builder()
                    .success(true)
                    .message("My reservations retrieved successfully")
                    .statusCode(200)
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<ReservationRequestOverviewApiResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // get reservation by id
    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> getReservationById(@PathVariable String id) {
        try {
            if (id == null || id.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.<ReservationRequestResponseDTO>builder()
                        .success(false)
                        .message("Invalid reservation request ID")
                        .statusCode(400)
                        .build());
            }

            // get reservation request
            ReservationRequestResponseDTO request = reservationService.getRequestById(id);

            return ResponseEntity.ok(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(true)
                    .message("Reservation request retrieved successfully")
                    .statusCode(200)
                    .data(request)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // admin routes
    // approve reservation request
    @PatchMapping(path = "/{id}/approve")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> approveRequest(@PathVariable String id,
            @RequestBody AdminCommentDTO adminCommentDTO,
            @AuthenticationPrincipal UserEntitiy currentUser) {
        try {
            if (id == null || id.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.<ReservationRequestResponseDTO>builder()
                        .success(false)
                        .message("Invalid reservation request ID")
                        .statusCode(400)
                        .build());
            }

            // approve reservation request
            ReservationRequestResponseDTO approvedRequest = reservationService.approveRequest(id, adminCommentDTO,
                    currentUser);

            return ResponseEntity.ok(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(true)
                    .message("Reservation request approved successfully")
                    .statusCode(200)
                    .data(approvedRequest)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // reject reservation request
    @PatchMapping(path = "/{id}/reject")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> rejectRequest(@PathVariable String id,
            @RequestBody AdminCommentDTO adminCommentDTO,
            @AuthenticationPrincipal UserEntitiy currentUser) {
        try {
            if (id == null || id.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.<ReservationRequestResponseDTO>builder()
                        .success(false)
                        .message("Invalid reservation request ID")
                        .statusCode(400)
                        .build());
            }

            // reject reservation request
            ReservationRequestResponseDTO rejectedRequest = reservationService.rejectRequest(id, adminCommentDTO,
                    currentUser);

            return ResponseEntity.ok(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(true)
                    .message("Reservation request rejected successfully")
                    .statusCode(200)
                    .data(rejectedRequest)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // request update on reservation request
    @PatchMapping(path = "/{id}/request-changes")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> requestChanges(
            @PathVariable String id,
            @RequestBody AdminModificationRequestDTO modificationRequest,
            @AuthenticationPrincipal UserEntitiy currentUser) {

        try {
            ReservationRequestResponseDTO updatedRequest = reservationService.requestChanges(id, modificationRequest,
                    currentUser);

            return ResponseEntity.ok(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(true)
                    .message("Requested changes on reservation request successfully")
                    .data(updatedRequest)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // get reservation request history
    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<ReservationHistoryResponseDTO>>> getRequestHistory(@PathVariable String id) {

        try {
            List<ReservationHistoryResponseDTO> history = reservationService.getReservationRequestHistory(id);

            return ResponseEntity.ok(ApiResponse.<List<ReservationHistoryResponseDTO>>builder()
                    .success(true)
                    .statusCode(200)
                    .message("Reservation request history retrieved successfully")
                    .data(history)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<List<ReservationHistoryResponseDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }
}
