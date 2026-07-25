package com.restaurant.table_service.controller;

import com.restaurant.table_service.dto.ApiResponse;
import com.restaurant.table_service.dto.TableAssignmentDetailProjection;
import com.restaurant.table_service.dto.TableAssignmentProjection;
import com.restaurant.table_service.dto.table_assignment.request.TableAssignmentRequest;
import com.restaurant.table_service.request.TableAssignmentFilterRequest;
import com.restaurant.table_service.security.SecurityCheckApisClass;

import com.restaurant.table_service.service.impl.ITableAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
@Validated
public class TableAssignmentController {

    private final SecurityCheckApisClass securityCheckApisClass;
    private final ITableAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<TableAssignmentProjection>> createTableAssignment(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody @Valid TableAssignmentRequest tableAssignmentRequest
    ){
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ApiResponse<TableAssignmentProjection> response = assignmentService.createTableAssignment(tableAssignmentRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/vacate/{assignmentId}/{uid}")
    public ResponseEntity<ApiResponse<TableAssignmentProjection>> vacateTableAssignment(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long assignmentId,
            @PathVariable Long uid
    ){
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ApiResponse<TableAssignmentProjection> response = assignmentService.vacateTableAssignment(assignmentId,uid);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<TableAssignmentProjection>> updateTableAssignment(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody @Valid TableAssignmentRequest tableAssignmentRequest
    ){
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ApiResponse<TableAssignmentProjection> response = assignmentService.updateTableAssignment(tableAssignmentRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<TableAssignmentProjection>>> getAllAssignments(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        if(!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        log.info("GET /api/v1/assignments - restaurantId: {}, page: {}, size: {}", restaurantId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<Page<TableAssignmentProjection>> assignments = assignmentService.getAllAssignments(restaurantId, pageable);
        return new ResponseEntity<>(assignments, assignments.getStatus());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<Page<TableAssignmentProjection>>> getAssignmentsByOrder(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/assignments/order/{}", orderId);
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<Page<TableAssignmentProjection>> assignments = assignmentService.getAssignmentsByOrder(orderId, pageable);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<Page<TableAssignmentProjection>>> getAssignmentsByCustomer(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/assignments/customer/{}", customerId);
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<Page<TableAssignmentProjection>> assignments = assignmentService.getAssignmentsByCustomer(customerId, pageable);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<Page<TableAssignmentProjection>>> filterAssignments(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody TableAssignmentFilterRequest request) {
        log.info("POST /api/v1/assignments/filter - {}", request);
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        ApiResponse<Page<TableAssignmentProjection>> assignments = assignmentService.filterAssignments(request);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<ApiResponse<TableAssignmentDetailProjection>> getAssignmentById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long assignmentId) {
        log.info("GET /api/v1/assignments/{}", assignmentId);
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        ApiResponse<TableAssignmentDetailProjection> assignment = assignmentService.getAssignmentById(assignmentId);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TableAssignmentProjection>>> getActiveAssignments(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId) {
        log.info("GET /api/v1/assignments/active - restaurantId: {}", restaurantId);
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        ApiResponse<List<TableAssignmentProjection>> assignments = assignmentService.getActiveAssignments(restaurantId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/table/{tableId}/active")
    public ResponseEntity<ApiResponse<List<TableAssignmentProjection>>> getActiveAssignmentsByTable(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long tableId) {
        log.info("GET /api/v1/assignments/table/{}/active", tableId);
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        ApiResponse<List<TableAssignmentProjection>> assignments = assignmentService.getActiveAssignmentsByTable(tableId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<ApiResponse<List<TableAssignmentProjection>>> getAssignmentsByTableAndActive(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long tableId,
            @RequestParam Boolean active) {
        log.info("GET /api/v1/assignments/table/{} - active: {}", tableId, active);
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        ApiResponse<List<TableAssignmentProjection>> assignments = assignmentService.getAssignmentsByTableAndActive(tableId, active);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/count-by-restaurant-and-active")
    public ResponseEntity<ApiResponse<Long>> getAssignmentCountByRestaurantAndActive(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam Boolean active) {
        log.info("GET /api/v1/assignments/count-by-restaurant-and-active - restaurantId: {}, active: {}", restaurantId, active);
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        ApiResponse<Long> count = assignmentService.getAssignmentCountByRestaurantAndActive(restaurantId, active);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-by-table-and-active")
    public ResponseEntity<ApiResponse<Long>> getAssignmentCountByTableAndActive(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long tableId,
            @RequestParam Boolean active) {
        log.info("GET /api/v1/assignments/count-by-table-and-active - tableId: {}, active: {}", tableId, active);
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return ResponseEntity.status(403).build();
        }
        ApiResponse<Long> count = assignmentService.getAssignmentCountByTableAndActive(tableId, active);
        return ResponseEntity.ok(count);
    }
}
