package com.restaurant.menu_service.service.menu.impl;

import com.restaurant.menu_service.entity.menu.MenuCategory;
import com.restaurant.menu_service.projection.menu.response.MenuCategoryProjection;
import com.restaurant.menu_service.repository.menu.MenuCategoryRepository;
import com.restaurant.menu_service.service.menu.MenuCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuCategoryServiceImpl implements MenuCategoryService {
    private final MenuCategoryRepository repository;

    @Override
    public MenuCategory create(MenuCategory category) {
        category.setUpdatedAt(null);
        category.setUpdatedAt(null);
        return repository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuCategoryProjection getById(Long id) {
        return repository.findProjectedById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuCategoryProjection> listByRestaurant(Long restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }

    @Override
    public MenuCategory update(Long id, MenuCategory category) {
        return repository.findById(id).map(existing -> {
            existing.setName(category.getName());
            existing.setDescription(category.getDescription());
            existing.setImage(category.getImage());
            existing.setDisplayOrder(category.getDisplayOrder());
            existing.setActive(category.getActive());
            existing.setUpdatedBy(category.getUpdatedBy());
            existing.setUpdatedAt(Instant.now());
            return repository.save(existing);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

