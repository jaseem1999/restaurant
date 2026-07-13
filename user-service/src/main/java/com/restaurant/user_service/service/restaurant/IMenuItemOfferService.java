package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuoffer.request.MenuItemOfferRequest;
import com.restaurant.user_service.dto.menuoffer.request.MenuItemOfferUpdateRequest;
import com.restaurant.user_service.dto.menuoffer.response.MenuItemOfferResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IMenuItemOfferService {
    ApiResponse<MenuItemOfferResponse> create(@Valid MenuItemOfferRequest request);

    ApiResponse<MenuItemOfferResponse> update(@Valid MenuItemOfferUpdateRequest request);

    ApiResponse<MenuItemOfferResponse> getOfferById(Long offerId);

    ApiResponse<List<MenuItemOfferResponse>> getAllOffers(long menuItemId, Boolean active);

    ApiResponse<String> deleteOffer(Long offerId);
}
