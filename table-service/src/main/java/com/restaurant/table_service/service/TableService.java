package com.restaurant.table_service.service;

import com.restaurant.table_service.dto.TableDetailProjection;
import com.restaurant.table_service.dto.TableProjection;
import com.restaurant.table_service.entity.table.Table;
import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.request.TableFilterRequest;
import com.restaurant.table_service.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TableService {

    private final TableRepository tableRepository;

    public Page<TableProjection> getAllTables(Long restaurantId, Pageable pageable) {
        log.info("Fetching all tables for restaurant: {}", restaurantId);
        Page<Table> tables = tableRepository.findByRestaurantId(restaurantId, pageable);
        return mapToTableProjection(tables);
    }

    public Page<TableProjection> getTablesByStatus(Long restaurantId, TableStatus status, Pageable pageable) {
        log.info("Fetching tables for restaurant: {} with status: {}", restaurantId, status);
        Page<Table> tables = tableRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);
        return mapToTableProjection(tables);
    }

    public Page<TableProjection> filterTables(TableFilterRequest request) {
        log.info("Filtering tables with request: {}", request);
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        Page<Table> tables = tableRepository.findTableByFilters(
                request.getRestaurantId(),
                request.getStatus(),
                request.getTableType(),
                request.getCapacity(),
                request.getFloor(),
                request.getSection(),
                request.getActive(),
                pageable
        );
        return mapToTableProjection(tables);
    }

    public TableDetailProjection getTableById(Long tableId) {
        log.info("Fetching table with id: {}", tableId);
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + tableId));
        return mapToTableDetailProjection(table);
    }

    public List<TableProjection> getAvailableTablesForCapacity(Long restaurantId, Integer guestCount) {
        log.info("Finding available tables for restaurant: {} with capacity: {}", restaurantId, guestCount);
        List<Table> tables = tableRepository.findAvailableTableForCapacity(restaurantId, guestCount);
        return tables.stream()
                .map(this::mapToTableProjection)
                .collect(Collectors.toList());
    }

    public List<TableProjection> getTablesByFloor(Long restaurantId, String floor) {
        log.info("Fetching tables for restaurant: {} on floor: {}", restaurantId, floor);
        List<Table> tables = tableRepository.findTableByFloor(restaurantId, floor);
        return tables.stream()
                .map(this::mapToTableProjection)
                .collect(Collectors.toList());
    }

    public List<TableProjection> getTablesBySection(Long restaurantId, String section) {
        log.info("Fetching tables for restaurant: {} in section: {}", restaurantId, section);
        List<Table> tables = tableRepository.findTableBySection(restaurantId, section);
        return tables.stream()
                .map(this::mapToTableProjection)
                .collect(Collectors.toList());
    }

    public Long getTableCountByStatus(Long restaurantId, TableStatus status) {
        log.info("Getting table count for restaurant: {} with status: {}", restaurantId, status);
        return tableRepository.countByRestaurantIdAndStatus(restaurantId, status);
    }

    private Page<TableProjection> mapToTableProjection(Page<Table> tables) {
        List<TableProjection> content = tables.getContent()
                .stream()
                .map(this::mapToTableProjection)
                .collect(Collectors.toList());
        return new PageImpl<>(content, tables.getPageable(), tables.getTotalElements());
    }

    private TableProjection mapToTableProjection(Table table) {
        return TableProjection.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .status(table.getStatus())
                .tableType(table.getTableType())
                .location(table.getLocation())
                .floor(table.getFloor())
                .section(table.getSection())
                .active(table.getActive())
                .build();
    }

    private TableDetailProjection mapToTableDetailProjection(Table table) {
        return TableDetailProjection.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .status(table.getStatus())
                .tableType(table.getTableType())
                .restaurantId(table.getRestaurantId())
                .location(table.getLocation())
                .floor(table.getFloor())
                .section(table.getSection())
                .active(table.getActive())
                .notes(table.getNotes())
                .createdAt(table.getCreatedAt())
                .updatedAt(table.getUpdatedAt())
                .build();
    }
}
