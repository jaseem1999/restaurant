package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuItemVariant;
import com.restaurant.menu_service.projection.variant.VariantProjection;

import java.util.List;

public interface MenuItemVariantService {
    MenuItemVariant create(MenuItemVariant variant);
    VariantProjection getById(Long id);
    List<VariantProjection> listByMenuItem(Long menuItemId);
    MenuItemVariant update(Long id, MenuItemVariant variant);
    String delete(Long id);
}

