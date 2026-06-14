package com.restaurant.menu_service.service.menu.impl;

import com.restaurant.menu_service.entity.menu.MenuCategory;
import com.restaurant.menu_service.entity.menu.MenuItem;
import com.restaurant.menu_service.projection.menu.response.MenuItemProjection;
import com.restaurant.menu_service.repository.menu.MenuCategoryRepository;
import com.restaurant.menu_service.repository.menu.MenuItemRepository;
import com.restaurant.menu_service.service.menu.MenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu category is required for menu item creation");
        }
        
        MenuCategory category = categoryRepository.findById(item.getCategory().getId())
                .orElse(null);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu category not found with id: " + item.getCategory().getId());
        }
        item.setCategory(category);
        try {
            return repository.save(item);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemProjection getById(Long id) {
        return repository.findByIdProjection(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemProjection> listByRestaurant(Long restaurantId) {
        return repository.findByRestaurantIdProjection(restaurantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemProjection> listByCategory(Long categoryId) {
        try {
            return repository.findByCategoryIdProjection(categoryId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

