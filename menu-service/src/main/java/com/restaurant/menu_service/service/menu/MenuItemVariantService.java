package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuItemVariant;

import java.util.List;

public interface MenuItemVariantService {
    MenuItemVariant create(MenuItemVariant variant);
    MenuItemVariant getById(Long id);
    List<MenuItemVariant> listByMenuItem(Long menuItemId);
    MenuItemVariant update(Long id, MenuItemVariant variant);
    void delete(Long id);
}

