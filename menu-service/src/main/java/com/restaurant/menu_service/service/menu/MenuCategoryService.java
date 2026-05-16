package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuCategory;
import com.restaurant.menu_service.projection.menu.response.MenuCategoryProjection;

import java.util.List;

public interface MenuCategoryService {
    MenuCategory create(MenuCategory category);
    MenuCategoryProjection getById(Long id);
    List<MenuCategoryProjection> listByRestaurant(Long restaurantId);
    MenuCategory update(Long id, MenuCategory category);
    void delete(Long id);
}

