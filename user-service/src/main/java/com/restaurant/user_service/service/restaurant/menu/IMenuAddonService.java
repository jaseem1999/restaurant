package com.restaurant.user_service.service.restaurant.menu;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuaddon.request.MenuItemAddonRequest;
import com.restaurant.user_service.dto.menuaddon.request.MenuItemAddonUpdateRequest;
import com.restaurant.user_service.dto.menuaddon.response.MenuItemAddonResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface IMenuAddonService {
    ApiResponse<MenuItemAddonResponse> add(MenuItemAddonRequest request);

    ApiResponse<List<MenuItemAddonResponse>> getByItemId(@Min(1) @Valid Long itemId);

    ApiResponse<MenuItemAddonResponse> getById(@Min(1) @Valid Long id);

    ApiResponse<MenuItemAddonResponse> update(@Valid MenuItemAddonUpdateRequest request);

    ApiResponse<Void> delete(Long id);
}
