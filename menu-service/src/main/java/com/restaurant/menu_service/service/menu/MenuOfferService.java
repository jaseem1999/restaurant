package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuOffer;
import com.restaurant.menu_service.projection.offer.MenuOfferProjection;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface MenuOfferService {
    MenuOffer create(MenuOffer offer);
    MenuOfferProjection getById(Long id);
    List<MenuOfferProjection> listByMenuItem(Long menuItemId);
    List<MenuOfferProjection> listActiveByMenuItem(Long menuItemId);
    List<MenuOfferProjection> listActiveBetween(Instant from, Instant to);
    MenuOffer update(Long id, MenuOffer offer);
    String delete(Long id);
}

