package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuItemImage;
import com.restaurant.menu_service.projection.images.ItemImagesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemImageRepository extends JpaRepository<MenuItemImage, Long> {
    
    List<MenuItemImage> findByMenuItemId(Long menuItemId);
    
    List<MenuItemImage> findByMenuItemIdAndActiveTrue(Long menuItemId);

    @Query("SELECT i.id AS id, i.menuItem.id AS menuItemId, i.imageUrl AS imageUrl, " +
            "i.altText AS altText, i.displayOrder AS displayOrder, i.active AS active, " +
            "i.createdBy AS createdBy, i.updatedBy AS updatedBy " +
            "FROM MenuItemImage i WHERE i.menuItem.id = :id")
    List<ItemImagesProjection> findByMenuItemIdOrderByDisplayOrder(@Param("id")Long menuItemId);

    @Query("SELECT i.id AS id, i.menuItem.id AS menuItemId, i.imageUrl AS imageUrl, " +
           "i.altText AS altText, i.displayOrder AS displayOrder, i.active AS active, " +
           "i.createdBy AS createdBy, i.updatedBy AS updatedBy " +
           "FROM MenuItemImage i WHERE i.id = :id")
    Optional<ItemImagesProjection> findByIdItemImagesProjection( @Param("id") Long id);
}

