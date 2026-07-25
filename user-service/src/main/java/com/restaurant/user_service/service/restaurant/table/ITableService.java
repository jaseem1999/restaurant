package com.restaurant.user_service.service.restaurant.table;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.table.request.TableRequest;
import com.restaurant.user_service.dto.table.response.TableResponse;
import jakarta.validation.Valid;

public interface ITableService {
    ApiResponse<TableResponse> createTable(@Valid TableRequest tableRequest);
}
