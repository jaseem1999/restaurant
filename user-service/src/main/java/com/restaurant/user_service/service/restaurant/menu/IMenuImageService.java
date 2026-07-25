package com.restaurant.user_service.service.restaurant.menu;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuimage.request.MenuItemImageRequest;
import com.restaurant.user_service.dto.menuimage.request.MenuItemImageUpdateRequest;
import com.restaurant.user_service.dto.menuimage.response.MenuItemImageResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IMenuImageService {
    ApiResponse<MenuItemImageResponse> createItemImage(@Valid MenuItemImageRequest menuItemImageRequest);

    ApiResponse<List<MenuItemImageResponse>> listByMenuItem(@Valid Long menuItemId);

    ApiResponse<MenuItemImageResponse> updateItemImage(@Valid MenuItemImageUpdateRequest request);

    ApiResponse<MenuItemImageResponse> getItemImageById(@Valid Long id);

    ApiResponse<String> deleteItemImage(@Valid Long id);
}
