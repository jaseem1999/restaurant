package com.restaurant.user_service.controller.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuvariant.request.MenuItemVariantRequest;
import com.restaurant.user_service.dto.menuvariant.request.MenuItemVariantUpdateRequest;
import com.restaurant.user_service.dto.menuvariant.response.MenuItemVariantResponse;
import com.restaurant.user_service.service.restaurant.menu.IMenuItemVariantServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/menu-item/variants")
@Validated
@RequiredArgsConstructor
public class MenuItemVariantController {
    private final IMenuItemVariantServices services;

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ApiResponse<MenuItemVariantResponse>> createMenuItemVariant(
            @RequestBody @Valid MenuItemVariantRequest request
            ){
        ApiResponse<MenuItemVariantResponse> response = services.createMenuItemVariant(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<MenuItemVariantResponse>> getMenuItemVariantById(@PathVariable Long id) {
        ApiResponse<MenuItemVariantResponse> response = services.getMenuItemVariantById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ApiResponse<MenuItemVariantResponse>> updateMenuItemVariant(
            @PathVariable Long id,
            @RequestBody @Valid MenuItemVariantUpdateRequest request
    ) {
        ApiResponse<MenuItemVariantResponse> response = services.updateMenuItemVariant(id, request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(path = "item/{menuItemId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<MenuItemVariantResponse>>> getMenuItemVariantsByMenuItemId(@PathVariable Long menuItemId) {
        ApiResponse<List<MenuItemVariantResponse>> response = services.getMenuItemVariantByItemId(menuItemId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMenuItemVariantById(@PathVariable Long id) {
        ApiResponse<String> response = services.deleteMenuItemVariantById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }



}
