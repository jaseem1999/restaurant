package com.restaurant.menu_service.service.menu.impl;

import com.restaurant.menu_service.entity.menu.MenuItemVariant;
import com.restaurant.menu_service.projection.variant.VariantProjection;
import com.restaurant.menu_service.repository.menu.MenuItemVariantRepository;
import com.restaurant.menu_service.service.menu.MenuItemVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemVariantServiceImpl implements MenuItemVariantService {
    private final MenuItemVariantRepository repository;

    @Override
    public MenuItemVariant create(MenuItemVariant variant) {
        variant.setCreatedAt(Instant.now());
        return repository.save(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public VariantProjection getById(Long id) {
        return repository.findByIdProjection(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VariantProjection> listByMenuItem(Long menuItemId) {
        return repository.findByMenuItemIdProjection(menuItemId);
    }

    @Override
    public MenuItemVariant update(Long id, MenuItemVariant variant) {
        return repository.findById(id).map(existing -> {
            existing.setVariantName(variant.getVariantName());
            existing.setPriceAdjustment(variant.getPriceAdjustment());
            existing.setUpdatedAt(Instant.now());
            existing.setUpdatedBy(variant.getUpdatedBy());
            existing.setAdditionalPreparationTime(variant.getAdditionalPreparationTime());
            existing.setAdditionalCalories(variant.getAdditionalCalories());
            existing.setAvailable(variant.getAvailable());
            return repository.save(existing);
        }).orElse(null);
    }

    @Override
    public String delete(Long id) {
        if (!repository.existsById(id)) {
            return "NOT_FOUND";
        }
        repository.deleteById(id);
        return "DELETED";
    }
}

