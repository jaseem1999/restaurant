package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuitems.request.MenuItemRequest;
import com.restaurant.user_service.dto.menuitems.response.MenuItemsResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface IMenuItemService {
    ApiResponse<MenuItemsResponse> saveMenuItem(@Valid MenuItemRequest request);

    ApiResponse<List<MenuItemsResponse>> getMenuItemsByCategory(Long categoryId);

    ApiResponse<List<MenuItemsResponse>> getMenuItemsByRestaurant(@Valid @Min(1) Long restaurantId);
}
