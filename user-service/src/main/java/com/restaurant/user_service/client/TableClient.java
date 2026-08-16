package com.restaurant.user_service.client;


import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.table.request.TableRequest;
import com.restaurant.user_service.dto.table.response.TableResponse;
import com.restaurant.user_service.dto.tableassignment.request.TableAssignmentRequest;
import com.restaurant.user_service.dto.tableassignment.response.TableAssignmentResponse;
import com.restaurant.user_service.security.TableFeignConfigSecurity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "table-service", configuration = TableFeignConfigSecurity.class)
public interface TableClient {

    @PostMapping("/api/v1/tables/create")
    ApiResponse<TableResponse> createTable(@RequestBody TableRequest tableRequest);

    @PutMapping("/api/v1/tables/update")
    ApiResponse<TableResponse> updateTable(@RequestBody TableRequest tableRequest);

    @GetMapping("/api/v1/tables")
    ApiResponse<Page<TableResponse>> getTables( @RequestParam Long restaurantId,
                                                @RequestParam Integer page,
                                                @RequestParam Integer size);

    @PostMapping("/api/v1/assignments")
    ApiResponse<TableAssignmentResponse> createTableAssignment(
            @RequestBody TableAssignmentRequest request);
}
