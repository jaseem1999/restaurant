package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuItemImage;

import java.util.List;

public interface MenuItemImageService {
    MenuItemImage create(MenuItemImage image);
    MenuItemImage getById(Long id);
    List<MenuItemImage> listByMenuItem(Long menuItemId);
    MenuItemImage update(Long id, MenuItemImage image);
    void delete(Long id);
}

