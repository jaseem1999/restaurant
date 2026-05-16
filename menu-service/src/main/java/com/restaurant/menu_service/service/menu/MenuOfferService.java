package com.restaurant.menu_service.service.menu;

import com.restaurant.menu_service.entity.menu.MenuOffer;

import java.time.LocalDateTime;
import java.util.List;

public interface MenuOfferService {
    MenuOffer create(MenuOffer offer);
    MenuOffer getById(Long id);
    List<MenuOffer> listByMenuItem(Long menuItemId);
    List<MenuOffer> listActiveByMenuItem(Long menuItemId);
    List<MenuOffer> listActiveBetween(LocalDateTime from, LocalDateTime to);
    MenuOffer update(Long id, MenuOffer offer);
    void delete(Long id);
}

