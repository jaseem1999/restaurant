package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuItemVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemVariantRepository extends JpaRepository<MenuItemVariant, Long> {

    List<MenuItemVariant> findByMenuItemId(Long menuItemId);

    List<MenuItemVariant> findByMenuItemIdAndAvailableTrue(Long menuItemId);

    boolean existsByVariantNameAndMenuItemId(String variantName, Long menuItemId);
}

