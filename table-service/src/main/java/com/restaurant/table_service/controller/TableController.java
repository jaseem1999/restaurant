package com.restaurant.table_service.controller;

import com.restaurant.table_service.dto.TableDetailProjection;
import com.restaurant.table_service.dto.TableProjection;
import com.restaurant.table_service.dto.ApiResponse;
import com.restaurant.table_service.dto.table.request.TableRequest;
import com.restaurant.table_service.dto.table.response.TableResponse;
import com.restaurant.table_service.entity.table.Table;
import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.entity.table.enums.TableType;
import com.restaurant.table_service.request.TableFilterRequest;
import com.restaurant.table_service.security.SecurityCheckApisClass;
import com.restaurant.table_service.service.impl.ITableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class TableController {

    private final SecurityCheckApisClass securityCheckApisClass;

    private final ITableService iTableService;

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<TableResponse>> createTable(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody TableRequest tableRequest) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        ApiResponse<TableResponse> response = iTableService.createTable(fromDto(tableRequest));
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping(path = "/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<TableResponse>> updateTable(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody TableRequest tableRequest) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        ApiResponse<TableResponse> response = iTableService.updateTable(tableRequest.getId(),fromDto(tableRequest));
        return new ResponseEntity<>(response, response.getStatus());
    }

    private Table fromDto(TableRequest tableRequest) {
        Table table =  Table.builder()
                .tableNumber(tableRequest.getTableNumber())
                .capacity(tableRequest.getCapacity())
                .status(tableRequest.getTableStatus())
                .tableType(tableRequest.getTableType())
                .restaurantId(tableRequest.getRestaurantId())
                .location(tableRequest.getLocation())
                .floor(tableRequest.getFloor())
                .section(tableRequest.getSection())
                .active(tableRequest.getActive())
                .notes(tableRequest.getNotes()).build();
        if (tableRequest.getCreatedBy() != null) {
            table.setCreatedBy(tableRequest.getCreatedBy());
            table.setCreatedAt(Instant.now());
        }

        if (tableRequest.getUpdatedBy() != null) {
            table.setUpdatedBy(tableRequest.getUpdatedBy());
            table.setUpdatedAt(Instant.now());
        }
        return table;
    }

    private TableResponse fromProjectionToDto(TableProjection projection) {
        return TableResponse.builder()
                .id(projection.getId())
                .tableNumber(projection.getTableNumber())
                .capacity(projection.getCapacity())
                .tableStatus(projection.getStatus())
                .tableType(projection.getTableType())
                .location(projection.getLocation())
                .floor(projection.getFloor())
                .section(projection.getSection())
                .active(projection.getActive())
                .createdBy(projection.getCreatedBy())
                .createdAt(projection.getCreatedAt())
                .updatedBy(projection.getUpdatedBy())
                .updatedAt(projection.getUpdatedAt())
                .restaurantId(projection.getRestaurantId())
                .notes(projection.getNotes())
                .build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TableResponse>>> getAllTables(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        log.info("GET /api/v1/tables - restaurantId: {}, page: {}, size: {}", restaurantId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<TableProjection> tables = iTableService.getAllTables(restaurantId, pageable);
        Page<TableResponse> tableResponses = tables.map(this::fromProjectionToDto);
        return new ResponseEntity<>(new ApiResponse<>(tableResponses, true, "Tables retrieved successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<TableResponse>>> getTablesByStatus(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @PathVariable TableStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        log.info("GET /api/v1/tables/status/{} - restaurantId: {}", status, restaurantId);
        Pageable pageable = PageRequest.of(page, size);
        Page<TableProjection> tables = iTableService.getTablesByStatus(restaurantId, status, pageable);
        Page<TableResponse> tableResponses = tables.map(this::fromProjectionToDto);
        return new ResponseEntity<>(new ApiResponse<>(tableResponses, true, "Tables retrieved successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<Page<TableResponse>>> filterTables(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody TableFilterRequest request) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        log.info("POST /api/v1/tables/filter - {}", request);
        Page<TableProjection> tables = iTableService.filterTables(request);
        Page<TableResponse> tableResponses = tables.map(this::fromProjectionToDto);
        return new ResponseEntity<>(new ApiResponse<>(tableResponses, true, "Tables retrieved successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<ApiResponse<TableResponse>> getTableById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long tableId) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        log.info("GET /api/v1/tables/{}", tableId);
        TableDetailProjection table = iTableService.getTableById(tableId);
        TableResponse tableResponse = fromProjectionToDetailDto(table);
        return new ResponseEntity<>(new ApiResponse<>(tableResponse, true, "Table retrieved successfully", HttpStatus.OK), HttpStatus.OK);
    }

    private TableResponse fromProjectionToDetailDto(TableDetailProjection table) {
        // Implementation for converting TableDetailProjection to TableResponse
        return TableResponse.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .tableStatus(table.getStatus())
                .tableType(table.getTableType())
                .location(table.getLocation())
                .floor(table.getFloor())
                .section(table.getSection())
                .active(table.getActive())
                .notes(table.getNotes())
                .createdAt(table.getCreatedAt())
                .updatedAt(table.getUpdatedAt())
                .build();
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<TableProjection>>> getAvailableTablesForCapacity(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam Integer guestCount) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        log.info("GET /api/v1/tables/available - restaurantId: {}, guestCount: {}", restaurantId, guestCount);
        List<TableProjection> tables = iTableService.getAvailableTablesForCapacity(restaurantId, guestCount);
        return new ResponseEntity<>(new ApiResponse<>(tables, true, "Available tables retrieved successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/floor/{floor}")
    public ResponseEntity<ApiResponse<List<TableProjection>>> getTablesByFloor(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @PathVariable String floor) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        log.info("GET /api/v1/tables/floor/{} - restaurantId: {}", floor, restaurantId);
        List<TableProjection> tables = iTableService.getTablesByFloor(restaurantId, floor);
        return new ResponseEntity<>(new ApiResponse<>(tables, true, "Tables retrieved successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/section/{section}")
    public ResponseEntity<ApiResponse<List<TableProjection>>> getTablesBySection(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @PathVariable String section) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        log.info("GET /api/v1/tables/section/{} - restaurantId: {}", section, restaurantId);
        List<TableProjection> tables = iTableService.getTablesBySection(restaurantId, section);
        return new ResponseEntity<>(new ApiResponse<>(tables, true, "Tables retrieved successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/count-by-status")
    public ResponseEntity<ApiResponse<Long>> getTableCountByStatus(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long restaurantId,
            @RequestParam TableStatus status) {
        if (!securityCheckApisClass.checkApi(authorizationHeader)) {
            return new ResponseEntity<>(new ApiResponse<>(null, false, "Invalid API credentials", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        log.info("GET /api/v1/tables/count-by-status - restaurantId: {}, status: {}", restaurantId, status);
        Long count = iTableService.getTableCountByStatus(restaurantId, status);
        return new ResponseEntity<>(new ApiResponse<>(count, true, "Table count retrieved successfully", HttpStatus.OK), HttpStatus.OK);
    }
}
