package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuItemImage;
import com.restaurant.menu_service.projection.images.ItemImagesProjection;

import java.util.List;

public interface MenuItemImageService {
    MenuItemImage create(MenuItemImage image);
    ItemImagesProjection getById(Long id);
    List<ItemImagesProjection> listByMenuItem(Long menuItemId);
    MenuItemImage update(Long id, MenuItemImage image);
    String delete(Long id);
}

