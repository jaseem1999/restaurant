package com.restaurant.user_service.client;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menu.request.MenuCategoryRequest;
import com.restaurant.user_service.dto.menu.request.MenuCategoryUpdateRequest;
import com.restaurant.user_service.dto.menu.response.MenuCategoryResponse;
import com.restaurant.user_service.dto.menuaddon.request.MenuItemAddonRequest;
import com.restaurant.user_service.dto.menuaddon.response.MenuItemAddonResponse;
import com.restaurant.user_service.dto.menuitems.request.MenuItemRequest;
import com.restaurant.user_service.dto.menuitems.request.MenuItemUpdateRequest;
import com.restaurant.user_service.dto.menuitems.response.MenuItemsResponse;
import com.restaurant.user_service.security.MenuFeignConfigSecurity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "menu-service", configuration = MenuFeignConfigSecurity.class)
public interface MenuClient {

    @PostMapping("/api/menu/categories")
    ApiResponse<MenuCategoryResponse> createMenuCategory(MenuCategoryRequest request);

    @GetMapping("/api/menu/categories/by-restaurant")
    ApiResponse<List<MenuCategoryResponse>> getMenuCategoriesByRestaurantId(@RequestParam(required = false) Long restaurantId);

    @GetMapping("/api/menu/categories/{id}")
    ApiResponse<MenuCategoryResponse> getMenuCategoryById(@PathVariable("id") Long categoryId);

    @PutMapping("/api/menu/categories/{id}")
    ApiResponse<MenuCategoryResponse> updateMenuCategory(
            @PathVariable Long id, @RequestBody MenuCategoryUpdateRequest request);

    @DeleteMapping("/api/menu/categories/{id}")
    ApiResponse<Void> deleteMenuCategory(@PathVariable Long id);


    @PostMapping("/api/menu/items")
    ApiResponse<MenuItemsResponse> saveMenuItem(@RequestBody MenuItemRequest request);

    @GetMapping("/api/menu/items")
    ApiResponse<List<MenuItemsResponse>> getMenuItemsByCategory(@RequestParam Long categoryId);

    @GetMapping("/api/menu/items")
    ApiResponse<List<MenuItemsResponse>> getMenuItemsByRestaurant(@RequestParam Long restaurantId);

    @GetMapping("/api/menu/items/{id}")
    ApiResponse<MenuItemsResponse> getMenuItemsById(@PathVariable Long id);

    @PutMapping("/api/menu/items/{id}")
    ApiResponse<MenuItemsResponse> updateMenuItem(
            @PathVariable Long id, @RequestBody MenuItemUpdateRequest request);

    @DeleteMapping("/api/menu/items/{id}/restaurant/{restaurantId}")
    ApiResponse<Void> deleteMenuItem(@PathVariable Long id, @PathVariable Long restaurantId);

    @PostMapping("/api/menu/addons")
    ApiResponse<MenuItemAddonResponse> addAddon(@RequestBody MenuItemAddonRequest request);

    @GetMapping("/api/menu/addons")
    ApiResponse<List<MenuItemAddonResponse>> addOnGetByItemId(@RequestParam Long menuItemId);

    @GetMapping("/api/menu/addons/{id}")
    ApiResponse<MenuItemAddonResponse> addOnGetById(@PathVariable Long id);
}
