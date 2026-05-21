package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuItemAddon;

import java.util.List;

public interface MenuItemAddonService {
    MenuItemAddon create(MenuItemAddon addon);
    MenuItemAddon getById(Long id);
    List<MenuItemAddon> listByMenuItem(Long menuItemId);
    MenuItemAddon update(Long id, MenuItemAddon addon);
    void delete(Long id);
}

