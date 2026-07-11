package com.restaurant.menu_service.service.menu.impl;

import com.restaurant.menu_service.entity.menu.MenuItemImage;
import com.restaurant.menu_service.projection.images.ItemImagesProjection;
import com.restaurant.menu_service.repository.menu.MenuItemImageRepository;
import com.restaurant.menu_service.service.menu.MenuItemImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemImageServiceImpl implements MenuItemImageService {
    private final MenuItemImageRepository repository;

    @Override
    public MenuItemImage create(MenuItemImage image) {
        image.setCreatedAt(Instant.now());
        return repository.save(image);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemImagesProjection getById(Long id) {
        return repository.findByIdItemImagesProjection(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemImagesProjection> listByMenuItem(Long menuItemId) {
        return repository.findByMenuItemIdOrderByDisplayOrder(menuItemId);
    }

    @Override
    public MenuItemImage update(Long id, MenuItemImage image) {
        return repository.findById(id).map(existing -> {
            existing.setImageUrl(image.getImageUrl());
            existing.setAltText(image.getAltText());
            existing.setDisplayOrder(image.getDisplayOrder());
            existing.setActive(image.getActive());
            existing.setUpdatedAt(Instant.now());
            existing.setUpdatedBy(image.getUpdatedBy());
            return repository.save(existing);
        }).orElse(null);
    }

    @Override
    public String delete(Long id) {
        if (!repository.existsById(id)) {
            return "NOT_FOUND";
        }
        try {
            repository.deleteById(id);
            return "DELETED";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

