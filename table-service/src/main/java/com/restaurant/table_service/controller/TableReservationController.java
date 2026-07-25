package com.restaurant.table_service.controller;

import com.restaurant.table_service.dto.ApiResponse;
import com.restaurant.table_service.dto.ReservationDetailProjection;
import com.restaurant.table_service.dto.ReservationProjection;
import com.restaurant.table_service.dto.table_reservation.request.ReservationRequest;
import com.restaurant.table_service.dto.table_reservation.response.ReservationResponse;
import com.restaurant.table_service.entity.table.TableReservation;
import com.restaurant.table_service.entity.table.enums.ReservationStatus;
import com.restaurant.table_service.request.ReservationFilterRequest;
import com.restaurant.table_service.security.SecurityCheckApisClass;
import com.restaurant.table_service.service.impl.ITableReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class TableReservationController {

    private final ITableReservationService reservationService;
    private final SecurityCheckApisClass securityCheckApisClass;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody ReservationRequest request
    ){
        ApiResponse<ReservationResponse> response = reservationService.createReservation(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> updateReservation(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long reservationId,
            @RequestBody ReservationRequest request
    ){
        ApiResponse<ReservationResponse> response = reservationService.updateReservation(reservationId, request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    public ResponseEntity<ApiResponse<ReservationResponse>> updateReservationStatus(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long reservationId,
            @RequestParam ReservationStatus status,
            @RequestParam Long uid
    ){
        ReservationRequest request = new ReservationRequest();
        request.setStatus(status);
        ApiResponse<ReservationResponse> response = reservationService.updateReservationStatus(reservationId, request, uid);
        return new ResponseEntity<>(response, response.getStatus());
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReservationProjection>>> getAllReservations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        log.info("GET /api/v1/reservations - restaurantId: {}, page: {}, size: {}", restaurantId, page, size);
        Pageable pageable = PageRequest.of(page, size);

        ApiResponse<Page<ReservationProjection>> reservations = reservationService.getAllReservations(restaurantId, pageable);
        return new ResponseEntity<>(reservations, reservations.getStatus());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<ReservationProjection>>> getReservationsByStatus(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @PathVariable ReservationStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/reservations/status/{} - restaurantId: {}", status, restaurantId);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<Page<ReservationProjection>> reservations = reservationService.getReservationsByStatus(restaurantId, status, pageable);
        return new ResponseEntity<>(reservations, reservations.getStatus());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<Page<ReservationProjection>>> getCustomerReservations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/reservations/customer/{}", customerId);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<Page<ReservationProjection>> reservations = reservationService.getCustomerReservations(customerId, pageable);
        return new ResponseEntity<>(reservations, reservations.getStatus());
    }

    @GetMapping("/customer/{customerId}/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse<Page<ReservationProjection>>> getCustomerReservationsByRestaurant(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long customerId,
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/reservations/customer/{}/restaurant/{}", customerId, restaurantId);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<Page<ReservationProjection>> reservations = reservationService.getCustomerReservationsByRestaurant(customerId, restaurantId, pageable);
        return new ResponseEntity<>(reservations, reservations.getStatus());
    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<Page<ReservationProjection>>> filterReservations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody ReservationFilterRequest request) {
        log.info("POST /api/v1/reservations/filter - {}", request);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        ApiResponse<Page<ReservationProjection>> reservations = reservationService.filterReservations(request);
        return new ResponseEntity<>(reservations, reservations.getStatus());
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationDetailProjection>> getReservationById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long reservationId) {

        log.info("GET /api/v1/reservations/{}", reservationId);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        ApiResponse<ReservationDetailProjection> reservation = reservationService.getReservationById(reservationId);
        return new ResponseEntity<>(reservation, reservation.getStatus());
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<ReservationProjection>>> getPendingReservations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        log.info("GET /api/v1/reservations/pending - restaurantId: {}, fromDate: {}, toDate: {}", restaurantId, fromDate, toDate);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        ApiResponse<List<ReservationProjection>> reservations = reservationService.getPendingReservations(restaurantId, fromDate, toDate);
        return new ResponseEntity<>(reservations, reservations.getStatus());
    }

    @GetMapping("/confirmed")
    public ResponseEntity<ApiResponse<List<ReservationProjection>>> getConfirmedReservations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        log.info("GET /api/v1/reservations/confirmed - restaurantId: {}, fromDate: {}, toDate: {}", restaurantId, fromDate, toDate);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        ApiResponse<List<ReservationProjection>> reservations = reservationService.getConfirmedReservations(restaurantId, fromDate, toDate);
        return new ResponseEntity<>(reservations, reservations.getStatus());
    }

    @GetMapping("/no-show")
    public ResponseEntity<ApiResponse<List<ReservationProjection>>> getNoShowReservations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        log.info("GET /api/v1/reservations/no-show - restaurantId: {}, fromDate: {}, toDate: {}", restaurantId, fromDate, toDate);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        ApiResponse<List<ReservationProjection>> reservations = reservationService.getNoShowReservations(restaurantId, fromDate, toDate);
        return new ResponseEntity<>(reservations, reservations.getStatus());
    }

    @GetMapping("/count-by-status")
    public ResponseEntity<ApiResponse<Long>> getReservationCountByStatus(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam ReservationStatus status) {
        log.info("GET /api/v1/reservations/count-by-status - restaurantId: {}, status: {}", restaurantId, status);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            securityCheckApisClass.checkApi(authorizationHeader);
        }
        ApiResponse<Long> count = reservationService.getReservationCountByStatus(restaurantId, status);
        return new ResponseEntity<>(count, count.getStatus());
    }
}
