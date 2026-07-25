package com.restaurant.user_service.service.restaurant.menu;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuitems.request.MenuItemRequest;
import com.restaurant.user_service.dto.menuitems.request.MenuItemUpdateRequest;
import com.restaurant.user_service.dto.menuitems.response.MenuItemsResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface IMenuItemService {
    ApiResponse<MenuItemsResponse> saveMenuItem(@Valid MenuItemRequest request);

    ApiResponse<List<MenuItemsResponse>> getMenuItemsByCategory(Long categoryId);

    ApiResponse<List<MenuItemsResponse>> getMenuItemsByRestaurant();

    ApiResponse<MenuItemsResponse> getMenuItemsById(Long itemId);

    ApiResponse<MenuItemsResponse> update(@Valid MenuItemUpdateRequest request);

    ApiResponse<Void> deleteMenuItem(@Valid @Min(1) Long itemId);
}
