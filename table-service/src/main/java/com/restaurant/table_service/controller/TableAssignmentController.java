package com.restaurant.table_service.controller;

import com.restaurant.table_service.dto.TableAssignmentDetailProjection;
import com.restaurant.table_service.dto.TableAssignmentProjection;
import com.restaurant.table_service.request.TableAssignmentFilterRequest;
import com.restaurant.table_service.service.TableAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class TableAssignmentController {

    private final TableAssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<Page<TableAssignmentProjection>> getAllAssignments(
            @RequestParam Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/assignments - restaurantId: {}, page: {}, size: {}", restaurantId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<TableAssignmentProjection> assignments = assignmentService.getAllAssignments(restaurantId, pageable);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<TableAssignmentProjection>> getAssignmentsByOrder(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/assignments/order/{}", orderId);
        Pageable pageable = PageRequest.of(page, size);
        Page<TableAssignmentProjection> assignments = assignmentService.getAssignmentsByOrder(orderId, pageable);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<TableAssignmentProjection>> getAssignmentsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/assignments/customer/{}", customerId);
        Pageable pageable = PageRequest.of(page, size);
        Page<TableAssignmentProjection> assignments = assignmentService.getAssignmentsByCustomer(customerId, pageable);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<TableAssignmentProjection>> filterAssignments(@RequestBody TableAssignmentFilterRequest request) {
        log.info("POST /api/v1/assignments/filter - {}", request);
        Page<TableAssignmentProjection> assignments = assignmentService.filterAssignments(request);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<TableAssignmentDetailProjection> getAssignmentById(@PathVariable Long assignmentId) {
        log.info("GET /api/v1/assignments/{}", assignmentId);
        TableAssignmentDetailProjection assignment = assignmentService.getAssignmentById(assignmentId);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TableAssignmentProjection>> getActiveAssignments(
            @RequestParam Long restaurantId) {
        log.info("GET /api/v1/assignments/active - restaurantId: {}", restaurantId);
        List<TableAssignmentProjection> assignments = assignmentService.getActiveAssignments(restaurantId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/table/{tableId}/active")
    public ResponseEntity<List<TableAssignmentProjection>> getActiveAssignmentsByTable(
            @PathVariable Long tableId) {
        log.info("GET /api/v1/assignments/table/{}/active", tableId);
        List<TableAssignmentProjection> assignments = assignmentService.getActiveAssignmentsByTable(tableId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<TableAssignmentProjection>> getAssignmentsByTableAndActive(
            @PathVariable Long tableId,
            @RequestParam Boolean active) {
        log.info("GET /api/v1/assignments/table/{} - active: {}", tableId, active);
        List<TableAssignmentProjection> assignments = assignmentService.getAssignmentsByTableAndActive(tableId, active);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/count-by-restaurant-and-active")
    public ResponseEntity<Long> getAssignmentCountByRestaurantAndActive(
            @RequestParam Long restaurantId,
            @RequestParam Boolean active) {
        log.info("GET /api/v1/assignments/count-by-restaurant-and-active - restaurantId: {}, active: {}", restaurantId, active);
        Long count = assignmentService.getAssignmentCountByRestaurantAndActive(restaurantId, active);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-by-table-and-active")
    public ResponseEntity<Long> getAssignmentCountByTableAndActive(
            @RequestParam Long tableId,
            @RequestParam Boolean active) {
        log.info("GET /api/v1/assignments/count-by-table-and-active - tableId: {}, active: {}", tableId, active);
        Long count = assignmentService.getAssignmentCountByTableAndActive(tableId, active);
        return ResponseEntity.ok(count);
    }
}
