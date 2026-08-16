package com.restaurant.table_service.client;

import com.restaurant.table_service.security.TableFeignConfigSecurity;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service", configuration = TableFeignConfigSecurity.class)
public interface OrderClient {
    default boolean checkOrderExists(Long orderId){
        return true;
    }
}
