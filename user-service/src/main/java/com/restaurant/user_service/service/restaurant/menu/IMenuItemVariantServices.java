package com.restaurant.user_service.service.restaurant.menu;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuvariant.request.MenuItemVariantRequest;
import com.restaurant.user_service.dto.menuvariant.request.MenuItemVariantUpdateRequest;
import com.restaurant.user_service.dto.menuvariant.response.MenuItemVariantResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IMenuItemVariantServices {
    ApiResponse<MenuItemVariantResponse> createMenuItemVariant(@Valid MenuItemVariantRequest request);

    ApiResponse<MenuItemVariantResponse> getMenuItemVariantById(Long id);

    ApiResponse<MenuItemVariantResponse> updateMenuItemVariant(Long id, MenuItemVariantUpdateRequest request);

    ApiResponse<List<MenuItemVariantResponse>> getMenuItemVariantByItemId(Long menuItemId);

    ApiResponse<String> deleteMenuItemVariantById(Long id);
}
