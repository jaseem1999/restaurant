package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuCategory;

import java.util.List;

public interface MenuCategoryService {
    MenuCategory create(MenuCategory category);
    MenuCategory getById(Long id);
    List<MenuCategory> listByRestaurant(Long restaurantId);
    MenuCategory update(Long id, MenuCategory category);
    void delete(Long id);
}

