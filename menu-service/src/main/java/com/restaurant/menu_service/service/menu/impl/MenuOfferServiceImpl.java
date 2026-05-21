package com.restaurant.menu_service.service.menu.impl;

import com.restaurant.menu_service.entity.menu.MenuOffer;
import com.restaurant.menu_service.repository.menu.MenuOfferRepository;
import com.restaurant.menu_service.service.menu.MenuOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuOfferServiceImpl implements MenuOfferService {
    private final MenuOfferRepository repository;

    @Override
    public MenuOffer create(MenuOffer offer) {
        return repository.save(offer);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuOffer getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuOffer> listByMenuItem(Long menuItemId) {
        return repository.findByMenuItemId(menuItemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuOffer> listActiveByMenuItem(Long menuItemId) {
        return repository.findByMenuItemIdAndActiveTrueAndEndDateAfter(menuItemId, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuOffer> listActiveBetween(LocalDateTime from, LocalDateTime to) {
        return repository.findByActiveTrueAndStartDateBeforeAndEndDateAfter(from, to);
    }

    @Override
    public MenuOffer update(Long id, MenuOffer offer) {
        return repository.findById(id).map(existing -> {
            existing.setOfferName(offer.getOfferName());
            existing.setDescription(offer.getDescription());
            existing.setOfferType(offer.getOfferType());
            existing.setDiscountValue(offer.getDiscountValue());
            existing.setMinimumOrderValue(offer.getMinimumOrderValue());
            existing.setStartDate(offer.getStartDate());
            existing.setEndDate(offer.getEndDate());
            existing.setActive(offer.getActive());
            return repository.save(existing);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

