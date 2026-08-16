package com.restaurant.user_service.controller.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.tableassignment.request.TableAssignmentRequest;
import com.restaurant.user_service.dto.tableassignment.response.TableAssignmentResponse;
import com.restaurant.user_service.service.restaurant.table.ITableAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("restaurant/api/v1/table-assignment")
@Validated
@RequiredArgsConstructor
public class TableAssignmentController {
    private final ITableAssignmentService iTableAssignmentService;

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ApiResponse<TableAssignmentResponse>> createTableAssignment(
            @Valid @RequestBody TableAssignmentRequest request
    ) {
        ApiResponse<TableAssignmentResponse> response = iTableAssignmentService.createTableAssignment(request);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
