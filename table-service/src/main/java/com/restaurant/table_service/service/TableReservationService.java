package com.restaurant.table_service.service;

import com.restaurant.table_service.dto.ReservationDetailProjection;
import com.restaurant.table_service.dto.ReservationProjection;
import com.restaurant.table_service.entity.table.TableReservation;
import com.restaurant.table_service.entity.table.enums.ReservationStatus;
import com.restaurant.table_service.request.ReservationFilterRequest;
import com.restaurant.table_service.repository.TableReservationRepository;
import com.restaurant.table_service.service.impl.ITableReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TableReservationService implements ITableReservationService {

    private final TableReservationRepository reservationRepository;

    @Override
    public boolean isTableReserved(Long tableId, Instant assignedAt, Instant vacatedAt) {
        boolean isReserved = reservationRepository.findByTableIdAndStatusIn(tableId, Arrays.asList(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CHECKED_IN)).stream()
                .anyMatch(reservation -> {
                    Instant reservationStart = reservation.getReservationDateTime();
                    Instant reservationEnd = reservation.getCheckOutDateTime() != null ? reservation.getCheckOutDateTime() : reservationStart.plusSeconds(3600); // Assuming 1 hour if check-out is not set
                    return (assignedAt.isBefore(reservationEnd) && vacatedAt.isAfter(reservationStart));
                });
        return isReserved;
    }

    public Page<ReservationProjection> getAllReservations(Long restaurantId, Pageable pageable) {
        log.info("Fetching all reservations for restaurant: {}", restaurantId);
        Page<TableReservation> reservations = reservationRepository.findByRestaurantId(restaurantId, pageable);
        return mapToReservationProjection(reservations);
    }

    public Page<ReservationProjection> getReservationsByStatus(Long restaurantId, ReservationStatus status, Pageable pageable) {
        log.info("Fetching reservations for restaurant: {} with status: {}", restaurantId, status);
        Page<TableReservation> reservations = reservationRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);
        return mapToReservationProjection(reservations);
    }

    public Page<ReservationProjection> getCustomerReservations(Long customerId, Pageable pageable) {
        log.info("Fetching reservations for customer: {}", customerId);
        Page<TableReservation> reservations = reservationRepository.findByCustomerId(customerId, pageable);
        return mapToReservationProjection(reservations);
    }

    public Page<ReservationProjection> getCustomerReservationsByRestaurant(Long customerId, Long restaurantId, Pageable pageable) {
        log.info("Fetching reservations for customer: {} at restaurant: {}", customerId, restaurantId);
        Page<TableReservation> reservations = reservationRepository.findByCustomerIdAndRestaurantId(customerId, restaurantId, pageable);
        return mapToReservationProjection(reservations);
    }

    public Page<ReservationProjection> filterReservations(ReservationFilterRequest request) {
        log.info("Filtering reservations with request: {}", request);
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        Page<TableReservation> reservations = reservationRepository.findReservationsByFilters(
                request.getRestaurantId(),
                request.getCustomerId(),
                request.getStatus(),
                request.getReservationDateFrom(),
                request.getReservationDateTo(),
                request.getNoShow(),
                pageable
        );
        return mapToReservationProjection(reservations);
    }

    public ReservationDetailProjection getReservationById(Long reservationId) {
        log.info("Fetching reservation with id: {}", reservationId);
        TableReservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));
        return mapToReservationDetailProjection(reservation);
    }

    public List<ReservationProjection> getPendingReservations(Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Fetching pending reservations for restaurant: {} between {} and {}", restaurantId, fromDate, toDate);
        List<TableReservation> reservations = reservationRepository.findPendingReservations(restaurantId, fromDate, toDate);
        return reservations.stream()
                .map(this::mapToReservationProjection)
                .collect(Collectors.toList());
    }

    public List<ReservationProjection> getConfirmedReservations(Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Fetching confirmed reservations for restaurant: {} between {} and {}", restaurantId, fromDate, toDate);
        List<TableReservation> reservations = reservationRepository.findConfirmedReservations(restaurantId, fromDate, toDate);
        return reservations.stream()
                .map(this::mapToReservationProjection)
                .collect(Collectors.toList());
    }

    public List<ReservationProjection> getNoShowReservations(Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Fetching no-show reservations for restaurant: {} between {} and {}", restaurantId, fromDate, toDate);
        List<TableReservation> reservations = reservationRepository.findNoShowReservations(restaurantId, fromDate, toDate);
        return reservations.stream()
                .map(this::mapToReservationProjection)
                .collect(Collectors.toList());
    }

    public Long getReservationCountByStatus(Long restaurantId, ReservationStatus status) {
        log.info("Getting reservation count for restaurant: {} with status: {}", restaurantId, status);
        return reservationRepository.countByRestaurantIdAndStatus(restaurantId, status);
    }

    private Page<ReservationProjection> mapToReservationProjection(Page<TableReservation> reservations) {
        List<ReservationProjection> content = reservations.getContent()
                .stream()
                .map(this::mapToReservationProjection)
                .collect(Collectors.toList());
        return new PageImpl<>(content, reservations.getPageable(), reservations.getTotalElements());
    }

    private ReservationProjection mapToReservationProjection(TableReservation reservation) {
        return ReservationProjection.builder()
                .id(reservation.getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .guestCount(reservation.getGuestCount())
                .customerId(reservation.getCustomerId())
                .guestName(reservation.getGuestName())
                .guestPhone(reservation.getGuestPhone())
                .status(reservation.getStatus())
                .tableId(reservation.getTable() != null ? reservation.getTable().getId() : null)
                .tableNumber(reservation.getTable() != null ? reservation.getTable().getTableNumber() : null)
                .checkInDateTime(reservation.getCheckInDateTime())
                .checkOutDateTime(reservation.getCheckOutDateTime())
                .noShow(reservation.getNoShow())
                .build();
    }

    private ReservationDetailProjection mapToReservationDetailProjection(TableReservation reservation) {
        return ReservationDetailProjection.builder()
                .id(reservation.getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .guestCount(reservation.getGuestCount())
                .customerId(reservation.getCustomerId())
                .guestName(reservation.getGuestName())
                .guestPhone(reservation.getGuestPhone())
                .guestEmail(reservation.getGuestEmail())
                .status(reservation.getStatus())
                .restaurantId(reservation.getRestaurantId())
                .tableId(reservation.getTable() != null ? reservation.getTable().getId() : null)
                .tableNumber(reservation.getTable() != null ? reservation.getTable().getTableNumber() : null)
                .specialRequests(reservation.getSpecialRequests())
                .checkInDateTime(reservation.getCheckInDateTime())
                .checkOutDateTime(reservation.getCheckOutDateTime())
                .noShow(reservation.getNoShow())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }
}
