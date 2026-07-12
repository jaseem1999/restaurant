package com.restaurant.table_service.controller;

import com.restaurant.table_service.dto.TableDetailProjection;
import com.restaurant.table_service.dto.TableProjection;
import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.request.TableFilterRequest;
import com.restaurant.table_service.service.TableService;
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
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    public ResponseEntity<Page<TableProjection>> getAllTables(
            @RequestParam Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/tables - restaurantId: {}, page: {}, size: {}", restaurantId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<TableProjection> tables = tableService.getAllTables(restaurantId, pageable);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TableProjection>> getTablesByStatus(
            @RequestParam Long restaurantId,
            @PathVariable TableStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET /api/v1/tables/status/{} - restaurantId: {}", status, restaurantId);
        Pageable pageable = PageRequest.of(page, size);
        Page<TableProjection> tables = tableService.getTablesByStatus(restaurantId, status, pageable);
        return ResponseEntity.ok(tables);
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<TableProjection>> filterTables(@RequestBody TableFilterRequest request) {
        log.info("POST /api/v1/tables/filter - {}", request);
        Page<TableProjection> tables = tableService.filterTables(request);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<TableDetailProjection> getTableById(@PathVariable Long tableId) {
        log.info("GET /api/v1/tables/{}", tableId);
        TableDetailProjection table = tableService.getTableById(tableId);
        return ResponseEntity.ok(table);
    }

    @GetMapping("/available")
    public ResponseEntity<List<TableProjection>> getAvailableTablesForCapacity(
            @RequestParam Long restaurantId,
            @RequestParam Integer guestCount) {
        log.info("GET /api/v1/tables/available - restaurantId: {}, guestCount: {}", restaurantId, guestCount);
        List<TableProjection> tables = tableService.getAvailableTablesForCapacity(restaurantId, guestCount);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<TableProjection>> getTablesByFloor(
            @RequestParam Long restaurantId,
            @PathVariable String floor) {
        log.info("GET /api/v1/tables/floor/{} - restaurantId: {}", floor, restaurantId);
        List<TableProjection> tables = tableService.getTablesByFloor(restaurantId, floor);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/section/{section}")
    public ResponseEntity<List<TableProjection>> getTablesBySection(
            @RequestParam Long restaurantId,
            @PathVariable String section) {
        log.info("GET /api/v1/tables/section/{} - restaurantId: {}", section, restaurantId);
        List<TableProjection> tables = tableService.getTablesBySection(restaurantId, section);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/count-by-status")
    public ResponseEntity<Long> getTableCountByStatus(
            @RequestParam Long restaurantId,
            @RequestParam TableStatus status) {
        log.info("GET /api/v1/tables/count-by-status - restaurantId: {}, status: {}", restaurantId, status);
        Long count = tableService.getTableCountByStatus(restaurantId, status);
        return ResponseEntity.ok(count);
    }
}
