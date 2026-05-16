package com.restaurant.menu_service.service.menu.impl;

import com.restaurant.menu_service.entity.menu.MenuItemAddon;
import com.restaurant.menu_service.repository.menu.MenuItemAddonRepository;
import com.restaurant.menu_service.service.menu.MenuItemAddonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemAddonServiceImpl implements MenuItemAddonService {
    private final MenuItemAddonRepository repository;

    @Override
    public MenuItemAddon create(MenuItemAddon addon) {
        return repository.save(addon);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemAddon getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemAddon> listByMenuItem(Long menuItemId) {
        return repository.findByMenuItemId(menuItemId);
    }

    @Override
    public MenuItemAddon update(Long id, MenuItemAddon addon) {
        return repository.findById(id).map(existing -> {
            existing.setAddonName(addon.getAddonName());
            existing.setPrice(addon.getPrice());
            existing.setAdditionalPreparationTime(addon.getAdditionalPreparationTime());
            existing.setAdditionalCalories(addon.getAdditionalCalories());
            existing.setAvailable(addon.getAvailable());
            return repository.save(existing);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

