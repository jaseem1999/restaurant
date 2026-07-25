package com.restaurant.user_service.client;


import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.table.request.TableRequest;
import com.restaurant.user_service.dto.table.response.TableResponse;
import com.restaurant.user_service.security.TableFeignConfigSecurity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "table-service", configuration = TableFeignConfigSecurity.class)
public interface TableClient {

    @PostMapping("/api/v1/tables/create")
    ApiResponse<TableResponse> createTable(@RequestBody TableRequest tableRequest);
}
