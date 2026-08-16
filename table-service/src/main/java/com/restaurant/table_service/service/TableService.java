package com.restaurant.table_service.service;

import com.restaurant.table_service.dto.TableDetailProjection;
import com.restaurant.table_service.dto.TableProjection;
import com.restaurant.table_service.dto.ApiResponse;
import com.restaurant.table_service.dto.table.response.TableResponse;
import com.restaurant.table_service.entity.table.Table;
import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.request.TableFilterRequest;
import com.restaurant.table_service.repository.TableRepository;
import com.restaurant.table_service.security.SecurityCheckApisClass;
import com.restaurant.table_service.service.impl.ITableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TableService implements ITableService {

    private final TableRepository tableRepository;

    @Override
    public Page<TableProjection> getAllTables(Long restaurantId, Pageable pageable) {
        log.info("Fetching all tables for restaurant: {}", restaurantId);
        Page<Table> tables = tableRepository.findByRestaurantId(restaurantId, pageable);
        return mapToTableProjection(tables);
    }

    @Override
    public Page<TableProjection> getTablesByStatus(Long restaurantId, TableStatus status, Pageable pageable) {
        log.info("Fetching tables for restaurant: {} with status: {}", restaurantId, status);
        Page<Table> tables = tableRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);
        return mapToTableProjection(tables);
    }

    @Override
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

    @Override
    public TableDetailProjection getTableById(Long tableId) {
        log.info("Fetching table with id: {}", tableId);
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + tableId));
        return mapToTableDetailProjection(table);
    }

    @Override
    public List<TableProjection> getAvailableTablesForCapacity(Long restaurantId, Integer guestCount) {
        log.info("Finding available tables for restaurant: {} with capacity: {}", restaurantId, guestCount);
        List<Table> tables = tableRepository.findAvailableTableForCapacity(restaurantId, guestCount);
        return tables.stream()
                .map(this::mapToTableProjection)
                .collect(Collectors.toList());
    }

    @Override
    public List<TableProjection> getTablesByFloor(Long restaurantId, String floor) {
        log.info("Fetching tables for restaurant: {} on floor: {}", restaurantId, floor);
        List<Table> tables = tableRepository.findTableByFloor(restaurantId, floor);
        return tables.stream()
                .map(this::mapToTableProjection)
                .collect(Collectors.toList());
    }

    @Override
    public List<TableProjection> getTablesBySection(Long restaurantId, String section) {
        log.info("Fetching tables for restaurant: {} in section: {}", restaurantId, section);
        List<Table> tables = tableRepository.findTableBySection(restaurantId, section);
        return tables.stream()
                .map(this::mapToTableProjection)
                .collect(Collectors.toList());
    }

    @Override
    public Long getTableCountByStatus(Long restaurantId, TableStatus status) {
        log.info("Getting table count for restaurant: {} with status: {}", restaurantId, status);
        return tableRepository.countByRestaurantIdAndStatus(restaurantId, status);
    }

    @Override
    public ApiResponse<TableResponse> updateTable(Long id, Table table) {
         com.restaurant.table_service.entity.table.Table t =tableRepository.findById(id).map(
                tableEntity -> {
                    tableEntity.setRestaurantId(table.getRestaurantId() != null ? table.getRestaurantId() : tableEntity.getRestaurantId());
                    tableEntity.setStatus(table.getStatus() != null ? table.getStatus() : tableEntity.getStatus());
                    tableEntity.setTableType(table.getTableType() != null ? table.getTableType() : tableEntity.getTableType());
                    tableEntity.setCapacity(table.getCapacity() != null ? table.getCapacity() : tableEntity.getCapacity());
                    tableEntity.setFloor(table.getFloor() != null ? table.getFloor() : tableEntity.getFloor());
                    tableEntity.setSection(table.getSection() != null ? table.getSection() : tableEntity.getSection());
                    tableEntity.setActive(table.getActive() != null ? table.getActive() : tableEntity.getActive());
                    tableEntity.setUpdatedAt(table.getUpdatedAt());
                    tableEntity.setUpdatedBy(table.getUpdatedBy());
                    tableEntity.setTableNumber(table.getTableNumber() != null ? table.getTableNumber() : tableEntity.getTableNumber());
                    tableEntity.setLocation(table.getLocation() != null ? table.getLocation() : tableEntity.getLocation());
                    try {
                       return tableRepository.save(tableEntity);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).orElse(null);
         if (t == null) {
             return new ApiResponse<>(null,
                     false,
                     "Table not found with id: " + id,
                     HttpStatus.NOT_FOUND);
         }
         return new ApiResponse<>( toDTO(t),
                 true,
                 "Table updated successful",
                 HttpStatus.OK);
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
                .createdBy(table.getCreatedBy())
                .updatedBy(table.getUpdatedBy())
                .createdAt(table.getCreatedAt())
                .updatedAt(table.getUpdatedAt())
                .restaurantId(table.getRestaurantId())
                .notes(table.getNotes())
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


    @Override
    public ApiResponse<TableResponse> createTable(Table table) {
        try {
            table = tableRepository.save(table);
            return new ApiResponse<>(toDTO(table),true,"Table added successful", HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private TableResponse toDTO(Table table) {
        return TableResponse.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .tableStatus(table.getStatus())
                .tableType(table.getTableType())
                .location(table.getLocation())
                .floor(table.getFloor())
                .section(table.getSection())
                .restaurantId(table.getRestaurantId())
                .active(table.getActive())
                .createdBy(table.getCreatedBy())
                .updatedBy(table.getUpdatedBy())
                .updatedAt(table.getUpdatedAt())
                .createdAt(table.getCreatedAt())
                .notes(table.getNotes())
                .build();
    }
}
