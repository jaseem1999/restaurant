package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menu.request.MenuCategoryRequest;
import com.restaurant.user_service.dto.menu.response.MenuCategoryResponse;

public interface IMenuCategoryService {
    ApiResponse<MenuCategoryResponse> createMenuCategory(MenuCategoryRequest request);
}
