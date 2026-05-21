package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuItemAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemAddonRepository extends JpaRepository<MenuItemAddon, Long> {
    
    List<MenuItemAddon> findByMenuItemId(Long menuItemId);
    
    List<MenuItemAddon> findByMenuItemIdAndAvailableTrue(Long menuItemId);
    
    boolean existsByAddonNameAndMenuItemId(String addonName, Long menuItemId);
}

