package com.restaurant.menu_service.service.menu.impl;

import com.restaurant.menu_service.entity.menu.MenuCategory;
import com.restaurant.menu_service.entity.menu.MenuItem;
import com.restaurant.menu_service.repository.menu.MenuCategoryRepository;
import com.restaurant.menu_service.repository.menu.MenuItemRepository;
import com.restaurant.menu_service.service.menu.MenuItemService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository repository;
    private final MenuCategoryRepository categoryRepository;

    @Override
    public MenuItem create(MenuItem item) {

        log.warn("Menu item details: {}", item);

        item.setCreatedAt(Instant.now());
        
        // Validate category is provided
        if (item.getCategory() == null || item.getCategory().getId() == null) {
            throw new NotFoundException("Menu category is required for menu item creation");
        }
        
        MenuCategory category = categoryRepository.findById(item.getCategory().getId())
                .orElse(null);
        if (category == null) {
            throw new NotFoundException("Menu category not found with id: " + item.getCategory().getId());
        }
        item.setCategory(category);
        return repository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItem getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> listByRestaurant(Long restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> listByCategory(Long categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public MenuItem update(Long id, MenuItem item) {
        return repository.findById(id).map(existing -> {
            existing.setItemName(item.getItemName());
            existing.setDescription(item.getDescription());
            existing.setItemType(item.getItemType());
            existing.setFoodType(item.getFoodType());
            existing.setBasePrice(item.getBasePrice());
            existing.setPreparationTime(item.getPreparationTime());
            existing.setCalories(item.getCalories());
            existing.setImage(item.getImage());
            existing.setAvailable(item.getAvailable());
            existing.setFeatured(item.getFeatured());
            existing.setTaxPercentage(item.getTaxPercentage());
            return repository.save(existing);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

