package com.restaurant.menu_service.repository.menu;

import com.restaurant.menu_service.entity.menu.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByRestaurantId(Long restaurantId);
    
    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);
    
    List<MenuItem> findByRestaurantIdAndFeaturedTrue(Long restaurantId);
    
    List<MenuItem> findByCategoryId(Long categoryId);
    
    List<MenuItem> findByCategoryIdAndAvailableTrue(Long categoryId);
    
    boolean existsByItemNameAndRestaurantId(String itemName, Long restaurantId);
}

