package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuItem;
import com.restaurant.menu_service.projection.menu.response.MenuItemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByRestaurantId(Long restaurantId);
    
    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);
    
    List<MenuItem> findByRestaurantIdAndFeaturedTrue(Long restaurantId);
    
    List<MenuItem> findByCategoryId(Long categoryId);
    
    List<MenuItem> findByCategoryIdAndAvailableTrue(Long categoryId);
    
    boolean existsByItemNameAndRestaurantId(String itemName, Long restaurantId);

    @Query(value = "SELECT m.id as id, m.item_name as itemName, m.description, m.item_type as itemType, m.food_type as foodType, m.base_price as basePrice, m.preparation_time as preparationTime, m.calories, m.image, m.available, m.featured, m.tax_percentage as taxPercentage, m.category_id as categoryId, m.restaurant_id as restaurantId, m.created_by as createdBy, m.updated_by as updatedBy FROM menu_item m WHERE m.id = :id", nativeQuery = true)
    Optional<MenuItemProjection> findByIdProjection(@Param("id") Long id);

    @Query(value = "SELECT m.id as id, m.item_name as itemName, m.description, m.item_type as itemType, m.food_type as foodType, m.base_price as basePrice, m.preparation_time as preparationTime, m.calories, m.image, m.available, m.featured, m.tax_percentage as taxPercentage, m.category_id as categoryId, m.restaurant_id as restaurantId, m.created_by as createdBy, m.updated_by as updatedBy FROM menu_item m WHERE m.restaurant_id = :restaurantId", nativeQuery = true)
    List<MenuItemProjection> findByRestaurantIdProjection(@Param("restaurantId") Long restaurantId);

    @Query(value = "SELECT m.id as id, m.item_name as itemName, m.description, m.item_type as itemType, m.food_type as foodType, m.base_price as basePrice, m.preparation_time as preparationTime, m.calories, m.image, m.available, m.featured, m.tax_percentage as taxPercentage, m.category_id as categoryId, m.restaurant_id as restaurantId, m.created_by as createdBy, m.updated_by as updatedBy FROM menu_item m WHERE m.category_id = :categoryId", nativeQuery = true)
    List<MenuItemProjection> findByCategoryIdProjection(@Param("categoryId") Long categoryId);

    boolean existsByIdAndRestaurantId(Long id, Long restaurantId);
}


