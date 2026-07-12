package com.restaurant.table_service.service;

import com.restaurant.table_service.dto.TableAssignmentDetailProjection;
import com.restaurant.table_service.dto.TableAssignmentProjection;
import com.restaurant.table_service.entity.table.TableAssignment;
import com.restaurant.table_service.request.TableAssignmentFilterRequest;
import com.restaurant.table_service.repository.TableAssignmentRepository;
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
public class TableAssignmentService {

    private final TableAssignmentRepository assignmentRepository;

    public Page<TableAssignmentProjection> getAllAssignments(Long restaurantId, Pageable pageable) {
        log.info("Fetching all assignments for restaurant: {}", restaurantId);
        Page<TableAssignment> assignments = assignmentRepository.findByRestaurantId(restaurantId, pageable);
        return mapToTableAssignmentProjection(assignments);
    }

    public Page<TableAssignmentProjection> getAssignmentsByOrder(Long orderId, Pageable pageable) {
        log.info("Fetching assignments for order: {}", orderId);
        Page<TableAssignment> assignments = assignmentRepository.findByOrderId(orderId, pageable);
        return mapToTableAssignmentProjection(assignments);
    }

    public Page<TableAssignmentProjection> getAssignmentsByCustomer(Long customerId, Pageable pageable) {
        log.info("Fetching assignments for customer: {}", customerId);
        Page<TableAssignment> assignments = assignmentRepository.findByCustomerId(customerId, pageable);
        return mapToTableAssignmentProjection(assignments);
    }

    public Page<TableAssignmentProjection> filterAssignments(TableAssignmentFilterRequest request) {
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
        return mapToTableAssignmentProjection(assignments);
    }

    public TableAssignmentDetailProjection getAssignmentById(Long assignmentId) {
        log.info("Fetching assignment with id: {}", assignmentId);
        TableAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));
        return mapToTableAssignmentDetailProjection(assignment);
    }

    public List<TableAssignmentProjection> getActiveAssignments(Long restaurantId) {
        log.info("Fetching active assignments for restaurant: {}", restaurantId);
        List<TableAssignment> assignments = assignmentRepository.findActiveAssignments(restaurantId);
        return assignments.stream()
                .map(this::mapToTableAssignmentProjection)
                .collect(Collectors.toList());
    }

    public List<TableAssignmentProjection> getActiveAssignmentsByTable(Long tableId) {
        log.info("Fetching active assignments for table: {}", tableId);
        List<TableAssignment> assignments = assignmentRepository.findActiveAssignmentsByTable(tableId);
        return assignments.stream()
                .map(this::mapToTableAssignmentProjection)
                .collect(Collectors.toList());
    }

    public List<TableAssignmentProjection> getAssignmentsByTableAndActive(Long tableId, Boolean active) {
        log.info("Fetching assignments for table: {} with active: {}", tableId, active);
        List<TableAssignment> assignments = assignmentRepository.findByTableIdAndActive(tableId, active);
        return assignments.stream()
                .map(this::mapToTableAssignmentProjection)
                .collect(Collectors.toList());
    }

    public List<TableAssignmentProjection> getAssignmentsByRestaurantAndActive(Long restaurantId, Boolean active) {
        log.info("Fetching assignments for restaurant: {} with active: {}", restaurantId, active);
        List<TableAssignment> assignments = assignmentRepository.findByRestaurantIdAndActive(restaurantId, active);
        return assignments.stream()
                .map(this::mapToTableAssignmentProjection)
                .collect(Collectors.toList());
    }

    public Long getAssignmentCountByRestaurantAndActive(Long restaurantId, Boolean active) {
        log.info("Getting assignment count for restaurant: {} with active: {}", restaurantId, active);
        return assignmentRepository.countByRestaurantIdAndActive(restaurantId, active);
    }

    public Long getAssignmentCountByTableAndActive(Long tableId, Boolean active) {
        log.info("Getting assignment count for table: {} with active: {}", tableId, active);
        return assignmentRepository.countByTableIdAndActive(tableId, active);
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
