package com.restaurant.user_service.service.restaurant.table;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.table.request.TableRequest;
import com.restaurant.user_service.dto.table.response.TableResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface ITableService {
    ApiResponse<TableResponse> createTable(@Valid TableRequest tableRequest);

    ApiResponse<TableResponse> updateTable(@Valid TableRequest tableRequest);

    ApiResponse<Page<TableResponse>> getTables( Integer page, Integer size);
}
