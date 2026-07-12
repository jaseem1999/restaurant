package com.restaurant.table_service.repository;

import com.restaurant.table_service.entity.table.TableReservation;
import com.restaurant.table_service.entity.table.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TableReservationRepository extends JpaRepository<TableReservation, Long> {

    Page<TableReservation> findByRestaurantId(Long restaurantId, Pageable pageable);

    Page<TableReservation> findByRestaurantIdAndStatus(Long restaurantId, ReservationStatus status, Pageable pageable);

    Page<TableReservation> findByCustomerId(Long customerId, Pageable pageable);

    Page<TableReservation> findByCustomerIdAndRestaurantId(Long customerId, Long restaurantId, Pageable pageable);

    @Query("SELECT r FROM TableReservation r WHERE r.restaurantId = :restaurantId " +
            "AND (:customerId IS NULL OR r.customerId = :customerId) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:reservationDateFrom IS NULL OR r.reservationDateTime >= :reservationDateFrom) " +
            "AND (:reservationDateTo IS NULL OR r.reservationDateTime <= :reservationDateTo) " +
            "AND (:noShow IS NULL OR r.noShow = :noShow)")
    Page<TableReservation> findReservationsByFilters(
            @Param("restaurantId") Long restaurantId,
            @Param("customerId") Long customerId,
            @Param("status") ReservationStatus status,
            @Param("reservationDateFrom") LocalDateTime reservationDateFrom,
            @Param("reservationDateTo") LocalDateTime reservationDateTo,
            @Param("noShow") Boolean noShow,
            Pageable pageable
    );

    @Query("SELECT r FROM TableReservation r WHERE r.restaurantId = :restaurantId " +
            "AND r.status = com.restaurant.table_service.entity.table.enums.ReservationStatus.PENDING " +
            "AND r.reservationDateTime >= :fromDate " +
            "AND r.reservationDateTime <= :toDate")
    List<TableReservation> findPendingReservations(
            @Param("restaurantId") Long restaurantId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query("SELECT r FROM TableReservation r WHERE r.restaurantId = :restaurantId " +
            "AND r.status = com.restaurant.table_service.entity.table.enums.ReservationStatus.CONFIRMED " +
            "AND r.reservationDateTime >= :fromDate " +
            "AND r.reservationDateTime <= :toDate")
    List<TableReservation> findConfirmedReservations(
            @Param("restaurantId") Long restaurantId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query("SELECT r FROM TableReservation r WHERE r.restaurantId = :restaurantId " +
            "AND r.noShow = true " +
            "AND r.reservationDateTime >= :fromDate " +
            "AND r.reservationDateTime <= :toDate")
    List<TableReservation> findNoShowReservations(
            @Param("restaurantId") Long restaurantId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    Long countByRestaurantIdAndStatus(Long restaurantId, ReservationStatus status);
}
