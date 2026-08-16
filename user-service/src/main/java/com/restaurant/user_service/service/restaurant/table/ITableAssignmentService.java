package com.restaurant.user_service.service.restaurant.table;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.tableassignment.request.TableAssignmentRequest;
import com.restaurant.user_service.dto.tableassignment.response.TableAssignmentResponse;
import jakarta.validation.Valid;

public interface ITableAssignmentService {
    ApiResponse<TableAssignmentResponse> createTableAssignment(@Valid TableAssignmentRequest request);
}
