package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuItem;
import com.restaurant.menu_service.projection.menu.response.MenuItemProjection;

import java.util.List;

public interface MenuItemService {
    MenuItem create(MenuItem item);
    MenuItemProjection getById(Long id);
    List<MenuItemProjection> listByRestaurant(Long restaurantId);
    List<MenuItemProjection> listByCategory(Long categoryId);
    MenuItem update(Long id, MenuItem item);
    String delete(Long id, Long restaurantId);
}

