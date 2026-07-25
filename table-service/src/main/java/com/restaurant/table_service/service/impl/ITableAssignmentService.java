package com.restaurant.table_service.service.impl;

import com.restaurant.table_service.dto.ApiResponse;
import com.restaurant.table_service.dto.TableAssignmentDetailProjection;
import com.restaurant.table_service.dto.TableAssignmentProjection;
import com.restaurant.table_service.dto.table_assignment.request.TableAssignmentRequest;
import com.restaurant.table_service.request.TableAssignmentFilterRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface ITableAssignmentService {
    ApiResponse<Page<TableAssignmentProjection>> getAllAssignments(Long restaurantId, Pageable pageable);

    ApiResponse<Page<TableAssignmentProjection>> getAssignmentsByOrder(Long orderId, Pageable pageable);

    ApiResponse<Page<TableAssignmentProjection>> getAssignmentsByCustomer(Long customerId, Pageable pageable);

    ApiResponse<Page<TableAssignmentProjection>> filterAssignments(TableAssignmentFilterRequest request);

    ApiResponse<TableAssignmentDetailProjection> getAssignmentById(Long assignmentId);

    ApiResponse<List<TableAssignmentProjection>> getActiveAssignments(Long restaurantId);

    ApiResponse<List<TableAssignmentProjection>> getActiveAssignmentsByTable(Long tableId);

    ApiResponse<List<TableAssignmentProjection>> getAssignmentsByTableAndActive(Long tableId, Boolean active);

    ApiResponse<Long> getAssignmentCountByRestaurantAndActive(Long restaurantId, Boolean active);

    ApiResponse<Long> getAssignmentCountByTableAndActive(Long tableId, Boolean active);

    ApiResponse<TableAssignmentProjection> createTableAssignment(@Valid TableAssignmentRequest tableAssignmentRequest);

    ApiResponse<TableAssignmentProjection> vacateTableAssignment(Long assignmentId, Long uid);

    ApiResponse<TableAssignmentProjection> updateTableAssignment(@Valid TableAssignmentRequest tableAssignmentRequest);

}
