package com.restaurant.menu_service.service.menu.impl;

import com.restaurant.menu_service.entity.menu.MenuOffer;
import com.restaurant.menu_service.projection.offer.MenuOfferProjection;
import com.restaurant.menu_service.repository.menu.MenuOfferRepository;
import com.restaurant.menu_service.service.menu.MenuOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuOfferServiceImpl implements MenuOfferService {
    private final MenuOfferRepository repository;

    @Override
    public MenuOffer create(MenuOffer offer) {
        offer.setCreatedAt(Instant.now());
        return repository.save(offer);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuOfferProjection getById(Long id) {
        return repository.findByIdProjection(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuOfferProjection> listByMenuItem(Long menuItemId) {
        return repository.findByMenuItemId(menuItemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuOfferProjection> listActiveByMenuItem(Long menuItemId) {
        try {
            return repository.findByMenuItemIdAndActiveTrueAndEndDateAfter(menuItemId, Instant.now());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuOfferProjection> listActiveBetween(Instant from, Instant to) {
        return repository.findByActiveTrueAndStartDateBeforeAndEndDateAfter(from, to);
    }

    @Override
    public MenuOffer update(Long id, MenuOffer offer) {
        return repository.findById(id).map(existing -> {
            existing.setOfferName(offer.getOfferName());
            existing.setDescription(offer.getDescription());
            existing.setOfferType(offer.getOfferType());
            existing.setDiscountValue(offer.getDiscountValue());
            existing.setUpdatedAt(Instant.now());
            existing.setUpdatedBy(offer.getUpdatedBy());
            existing.setMinimumOrderValue(offer.getMinimumOrderValue());
            existing.setStartDate(offer.getStartDate());
            existing.setEndDate(offer.getEndDate());
            existing.setActive(offer.getActive());
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

