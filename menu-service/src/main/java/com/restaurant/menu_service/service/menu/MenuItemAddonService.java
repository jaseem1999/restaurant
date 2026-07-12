package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuItemAddon;
import com.restaurant.menu_service.projection.addon.MenuAddonProjection;

import java.util.List;

public interface MenuItemAddonService {
    MenuItemAddon create(MenuItemAddon addon);
    MenuAddonProjection getById(Long id);
    List<MenuAddonProjection> listByMenuItem(Long menuItemId);
    MenuItemAddon update(Long id, MenuItemAddon addon);
    String delete(Long rid,Long id);
}

