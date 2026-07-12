package com.restaurant.user_service.client;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menu.request.MenuCategoryRequest;
import com.restaurant.user_service.dto.menu.request.MenuCategoryUpdateRequest;
import com.restaurant.user_service.dto.menu.response.MenuCategoryResponse;
import com.restaurant.user_service.dto.menuaddon.request.MenuItemAddonRequest;
import com.restaurant.user_service.dto.menuaddon.request.MenuItemAddonUpdateRequest;
import com.restaurant.user_service.dto.menuaddon.response.MenuItemAddonResponse;
import com.restaurant.user_service.dto.menuimage.request.MenuItemImageRequest;
import com.restaurant.user_service.dto.menuimage.request.MenuItemImageUpdateRequest;
import com.restaurant.user_service.dto.menuimage.response.MenuItemImageResponse;
import com.restaurant.user_service.dto.menuitems.request.MenuItemRequest;
import com.restaurant.user_service.dto.menuitems.request.MenuItemUpdateRequest;
import com.restaurant.user_service.dto.menuitems.response.MenuItemsResponse;
import com.restaurant.user_service.dto.menuvariant.request.MenuItemVariantRequest;
import com.restaurant.user_service.dto.menuvariant.request.MenuItemVariantUpdateRequest;
import com.restaurant.user_service.dto.menuvariant.response.MenuItemVariantResponse;
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

    @PutMapping("/api/menu/addons/{id}")
    ApiResponse<MenuItemAddonResponse> addOnUpdate( @PathVariable Long id,@RequestBody MenuItemAddonUpdateRequest request);

    @DeleteMapping("/api/menu/addons/{restaurantId}/{id}")
    ApiResponse<Void> addOnDelete(@PathVariable Long restaurantId,@PathVariable Long id);

    @PostMapping("/api/menu/images")
    ApiResponse<MenuItemImageResponse> createMenuItemImage(@RequestBody  MenuItemImageRequest menuItemImageRequest);

    @GetMapping("/api/menu/images")
    ApiResponse<List<MenuItemImageResponse>> listMenuItemImages(@RequestParam Long menuItemId);

    @PutMapping("/api/menu/images/{id}")
    ApiResponse<MenuItemImageResponse> updateMenuItemImage(
            @PathVariable("id") Long id,
            @RequestBody MenuItemImageUpdateRequest request);

    @GetMapping("/api/menu/images/{id}")
    ApiResponse<MenuItemImageResponse> getMenuItemImageById(@PathVariable Long id);

    @DeleteMapping("/api/menu/images/{id}")
    ApiResponse<String> deleteMenuItemImage(@PathVariable Long id);

    @PostMapping("/api/menu/variants")
    ApiResponse<MenuItemVariantResponse> createMenuItemVariant(@RequestBody MenuItemVariantRequest request);

    @GetMapping("/api/menu/variants/{id}")
    ApiResponse<MenuItemVariantResponse> getMenuItemVariantById( @PathVariable Long id);

    @PutMapping("/api/menu/variants/{id}")
    ApiResponse<MenuItemVariantResponse> updateMenuItemVariant(
            @PathVariable Long id,
            @RequestBody MenuItemVariantUpdateRequest request);

    @GetMapping("/api/menu/variants")
    ApiResponse<List<MenuItemVariantResponse>> getMenuItemVariantsByMenuItemId(@RequestParam Long menuItemId);

    @DeleteMapping("/api/menu/variants/{id}")
    ApiResponse<String> deleteMenuItemVariantById(@PathVariable Long id);
}
