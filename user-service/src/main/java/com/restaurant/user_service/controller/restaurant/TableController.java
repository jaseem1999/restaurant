package com.restaurant.user_service.controller.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.table.request.TableRequest;
import com.restaurant.user_service.dto.table.response.TableResponse;
import com.restaurant.user_service.service.restaurant.table.ITableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant/table")
@Validated
@RequiredArgsConstructor
public class TableController {
    private final ITableService iTableService;

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ApiResponse<TableResponse>> createTable(@Valid @RequestBody TableRequest tableRequest) {
        ApiResponse<TableResponse> tableResponse = iTableService.createTable(tableRequest);
        return new ResponseEntity<>(tableResponse, tableResponse.getStatus());
    }

    @PutMapping(path = "/update", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ApiResponse<TableResponse>> updateTable(@Valid @RequestBody TableRequest tableRequest) {
        ApiResponse<TableResponse> tableResponse = iTableService.updateTable(tableRequest);
        return new ResponseEntity<>(tableResponse, tableResponse.getStatus());
    }
}
