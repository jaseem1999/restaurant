package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuItemAddon;
import com.restaurant.menu_service.projection.addon.MenuAddonProjection;
import com.restaurant.menu_service.projection.items.MenuItemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemAddonRepository extends JpaRepository<MenuItemAddon, Long> {

    @Query("""
    SELECT
        a.id AS addonId,
        a.addonName AS addonName,
        a.price AS price,
        a.additionalPreparationTime AS additionalPreparationTime,
        a.additionalCalories AS additionalCalories,
        a.available AS available,
        a.menuItem.id AS menuItemId,
        a.createdBy AS createdBy,
        a.updatedBy AS updatedBy
    FROM MenuItemAddon a
    WHERE a.menuItem.id = :menuItemId
    ORDER BY a.id ASC
    """)
    List<MenuAddonProjection> findByMenuItemId(@Param("menuItemId") Long menuItemId);
    
    List<MenuItemAddon> findByMenuItemIdAndAvailableTrue(Long menuItemId);
    
    boolean existsByAddonNameAndMenuItemId(String addonName, Long menuItemId);

    @Query("""
    SELECT
        a.id AS addonId,
        a.addonName AS addonName,
        a.price AS price,
        a.additionalPreparationTime AS additionalPreparationTime,
        a.additionalCalories AS additionalCalories,
        a.available AS available,
        a.menuItem.id AS menuItemId,
        a.createdBy AS createdBy,
        a.updatedBy AS updatedBy
    FROM MenuItemAddon a
    WHERE a.id = :id
    """)
    Optional<MenuAddonProjection> findByIdProjection(@Param("id") Long id);
}

