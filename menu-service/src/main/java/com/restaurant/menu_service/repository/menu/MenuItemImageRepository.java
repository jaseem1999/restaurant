package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemImageRepository extends JpaRepository<MenuItemImage, Long> {
    
    List<MenuItemImage> findByMenuItemId(Long menuItemId);
    
    List<MenuItemImage> findByMenuItemIdAndActiveTrue(Long menuItemId);
    
    List<MenuItemImage> findByMenuItemIdOrderByDisplayOrder(Long menuItemId);
}

