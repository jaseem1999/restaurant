package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuItemVariant;
import com.restaurant.menu_service.projection.variant.VariantProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemVariantRepository extends JpaRepository<MenuItemVariant, Long> {

    List<MenuItemVariant> findByMenuItemId(Long menuItemId);

    List<MenuItemVariant> findByMenuItemIdAndAvailableTrue(Long menuItemId);

    boolean existsByVariantNameAndMenuItemId(String variantName, Long menuItemId);

    @Query("SELECT v.id AS variantId, v.variantName AS variantName, v.priceAdjustment AS priceAdjustment, " +
            "v.additionalPreparationTime AS additionalPreparationTime, v.additionalCalories AS additionalCalories, " +
            "v.available AS available, v.menuItem.id AS menuItemId, v.createdBy AS createdBy, v.updatedBy AS updatedBy " +
            "FROM MenuItemVariant v WHERE v.id = :id")
    Optional<VariantProjection> findByIdProjection(Long id);

    @Query("SELECT v.id AS variantId, v.variantName AS variantName, v.priceAdjustment AS priceAdjustment, " +
            "v.additionalPreparationTime AS additionalPreparationTime, v.additionalCalories AS additionalCalories, " +
            "v.available AS available, v.menuItem.id AS menuItemId, v.createdBy AS createdBy, v.updatedBy AS updatedBy " +
            "FROM MenuItemVariant v WHERE v.menuItem.id = :menuItemId")
    List<VariantProjection> findByMenuItemIdProjection(Long menuItemId);
}

