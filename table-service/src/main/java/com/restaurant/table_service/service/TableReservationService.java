package com.restaurant.table_service.service;

import com.restaurant.table_service.dto.ApiResponse;
import com.restaurant.table_service.dto.ReservationDetailProjection;
import com.restaurant.table_service.dto.ReservationProjection;
import com.restaurant.table_service.dto.table_reservation.request.ReservationRequest;
import com.restaurant.table_service.dto.table_reservation.response.ReservationResponse;
import com.restaurant.table_service.entity.table.Table;
import com.restaurant.table_service.entity.table.TableAssignment;
import com.restaurant.table_service.entity.table.TableReservation;
import com.restaurant.table_service.entity.table.enums.ReservationStatus;
import com.restaurant.table_service.repository.TableAssignmentRepository;
import com.restaurant.table_service.request.ReservationFilterRequest;
import com.restaurant.table_service.repository.TableReservationRepository;
import com.restaurant.table_service.service.impl.ITableAssignmentService;
import com.restaurant.table_service.service.impl.ITableReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TableReservationService implements ITableReservationService {

    private final TableReservationRepository reservationRepository;
    private final TableAssignmentRepository assignmentRepository;

     private boolean isTableReserved(Long tableId, Instant assignedAt, Instant vacatedAt) {
        boolean isReserved = reservationRepository.findByTableIdAndStatusIn(tableId, Arrays.asList(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CHECKED_IN)).stream()
                .anyMatch(reservation -> {
                    Instant reservationStart = reservation.getReservationDateTime();
                    Instant reservationEnd = reservation.getCheckOutDateTime() != null ? reservation.getCheckOutDateTime() : reservationStart.plusSeconds(3600); // Assuming 1 hour if check-out is not set
                    return (assignedAt.isBefore(reservationEnd) && vacatedAt.isAfter(reservationStart));
                });
        return isReserved;
    }

    private boolean isTableAssigned(Long tableId, Instant checkInDateTime, Instant checkOutDateTime) {
        log.info("Checking if table {} is assigned between {} and {}", tableId, checkInDateTime, checkOutDateTime);
        List<TableAssignment> assignments = assignmentRepository.findByTableIdAndActive(tableId, true);
        return assignments.stream().anyMatch(assignment -> {
            if (assignment.getVacatedAt() == null) {
                return true; // Table is currently assigned
            }
            // Check for overlapping periods
            return !(checkInDateTime.isAfter(assignment.getVacatedAt()) || checkOutDateTime.isBefore(assignment.getAssignedAt()));
        });
    }
    @Override
    public ApiResponse<Page<ReservationProjection>> getAllReservations(Long restaurantId, Pageable pageable) {
        log.info("Fetching all reservations for restaurant: {}", restaurantId);
        Page<TableReservation> reservations = reservationRepository.findByRestaurantId(restaurantId, pageable);
        return new ApiResponse<>(mapToReservationProjection(reservations),true,"Fetching all reservations", HttpStatus.OK);
    }

    @Override
    public ApiResponse<Page<ReservationProjection>> getReservationsByStatus(Long restaurantId, ReservationStatus status, Pageable pageable) {
        log.info("Fetching reservations for restaurant: {} with status: {}", restaurantId, status);
        Page<TableReservation> reservations = reservationRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);
        return new ApiResponse<>(mapToReservationProjection(reservations), true, "Fetching reservations by status", HttpStatus.OK);
    }

    @Override
    public ApiResponse<Page<ReservationProjection>> getCustomerReservations(Long customerId, Pageable pageable) {
        log.info("Fetching reservations for customer: {}", customerId);
        Page<TableReservation> reservations = reservationRepository.findByCustomerId(customerId, pageable);
        return new ApiResponse<>(mapToReservationProjection(reservations), true, "Fetching reservations for customer", HttpStatus.OK);
    }

    @Override
    public ApiResponse<Page<ReservationProjection>> getCustomerReservationsByRestaurant(Long customerId, Long restaurantId, Pageable pageable) {
        log.info("Fetching reservations for customer: {} at restaurant: {}", customerId, restaurantId);
        Page<TableReservation> reservations = reservationRepository.findByCustomerIdAndRestaurantId(customerId, restaurantId, pageable);
        return new ApiResponse<>(mapToReservationProjection(reservations), true, "Fetching reservations for customer at restaurant", HttpStatus.OK);
    }

    @Override
    public ApiResponse<Page<ReservationProjection>> filterReservations(ReservationFilterRequest request) {
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

        return new ApiResponse<>(mapToReservationProjection(reservations), true, "Filtering reservations", HttpStatus.OK);
    }

    @Override
    public ApiResponse<ReservationDetailProjection> getReservationById(Long reservationId) {
        log.info("Fetching reservation with id: {}", reservationId);
        TableReservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));
        return new ApiResponse<>(mapToReservationDetailProjection(reservation), true, "Fetching reservation by id", HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<ReservationProjection>> getPendingReservations(Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Fetching pending reservations for restaurant: {} between {} and {}", restaurantId, fromDate, toDate);
        List<TableReservation> reservations = reservationRepository.findPendingReservations(restaurantId, fromDate, toDate);
        return new ApiResponse<>(reservations.stream()
                .map(this::mapToReservationProjection)
                .collect(Collectors.toList()), true, "Fetching pending reservations", HttpStatus.OK);
    }

    public ApiResponse<List<ReservationProjection>> getConfirmedReservations(Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Fetching confirmed reservations for restaurant: {} between {} and {}", restaurantId, fromDate, toDate);
        List<TableReservation> reservations = reservationRepository.findConfirmedReservations(restaurantId, fromDate, toDate);
        return new ApiResponse<>(reservations.stream()
                .map(this::mapToReservationProjection)
                .collect(Collectors.toList()), true, "Fetching confirmed reservations", HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<ReservationProjection>> getNoShowReservations(Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Fetching no-show reservations for restaurant: {} between {} and {}", restaurantId, fromDate, toDate);
        List<TableReservation> reservations = reservationRepository.findNoShowReservations(restaurantId, fromDate, toDate);
        return new ApiResponse<>(reservations.stream()
                .map(this::mapToReservationProjection)
                .collect(Collectors.toList()), true, "Fetching no-show reservations", HttpStatus.OK);
    }

    @Override
    public ApiResponse<Long> getReservationCountByStatus(Long restaurantId, ReservationStatus status) {
        log.info("Getting reservation count for restaurant: {} with status: {}", restaurantId, status);
        Long count = reservationRepository.countByRestaurantIdAndStatus(restaurantId, status);
        return new ApiResponse<>(count, true, "Fetching reservation count", HttpStatus.OK);
    }

    @Override
    public ApiResponse<ReservationResponse> createReservation(ReservationRequest request) {
        log.info("Creating reservation with request: {}", request);
        TableReservation reservation = TableReservation.builder()
                .reservationDateTime(request.getReservationDateTime())
                .guestCount(request.getGuestCount())
                .customerId(request.getCustomerId())
                .guestName(request.getGuestName())
                .guestPhone(request.getGuestPhone())
                .guestEmail(request.getGuestEmail())
                .status(request.getStatus())
                .restaurantId(request.getRestaurantId())
                .specialRequests(request.getSpecialRequests())
                .checkInDateTime(request.getCheckInDateTime())
                .checkOutDateTime(request.getCheckOutDateTime())
                .noShow(request.getNoShow())
                .build();
        if (request.getTableId() != null) {
            boolean isTableAlreadyReserved = isTableReserved(request.getTableId(), request.getReservationDateTime(), request.getCheckOutDateTime());
            if (isTableAlreadyReserved) {
                return new ApiResponse<>(null, false, "Table is already reserved for the selected time", HttpStatus.CONFLICT);
            }
            boolean isTableAlreadyAssigned = isTableAssigned(request.getTableId(), request.getReservationDateTime(), request.getCheckOutDateTime());
            if (isTableAlreadyAssigned) {
                return new ApiResponse<>(null, false, "Table is already assigned", HttpStatus.CONFLICT);
            }

            Table table = new Table();
            table.setId(request.getTableId());
            reservation.setTable(table);
        }else {
            return new ApiResponse<>(null, false, "Table ID is required for reservation", HttpStatus.BAD_REQUEST);
        }

        if(request.getCreatedBy() != null) {
            reservation.setCreatedBy(request.getCreatedBy());
        }else {
            return new ApiResponse<>(null, false, "CreatedBy is required for reservation", HttpStatus.BAD_REQUEST);
        }

        reservation.setCreatedAt(Instant.now());
        TableReservation savedReservation = reservationRepository.save(reservation);


        return new ApiResponse<>( toDto(savedReservation), true, "Reservation created successfully", HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<ReservationResponse> updateReservation(Long reservationId, ReservationRequest request) {
        log.info("Updating reservation with id: {} and request: {}", reservationId, request);
        TableReservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));
        existingReservation.setReservationDateTime(request.getReservationDateTime());
        existingReservation.setGuestCount(request.getGuestCount());
        existingReservation.setCustomerId(request.getCustomerId());
        existingReservation.setGuestName(request.getGuestName());
        existingReservation.setGuestPhone(request.getGuestPhone());
        existingReservation.setGuestEmail(request.getGuestEmail());
        existingReservation.setStatus(request.getStatus());
        existingReservation.setRestaurantId(request.getRestaurantId());
        existingReservation.setSpecialRequests(request.getSpecialRequests());
        existingReservation.setCheckInDateTime(request.getCheckInDateTime());
        existingReservation.setCheckOutDateTime(request.getCheckOutDateTime());
        existingReservation.setNoShow(request.getNoShow());

        if (request.getTableId() != null) {
            boolean isTableAlreadyReserved = isTableReserved(request.getTableId(), request.getReservationDateTime(), request.getCheckOutDateTime());
            if (isTableAlreadyReserved && !existingReservation.getTable().getId().equals(request.getTableId())) {
                return new ApiResponse<>(null, false, "Table is already reserved for the selected time", HttpStatus.CONFLICT);
            }
            boolean isTableAlreadyAssigned = isTableAssigned(request.getTableId(), request.getReservationDateTime(), request.getCheckOutDateTime());
            if (isTableAlreadyAssigned && !existingReservation.getTable().getId().equals(request.getTableId())) {
                return new ApiResponse<>(null, false, "Table is already assigned", HttpStatus.CONFLICT);
            }

            Table table = new Table();
            table.setId(request.getTableId());
            existingReservation.setTable(table);
        }

        if (request.getUpdatedBy() != null) {
            existingReservation.setUpdatedBy(request.getUpdatedBy());
        } else {
            return new ApiResponse<>(null, false, "UpdatedBy is required for reservation update", HttpStatus.BAD_REQUEST);
        }

        existingReservation.setUpdatedAt(Instant.now());

        TableReservation updatedReservation = reservationRepository.save(existingReservation);

        return new ApiResponse<>(toDto(updatedReservation), true, "Reservation updated successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<ReservationResponse> updateReservationStatus(Long reservationId, ReservationRequest request, Long uid) {
        AtomicReference<TableReservation> reservation = new AtomicReference<>();
        reservationRepository.findById(reservationId).ifPresent(
                existingReservation -> {
                    existingReservation.setStatus(request.getStatus());
                    existingReservation.setUpdatedBy(uid);
                    existingReservation.setUpdatedAt(Instant.now());
                    if (request.getStatus() == ReservationStatus.CHECKED_IN) {
                        existingReservation.setCheckInDateTime(Instant.now());
                    } else if (request.getStatus() == ReservationStatus.COMPLETED) {
                        existingReservation.setCheckOutDateTime(Instant.now());
                    } else if (request.getStatus() == ReservationStatus.CANCELLED) {
                        existingReservation.setNoShow(true);
                    } else if (request.getStatus() == ReservationStatus.CONFIRMED) {
                        existingReservation.setNoShow(false);
                    }
                    reservation.set(reservationRepository.save(existingReservation));
                }
        );
        if (reservation.get() == null) {
            return new ApiResponse<>(null, false,
                    "Reservation not found with id: " + reservationId,
                    HttpStatus.NOT_FOUND);
        }
        return new ApiResponse<>(toDto(reservation.get()),
                true,
                "Reservation status updated successfully",
                HttpStatus.OK);
    }

    private ReservationResponse toDto(TableReservation savedReservation) {
        return ReservationResponse.builder()
                .reservationId(savedReservation.getId())
                .reservationDateTime(savedReservation.getReservationDateTime())
                .guestCount(savedReservation.getGuestCount())
                .guestName(savedReservation.getGuestName())
                .guestPhone(savedReservation.getGuestPhone())
                .guestEmail(savedReservation.getGuestEmail())
                .status(savedReservation.getStatus())
                .restaurantId(savedReservation.getRestaurantId())
                .specialRequests(savedReservation.getSpecialRequests())
                .checkInDateTime(savedReservation.getCheckInDateTime())
                .checkOutDateTime(savedReservation.getCheckOutDateTime())
                .noShow(savedReservation.getNoShow())
                .tableId(savedReservation.getTable() != null ? savedReservation.getTable().getId() : null)
                .build();
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
