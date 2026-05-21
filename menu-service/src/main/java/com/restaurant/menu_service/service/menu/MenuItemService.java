package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuItem;

import java.util.List;

public interface MenuItemService {
    MenuItem create(MenuItem item);
    MenuItem getById(Long id);
    List<MenuItem> listByRestaurant(Long restaurantId);
    List<MenuItem> listByCategory(Long categoryId);
    MenuItem update(Long id, MenuItem item);
    void delete(Long id);
}

