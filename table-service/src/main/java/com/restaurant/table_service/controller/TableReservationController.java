package com.restaurant.table_service.controller;

import com.restaurant.table_service.dto.ReservationDetailProjection;
import com.restaurant.table_service.dto.ReservationProjection;
import com.restaurant.table_service.entity.table.enums.ReservationStatus;
import com.restaurant.table_service.request.ReservationFilterRequest;
import com.restaurant.table_service.service.TableReservationService;
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

    private final TableReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<ReservationProjection>> getAllReservations(
            @RequestParam Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/reservations - restaurantId: {}, page: {}, size: {}", restaurantId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationProjection> reservations = reservationService.getAllReservations(restaurantId, pageable);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ReservationProjection>> getReservationsByStatus(
            @RequestParam Long restaurantId,
            @PathVariable ReservationStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/reservations/status/{} - restaurantId: {}", status, restaurantId);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationProjection> reservations = reservationService.getReservationsByStatus(restaurantId, status, pageable);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<ReservationProjection>> getCustomerReservations(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/reservations/customer/{}", customerId);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationProjection> reservations = reservationService.getCustomerReservations(customerId, pageable);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/customer/{customerId}/restaurant/{restaurantId}")
    public ResponseEntity<Page<ReservationProjection>> getCustomerReservationsByRestaurant(
            @PathVariable Long customerId,
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/reservations/customer/{}/restaurant/{}", customerId, restaurantId);
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationProjection> reservations = reservationService.getCustomerReservationsByRestaurant(customerId, restaurantId, pageable);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<ReservationProjection>> filterReservations(@RequestBody ReservationFilterRequest request) {
        log.info("POST /api/v1/reservations/filter - {}", request);
        Page<ReservationProjection> reservations = reservationService.filterReservations(request);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDetailProjection> getReservationById(@PathVariable Long reservationId) {
        log.info("GET /api/v1/reservations/{}", reservationId);
        ReservationDetailProjection reservation = reservationService.getReservationById(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ReservationProjection>> getPendingReservations(
            @RequestParam Long restaurantId,
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        log.info("GET /api/v1/reservations/pending - restaurantId: {}, fromDate: {}, toDate: {}", restaurantId, fromDate, toDate);
        List<ReservationProjection> reservations = reservationService.getPendingReservations(restaurantId, fromDate, toDate);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/confirmed")
    public ResponseEntity<List<ReservationProjection>> getConfirmedReservations(
            @RequestParam Long restaurantId,
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        log.info("GET /api/v1/reservations/confirmed - restaurantId: {}, fromDate: {}, toDate: {}", restaurantId, fromDate, toDate);
        List<ReservationProjection> reservations = reservationService.getConfirmedReservations(restaurantId, fromDate, toDate);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/no-show")
    public ResponseEntity<List<ReservationProjection>> getNoShowReservations(
            @RequestParam Long restaurantId,
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {
        log.info("GET /api/v1/reservations/no-show - restaurantId: {}, fromDate: {}, toDate: {}", restaurantId, fromDate, toDate);
        List<ReservationProjection> reservations = reservationService.getNoShowReservations(restaurantId, fromDate, toDate);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/count-by-status")
    public ResponseEntity<Long> getReservationCountByStatus(
            @RequestParam Long restaurantId,
            @RequestParam ReservationStatus status) {
        log.info("GET /api/v1/reservations/count-by-status - restaurantId: {}, status: {}", restaurantId, status);
        Long count = reservationService.getReservationCountByStatus(restaurantId, status);
        return ResponseEntity.ok(count);
    }
}
