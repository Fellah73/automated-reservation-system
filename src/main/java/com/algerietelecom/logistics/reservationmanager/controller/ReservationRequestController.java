package com.algerietelecom.logistics.reservationmanager.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algerietelecom.logistics.reservationmanager.domain.dtos.ApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.AdminCommentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.AdminModificationRequestDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ApproverReservationRequestOverviewApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentBatchDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationAttachmentDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationHistoryResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestCreateDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestOverviewApiResponse;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestOverviewDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestUpdateDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationRequestsStatsDTO;
import com.algerietelecom.logistics.reservationmanager.domain.dtos.reservationRequest.ReservationWithHistoryResponseDTO;
import com.algerietelecom.logistics.reservationmanager.domain.entities.auth.UserEntity;
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
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> createReservation(
            @RequestBody ReservationRequestCreateDTO createDTO,
            @AuthenticationPrincipal UserEntity currentUser) {

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
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApiResponse<List<ReservationAttachmentDTO>>> addAttachment(
            @PathVariable String id,
            @RequestBody ReservationAttachmentBatchDTO attachmentBatchDTO,
            @AuthenticationPrincipal UserEntity currentUser) {

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

    // update reservation request
    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> updateReservation(
            @PathVariable String id,
            @RequestBody ReservationRequestUpdateDTO updateDTO,
            @AuthenticationPrincipal UserEntity currentUser) {

        try {
            // update reservation request
            ReservationRequestResponseDTO updatedRequest = reservationService.updateRequest(id, updateDTO, currentUser);

            return ResponseEntity.ok(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(true)
                    .message("Reservation request updated successfully")
                    .statusCode(200)
                    .data(updatedRequest)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(false)
                    .message("Failed to update reservation request: " + e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // update reservation request attachments
    @PutMapping(path = "/{id}/attachments")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApiResponse<List<ReservationAttachmentDTO>>> syncAttachments(
            @PathVariable String id,
            @RequestBody ReservationAttachmentBatchDTO attachmentBatchDTO,
            @AuthenticationPrincipal UserEntity currentUser) {

        try {
            List<ReservationAttachmentDTO> savedRequestAttachments = reservationService.updateAttachment(id,
                    attachmentBatchDTO,
                    currentUser);

            return ResponseEntity.ok(ApiResponse.<List<ReservationAttachmentDTO>>builder()
                    .success(true)
                    .message("Attachments updated successfully")
                    .statusCode(200)
                    .data(savedRequestAttachments)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<List<ReservationAttachmentDTO>>builder()
                    .success(false)
                    .message("Failed to update attachments: " + e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // get all user reservations
    @GetMapping(path = "/my")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApiResponse<ReservationRequestOverviewApiResponse>> getMyReservations(
            @AuthenticationPrincipal UserEntity currentUser) {

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

    // get all reservation requests with pagination
    @GetMapping(path = "/all")
    @PreAuthorize("hasRole('APPROVER')")
    public ResponseEntity<ApiResponse<ApproverReservationRequestOverviewApiResponse>> getAllReservations(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        try {
            Page<ReservationRequestOverviewDTO> reservationRequests = reservationService.getAllRequests(pageable);

            // get their stats
            ReservationRequestsStatsDTO stats = reservationService.getRequestStats();

            ApproverReservationRequestOverviewApiResponse response = new ApproverReservationRequestOverviewApiResponse();
            response.setReservationRequests(reservationRequests);
            response.setStats(stats);

            return ResponseEntity.ok(ApiResponse.<ApproverReservationRequestOverviewApiResponse>builder()
                    .success(true)
                    .message("All reservation requests retrieved successfully")
                    .statusCode(200)
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<ApproverReservationRequestOverviewApiResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // approve reservation request
    @PatchMapping(path = "/{id}/approve")
    @PreAuthorize("hasRole('APPROVER')")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> approveRequest(@PathVariable String id,
            @AuthenticationPrincipal UserEntity currentUser) {
        try {
            if (id == null || id.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.<ReservationRequestResponseDTO>builder()
                        .success(false)
                        .message("Invalid reservation request ID")
                        .statusCode(400)
                        .build());
            }

            // approve reservation request
            ReservationRequestResponseDTO approvedRequest = reservationService.approveRequest(id,
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
    @PreAuthorize("hasRole('APPROVER')")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> rejectRequest(@PathVariable String id,
            @RequestBody AdminCommentDTO adminCommentDTO,
            @AuthenticationPrincipal UserEntity currentUser) {
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
    @PreAuthorize("hasRole('APPROVER')")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> requestChanges(
            @PathVariable String id,
            @RequestBody AdminModificationRequestDTO modificationRequest,
            @AuthenticationPrincipal UserEntity currentUser) {

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

    // request update on reservation request attachments
    @PatchMapping(path = "/{id}/request-changes/attachments")
    @PreAuthorize("hasRole('APPROVER')")
    public ResponseEntity<ApiResponse<ReservationRequestResponseDTO>> requestAttachmentChanges(
            @PathVariable String id,
            @RequestBody AdminModificationRequestDTO modificationRequest,
            @AuthenticationPrincipal UserEntity currentUser) {

        try {
            ReservationRequestResponseDTO updatedRequest = reservationService.requestAttachmentChanges(id,
                    modificationRequest,
                    currentUser);

            return ResponseEntity.ok(ApiResponse.<ReservationRequestResponseDTO>builder()
                    .success(true)
                    .message("Requested changes on reservation request attachments successfully")
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
    @GetMapping(path = "/{id}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ReservationHistoryResponseDTO>>> getRequestHistory(@PathVariable String id,
        @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        try {
            Page<ReservationHistoryResponseDTO> history = reservationService.getReservationRequestHistory(id, pageable);

            return ResponseEntity.ok(ApiResponse.<Page<ReservationHistoryResponseDTO>>builder()
                    .success(true)
                    .statusCode(200)
                    .message("Reservation request history retrieved successfully")
                    .data(history)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<Page<ReservationHistoryResponseDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    // get all reservation requests history
    @GetMapping("/audit-log")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ReservationWithHistoryResponseDTO>>> getAuditLog(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        try {

            Page<ReservationWithHistoryResponseDTO> auditLog = reservationService.getGlobalAuditLog(pageable);

            return ResponseEntity.ok(ApiResponse.<Page<ReservationWithHistoryResponseDTO>>builder()
                    .success(true)
                    .data(auditLog)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<Page<ReservationWithHistoryResponseDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

}
