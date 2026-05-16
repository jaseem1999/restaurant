package com.restaurant.user_service.client;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menu.request.MenuCategoryRequest;
import com.restaurant.user_service.dto.menu.response.MenuCategoryResponse;
import com.restaurant.user_service.security.MenuFeignConfigSecurity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "menu-service", configuration = MenuFeignConfigSecurity.class)
public interface MenuClient {

    @PostMapping("/api/menu/categories")
    ApiResponse<MenuCategoryResponse> createMenuCategory(MenuCategoryRequest request);
}
