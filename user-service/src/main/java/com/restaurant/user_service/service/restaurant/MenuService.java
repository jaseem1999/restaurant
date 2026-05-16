package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.client.MenuClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menu.request.MenuCategoryRequest;
import com.restaurant.user_service.dto.menu.response.MenuCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService implements IMenuCategoryService{

    private final MenuClient menuClient;

    @Override
    public ApiResponse<MenuCategoryResponse> createMenuCategory(MenuCategoryRequest request) {
        return menuClient.createMenuCategory(request);
    }

    @Override
    public ApiResponse<List<MenuCategoryResponse>> getMenuCategoriesByRestaurantId(Long restaurantId) {
        return menuClient.getMenuCategoriesByRestaurantId(restaurantId);
    }
}
