package com.restaurant.table_service.repository;

import com.restaurant.table_service.entity.table.Table;
import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.entity.table.enums.TableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {

    Optional<Table> findByTableNumberAndRestaurantId(String tableNumber, Long restaurantId);

    List<Table> findByRestaurantIdAndStatus(Long restaurantId, TableStatus status);

    List<Table> findByRestaurantIdAndStatusAndActive(Long restaurantId, TableStatus status, Boolean active);

    Page<Table> findByRestaurantId(Long restaurantId, Pageable pageable);

    Page<Table> findByRestaurantIdAndStatus(Long restaurantId, TableStatus status, Pageable pageable);

    Page<Table> findByRestaurantIdAndTableType(Long restaurantId, TableType tableType, Pageable pageable);

    @Query("SELECT t FROM Table t WHERE t.restaurantId = :restaurantId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:tableType IS NULL OR t.tableType = :tableType) " +
            "AND (:capacity IS NULL OR t.capacity >= :capacity) " +
            "AND (:floor IS NULL OR t.floor = :floor) " +
            "AND (:section IS NULL OR t.section = :section) " +
            "AND (:active IS NULL OR t.active = :active)")
    Page<Table> findTableByFilters(
            @Param("restaurantId") Long restaurantId,
            @Param("status") TableStatus status,
            @Param("tableType") TableType tableType,
            @Param("capacity") Integer capacity,
            @Param("floor") String floor,
            @Param("section") String section,
            @Param("active") Boolean active,
            Pageable pageable
    );

    @Query("SELECT t FROM Table t WHERE t.restaurantId = :restaurantId " +
            "AND t.status = com.restaurant.table_service.entity.table.enums.TableStatus.AVAILABLE " +
            "AND t.capacity >= :guestCount " +
            "AND t.active = true")
    List<Table> findAvailableTableForCapacity(
            @Param("restaurantId") Long restaurantId,
            @Param("guestCount") Integer guestCount
    );

    @Query("SELECT t FROM Table t WHERE t.restaurantId = :restaurantId " +
            "AND t.floor = :floor " +
            "AND t.active = true")
    List<Table> findTableByFloor(
            @Param("restaurantId") Long restaurantId,
            @Param("floor") String floor
    );

    @Query("SELECT t FROM Table t WHERE t.restaurantId = :restaurantId " +
            "AND t.section = :section " +
            "AND t.active = true")
    List<Table> findTableBySection(
            @Param("restaurantId") Long restaurantId,
            @Param("section") String section
    );

    Long countByRestaurantIdAndStatus(Long restaurantId, TableStatus status);

    Long countByRestaurantIdAndStatusAndActive(Long restaurantId, TableStatus status, Boolean active);
}
