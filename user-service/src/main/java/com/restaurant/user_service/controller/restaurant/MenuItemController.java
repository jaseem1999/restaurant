package com.restaurant.user_service.controller.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuitems.request.MenuItemRequest;
import com.restaurant.user_service.dto.menuitems.response.MenuItemsResponse;
import com.restaurant.user_service.service.restaurant.IMenuItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/restaurant/menu/item")
@Validated
@RestController
@RequiredArgsConstructor
public class MenuItemController {
    private final IMenuItemService menuItemService;

    @PostMapping(path = "/create", consumes = "application/json")
    public ResponseEntity<ApiResponse<MenuItemsResponse>> saveMenuItem(
            @Valid @RequestBody MenuItemRequest request
            ) {
        ApiResponse<MenuItemsResponse> response = menuItemService.saveMenuItem(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(path = "/category/{categoryId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<MenuItemsResponse>>> getMenuItemsByCategory(@Valid @Min(1) @PathVariable Long categoryId) {
        ApiResponse<List<MenuItemsResponse>> response = menuItemService.getMenuItemsByCategory(categoryId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(path = "/restaurant/{restaurantId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<MenuItemsResponse>>> getMenuItemsByRestaurant(@Valid @Min(1) @PathVariable Long restaurantId) {
        ApiResponse<List<MenuItemsResponse>> response = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
