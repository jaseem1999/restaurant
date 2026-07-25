package com.restaurant.table_service.service.impl;

import com.restaurant.table_service.dto.TableDetailProjection;
import com.restaurant.table_service.dto.TableProjection;
import com.restaurant.table_service.dto.ApiResponse;
import com.restaurant.table_service.dto.table.response.TableResponse;
import com.restaurant.table_service.entity.table.Table;
import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.request.TableFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITableService {
    ApiResponse<TableResponse> createTable(Table table);

    Page<TableProjection> getAllTables(Long restaurantId, Pageable pageable);

    Page<TableProjection> getTablesByStatus(Long restaurantId, TableStatus status, Pageable pageable);

    Page<TableProjection> filterTables(TableFilterRequest request);

    TableDetailProjection getTableById(Long tableId);

    List<TableProjection> getAvailableTablesForCapacity(Long restaurantId, Integer guestCount);

    List<TableProjection> getTablesByFloor(Long restaurantId, String floor);

    List<TableProjection> getTablesBySection(Long restaurantId, String section);

    Long getTableCountByStatus(Long restaurantId, TableStatus status);

    ApiResponse<TableResponse> updateTable(Long id, Table table);
}
