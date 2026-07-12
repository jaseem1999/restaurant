package com.restaurant.table_service.repository;

import com.restaurant.table_service.entity.table.TableAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TableAssignmentRepository extends JpaRepository<TableAssignment, Long> {

    Page<TableAssignment> findByRestaurantId(Long restaurantId, Pageable pageable);

    Page<TableAssignment> findByOrderId(Long orderId, Pageable pageable);

    Page<TableAssignment> findByCustomerId(Long customerId, Pageable pageable);

    List<TableAssignment> findByTableIdAndActive(Long tableId, Boolean active);

    List<TableAssignment> findByRestaurantIdAndActive(Long restaurantId, Boolean active);

    @Query("SELECT ta FROM TableAssignment ta WHERE ta.restaurantId = :restaurantId " +
            "AND (:customerId IS NULL OR ta.customerId = :customerId) " +
            "AND (:active IS NULL OR ta.active = :active) " +
            "AND (:assignedFromDate IS NULL OR ta.assignedAt >= :assignedFromDate) " +
            "AND (:assignedToDate IS NULL OR ta.assignedAt <= :assignedToDate)")
    Page<TableAssignment> findAssignmentsByFilters(
            @Param("restaurantId") Long restaurantId,
            @Param("customerId") Long customerId,
            @Param("active") Boolean active,
            @Param("assignedFromDate") LocalDateTime assignedFromDate,
            @Param("assignedToDate") LocalDateTime assignedToDate,
            Pageable pageable
    );

    @Query("SELECT ta FROM TableAssignment ta WHERE ta.restaurantId = :restaurantId " +
            "AND ta.active = true " +
            "AND ta.vacatedAt IS NULL")
    List<TableAssignment> findActiveAssignments(
            @Param("restaurantId") Long restaurantId
    );

    @Query("SELECT ta FROM TableAssignment ta WHERE ta.table.id = :tableId " +
            "AND ta.active = true " +
            "AND ta.vacatedAt IS NULL")
    List<TableAssignment> findActiveAssignmentsByTable(
            @Param("tableId") Long tableId
    );

    Long countByRestaurantIdAndActive(Long restaurantId, Boolean active);

    Long countByTableIdAndActive(Long tableId, Boolean active);
}
