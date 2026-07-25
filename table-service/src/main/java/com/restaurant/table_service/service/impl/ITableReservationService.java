package com.restaurant.table_service.service.impl;

import com.restaurant.table_service.dto.ApiResponse;
import com.restaurant.table_service.dto.ReservationDetailProjection;
import com.restaurant.table_service.dto.ReservationProjection;
import com.restaurant.table_service.dto.table_reservation.request.ReservationRequest;
import com.restaurant.table_service.dto.table_reservation.response.ReservationResponse;
import com.restaurant.table_service.entity.table.enums.ReservationStatus;
import com.restaurant.table_service.request.ReservationFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface ITableReservationService {


    ApiResponse<Page<ReservationProjection>> getAllReservations(Long restaurantId, Pageable pageable);

    ApiResponse<Page<ReservationProjection>> getReservationsByStatus(Long restaurantId, ReservationStatus status, Pageable pageable);

    ApiResponse<Page<ReservationProjection>> getCustomerReservations(Long customerId, Pageable pageable);

    ApiResponse<Page<ReservationProjection>> getCustomerReservationsByRestaurant(Long customerId, Long restaurantId, Pageable pageable);

    ApiResponse<Page<ReservationProjection>> filterReservations(ReservationFilterRequest request);

    ApiResponse<ReservationDetailProjection> getReservationById(Long reservationId);

    ApiResponse<List<ReservationProjection>> getPendingReservations(Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate);

    ApiResponse<List<ReservationProjection>> getConfirmedReservations(Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate);

    ApiResponse<List<ReservationProjection>> getNoShowReservations(Long restaurantId, LocalDateTime fromDate, LocalDateTime toDate);

    ApiResponse<Long> getReservationCountByStatus(Long restaurantId, ReservationStatus status);

    ApiResponse<ReservationResponse> createReservation(ReservationRequest request);

    ApiResponse<ReservationResponse> updateReservation(Long reservationId, ReservationRequest request);

    ApiResponse<ReservationResponse> updateReservationStatus(Long reservationId, ReservationRequest request, Long uid);
}
