package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuCategory;
import com.restaurant.menu_service.projection.menu.response.MenuCategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    
    List<MenuCategoryProjection> findByRestaurantId(Long restaurantId);
    
    List<MenuCategoryProjection> findByRestaurantIdAndActiveTrue(Long restaurantId);
    
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);

    @Query("SELECT c.id AS id, c.name AS name, c.description AS description, " +
           "c.image AS image, c.displayOrder AS displayOrder, c.active AS active, " +
           "c.restaurantId AS restaurantId, c.createdBy AS createdBy, c.updatedBy AS updatedBy " +
           "FROM MenuCategory c WHERE c.id = :id")
    MenuCategoryProjection findProjectedById(@Param("id") Long id);
}

