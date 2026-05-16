package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    
    List<MenuCategory> findByRestaurantId(Long restaurantId);
    
    List<MenuCategory> findByRestaurantIdAndActiveTrue(Long restaurantId);
    
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);
}

