package com.restaurant.table_service.service;

import com.restaurant.table_service.dto.ApiResponse;
import com.restaurant.table_service.dto.TableAssignmentDetailProjection;
import com.restaurant.table_service.dto.TableAssignmentProjection;
import com.restaurant.table_service.dto.table_assignment.request.TableAssignmentRequest;
import com.restaurant.table_service.entity.table.Table;
import com.restaurant.table_service.entity.table.TableAssignment;
import com.restaurant.table_service.request.TableAssignmentFilterRequest;
import com.restaurant.table_service.repository.TableAssignmentRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TableAssignmentService implements ITableAssignmentService {

    private final TableAssignmentRepository assignmentRepository;
    private final ITableReservationService tableReservationService;

    @Override
    public ApiResponse<Page<TableAssignmentProjection>> getAllAssignments(Long restaurantId, Pageable pageable) {
        log.info("Fetching all assignments for restaurant: {}", restaurantId);
        Page<TableAssignment> assignments = assignmentRepository.findByRestaurantId(restaurantId, pageable);
        return new ApiResponse<>(mapToTableAssignmentProjection(assignments),true,"fetched successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<Page<TableAssignmentProjection>> getAssignmentsByOrder(Long orderId, Pageable pageable) {
        log.info("Fetching assignments for order: {}", orderId);
        Page<TableAssignment> assignments = assignmentRepository.findByOrderId(orderId, pageable);
        return new ApiResponse<>(mapToTableAssignmentProjection(assignments), true, "fetched successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<Page<TableAssignmentProjection>> getAssignmentsByCustomer(Long customerId, Pageable pageable) {
        log.info("Fetching assignments for customer: {}", customerId);
        Page<TableAssignment> assignments = assignmentRepository.findByCustomerId(customerId, pageable);
        return new ApiResponse<>(mapToTableAssignmentProjection(assignments), true, "fetched successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<Page<TableAssignmentProjection>> filterAssignments(TableAssignmentFilterRequest request) {
        log.info("Filtering assignments with request: {}", request);
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        Page<TableAssignment> assignments = assignmentRepository.findAssignmentsByFilters(
                request.getRestaurantId(),
                request.getCustomerId(),
                request.getActive(),
                request.getAssignedFromDate(),
                request.getAssignedToDate(),
                pageable
        );
        return new ApiResponse<>(mapToTableAssignmentProjection(assignments), true, "fetched successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<TableAssignmentDetailProjection> getAssignmentById(Long assignmentId) {
        log.info("Fetching assignment with id: {}", assignmentId);
        TableAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));
        return new ApiResponse<>(mapToTableAssignmentDetailProjection(assignment), true, "fetched successfully", HttpStatus.OK);
    }

    public ApiResponse<List<TableAssignmentProjection>> getActiveAssignments(Long restaurantId) {
        log.info("Fetching active assignments for restaurant: {}", restaurantId);
        List<TableAssignment> assignments = assignmentRepository.findActiveAssignments(restaurantId);
        return new ApiResponse<>(assignments.stream()
                .map(this::mapToTableAssignmentProjection)
                .collect(Collectors.toList()), true, "fetched successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<TableAssignmentProjection>> getActiveAssignmentsByTable(Long tableId) {
        log.info("Fetching active assignments for table: {}", tableId);
        List<TableAssignment> assignments = assignmentRepository.findActiveAssignmentsByTable(tableId);
        return new ApiResponse<>(assignments.stream()
                .map(this::mapToTableAssignmentProjection)
                .collect(Collectors.toList()), true, "fetched successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<TableAssignmentProjection>> getAssignmentsByTableAndActive(Long tableId, Boolean active) {
        log.info("Fetching assignments for table: {} with active: {}", tableId, active);
        List<TableAssignment> assignments = assignmentRepository.findByTableIdAndActive(tableId, active);
        return new ApiResponse<>(assignments.stream()
                .map(this::mapToTableAssignmentProjection)
                .collect(Collectors.toList()), true, "fetched successfully", HttpStatus.OK);
    }


//    public ApiResponse<List<TableAssignmentProjection>> getAssignmentsByRestaurantAndActive(Long restaurantId, Boolean active) {
//        log.info("Fetching assignments for restaurant: {} with active: {}", restaurantId, active);
//        List<TableAssignment> assignments = assignmentRepository.findByRestaurantIdAndActive(restaurantId, active);
//        return new ApiResponse<>(assignments.stream()
//                .map(this::mapToTableAssignmentProjection)
//                .collect(Collectors.toList()), true, "fetched successfully", HttpStatus.OK);
//    }

    @Override
    public ApiResponse<Long> getAssignmentCountByRestaurantAndActive(Long restaurantId, Boolean active) {
        log.info("Getting assignment count for restaurant: {} with active: {}", restaurantId, active);
        Long count = assignmentRepository.countByRestaurantIdAndActive(restaurantId, active);
        return new ApiResponse<>(count, true, "count fetched successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<Long> getAssignmentCountByTableAndActive(Long tableId, Boolean active) {
        log.info("Getting assignment count for table: {} with active: {}", tableId, active);
        Long count = assignmentRepository.countByTableIdAndActive(tableId, active);
        return new ApiResponse<>(count, true, "count fetched successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<TableAssignmentProjection> createTableAssignment(TableAssignmentRequest tableAssignmentRequest) {
        log.info("Creating new table assignment with request: {}", tableAssignmentRequest);
        TableAssignment assignment = TableAssignment.builder()
                .orderId(tableAssignmentRequest.getOrderId())
                .customerId(tableAssignmentRequest.getCustomerId())
                .restaurantId(tableAssignmentRequest.getRestaurantId())
                .assignedAt(tableAssignmentRequest.getAssignedAt())
                .vacatedAt(tableAssignmentRequest.getVacatedAt())
                .active(true)
                .notes(tableAssignmentRequest.getNotes())
                .build();
        if (tableAssignmentRequest.getTableId() != null) {
            boolean checkIsAlreadyReserved = tableReservationService.isTableReserved(
                    tableAssignmentRequest.getTableId(),
                    tableAssignmentRequest.getAssignedAt(), tableAssignmentRequest.getVacatedAt());
            log.info("Checking if table {} is already reserved for the given period: {}", tableAssignmentRequest.getTableId(), checkIsAlreadyReserved);
            if (checkIsAlreadyReserved) {
                return new ApiResponse<>(null, false, "Table is already reserved for the given period", HttpStatus.NOT_ACCEPTABLE);
            }
            boolean checkIsAlreadyAssignedGivenPeriod = assignmentRepository.findByTableIdAndActive(tableAssignmentRequest.getTableId(), true)
                    .stream()
                    .anyMatch(existingAssignment -> {
                        if (existingAssignment.getVacatedAt() == null) {
                            return true; // Table is currently assigned
                        }
                        // Check for overlapping periods
                        return !(tableAssignmentRequest.getAssignedAt().isAfter(existingAssignment.getVacatedAt()) ||
                                tableAssignmentRequest.getVacatedAt().isBefore(existingAssignment.getAssignedAt()));
                    });
            if (checkIsAlreadyAssignedGivenPeriod) {
                return new ApiResponse<>(null, false, "Table is already assigned for the given period", HttpStatus.NOT_ACCEPTABLE);
            }
            Table table = new Table();
            table.setId(tableAssignmentRequest.getTableId());
            assignment.setTable(table);

        }else{
            return new ApiResponse<>(null, false, "Table ID is required", HttpStatus.NOT_ACCEPTABLE);
        }
        assignment.setCreatedAt(Instant.now());
        assignment.setCreatedBy(tableAssignmentRequest.getCreatedBy());
        TableAssignment savedAssignment = assignmentRepository.save(assignment);
        return new ApiResponse<>(mapToTableAssignmentProjection(savedAssignment), true, "created successfully", HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<TableAssignmentProjection> vacateTableAssignment(Long assignmentId, Long uid) {

        log.info("Vacating table assignment with id: {}", assignmentId);
        TableAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));
        if (!assignment.getActive()) {
            return new ApiResponse<>(null, false, "Assignment is already vacated", HttpStatus.NOT_ACCEPTABLE);
        }
        assignment.setActive(false);
        assignment.setUpdatedAt(Instant.now());
        assignment.setUpdatedBy(uid);
        assignment.setVacatedAt(Instant.now());
        TableAssignment updatedAssignment = assignmentRepository.save(assignment);
        return new ApiResponse<>(mapToTableAssignmentProjection(updatedAssignment), true, "vacated successfully", HttpStatus.OK);
    }

    //:Todo verification needed
    @Override
    public ApiResponse<TableAssignmentProjection> updateTableAssignment(TableAssignmentRequest tableAssignmentRequest) {
        TableAssignment assignment = assignmentRepository.existsById(tableAssignmentRequest.getId()) ?
                assignmentRepository.findById(tableAssignmentRequest.getId()).get() : null;
        if (assignment == null) {
            return new ApiResponse<>(null, false, "Assignment not found with id: " +tableAssignmentRequest.getId(), HttpStatus.NOT_FOUND);
        }
        assignment.setOrderId(tableAssignmentRequest.getOrderId());
        assignment.setCustomerId(tableAssignmentRequest.getCustomerId());
        assignment.setRestaurantId(tableAssignmentRequest.getRestaurantId());
        if (tableAssignmentRequest.getTableId() != null) {
            boolean checkIsAlreadyReserved = tableReservationService.isTableReserved(
                    tableAssignmentRequest.getTableId(),
                    tableAssignmentRequest.getAssignedAt(), tableAssignmentRequest.getVacatedAt());
            if (checkIsAlreadyReserved) {
                return new ApiResponse<>(null, false, "Table is already reserved for the given period", HttpStatus.NOT_ACCEPTABLE);
            }
            boolean checkIsAlreadyAssignedGivenPeriod = assignmentRepository.findByTableIdAndActive(tableAssignmentRequest.getTableId(), true)
                    .stream()
                    .anyMatch(existingAssignment -> {
                        if (existingAssignment.getVacatedAt() == null) {
                            return true; // Table is currently assigned
                        }
                        return false;
                    });
            if (checkIsAlreadyAssignedGivenPeriod) {
                return new ApiResponse<>(null, false, "Table is already assigned for the given period", HttpStatus.CONFLICT);
            }
        }
        assignment.setUpdatedBy(tableAssignmentRequest.getUpdatedBy());
        assignment.setUpdatedAt(Instant.now());
        TableAssignment updatedAssignment = assignmentRepository.save(assignment);
        return new ApiResponse<>(mapToTableAssignmentProjection(updatedAssignment), true, "updated successfully", HttpStatus.OK);
    }

    private Page<TableAssignmentProjection> mapToTableAssignmentProjection(Page<TableAssignment> assignments) {
        List<TableAssignmentProjection> content = assignments.getContent()
                .stream()
                .map(this::mapToTableAssignmentProjection)
                .collect(Collectors.toList());
        return new PageImpl<>(content, assignments.getPageable(), assignments.getTotalElements());
    }

    private TableAssignmentProjection mapToTableAssignmentProjection(TableAssignment assignment) {
        return TableAssignmentProjection.builder()
                .id(assignment.getId())
                .orderId(assignment.getOrderId())
                .customerId(assignment.getCustomerId())
                .tableId(assignment.getTable() != null ? assignment.getTable().getId() : null)
                .tableNumber(assignment.getTable() != null ? assignment.getTable().getTableNumber() : null)
                .assignedAt(assignment.getAssignedAt())
                .vacatedAt(assignment.getVacatedAt())
                .active(assignment.getActive())
                .build();
    }

    private TableAssignmentDetailProjection mapToTableAssignmentDetailProjection(TableAssignment assignment) {
        return TableAssignmentDetailProjection.builder()
                .id(assignment.getId())
                .orderId(assignment.getOrderId())
                .customerId(assignment.getCustomerId())
                .restaurantId(assignment.getRestaurantId())
                .tableId(assignment.getTable() != null ? assignment.getTable().getId() : null)
                .tableNumber(assignment.getTable() != null ? assignment.getTable().getTableNumber() : null)
                .assignedAt(assignment.getAssignedAt())
                .vacatedAt(assignment.getVacatedAt())
                .active(assignment.getActive())
                .notes(assignment.getNotes())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }
}
