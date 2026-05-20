package com.restaurant.user_service.controller.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menu.request.MenuCategoryRequest;
import com.restaurant.user_service.dto.menu.request.MenuCategoryUpdateRequest;
import com.restaurant.user_service.dto.menu.response.MenuCategoryResponse;
import com.restaurant.user_service.service.restaurant.IMenuCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/restaurant/menu")
@Validated
@RestController
@RequiredArgsConstructor
public class MenuCategoryController {
    private final IMenuCategoryService menuService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> createMenu(
            @RequestBody MenuCategoryRequest request
            ) {
        ApiResponse<MenuCategoryResponse> response = menuService.createMenuCategory(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/categories/{restaurantId}")
    public ResponseEntity<ApiResponse<List<MenuCategoryResponse>>> getMenuCategoriesByRestaurantId(Long restaurantId) {
        ApiResponse<List<MenuCategoryResponse>> response = menuService.getMenuCategoriesByRestaurantId(restaurantId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> getMenuCategoryById(Long categoryId) {
        ApiResponse<MenuCategoryResponse> response = menuService.getMenuCategoryById(categoryId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping(path = "/category/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> updateMenuCategory(@RequestBody  MenuCategoryUpdateRequest request) {
        ApiResponse<MenuCategoryResponse> response = menuService.updateMenuCategory(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/category/delete/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuCategory(@PathVariable("categoryId") Long id) {
       ApiResponse<Void> response = menuService.deleteMenuCategory(id);
         return new ResponseEntity<>(response, response.getStatus());
    }
}
