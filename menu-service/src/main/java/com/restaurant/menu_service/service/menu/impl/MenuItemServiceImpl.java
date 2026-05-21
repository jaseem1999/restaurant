package com.restaurant.menu_service.service.menu.impl;

import com.restaurant.menu_service.entity.menu.MenuItem;
import com.restaurant.menu_service.repository.menu.MenuItemRepository;
import com.restaurant.menu_service.service.menu.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository repository;

    @Override
    public MenuItem create(MenuItem item) {
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

