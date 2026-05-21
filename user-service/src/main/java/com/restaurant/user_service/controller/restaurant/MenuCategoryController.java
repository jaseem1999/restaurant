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

/**
 * Developed by: Jaseem
 * Updated by:
 * Tested by: Jaseem
 * stage: completed
 * Time verified by: 2026-05-16
 * Description:
 * Controller for managing menu categories in the restaurant.
 * Provides endpoints for creating, retrieving, updating, and deleting menu categories.
 */
@RequestMapping("/restaurant/menu")
@Validated
@RestController
@RequiredArgsConstructor
public class MenuCategoryController {
    private final IMenuCategoryService menuService;

    /**
     * Developed by: Jaseem
     * Updated by:
     * Tested by: Jaseem
     * stage: completed
     * Time verified by: 2026-05-16
     * Description:
     * Endpoint to create a new menu category. Expects a MenuCategoryRequest in the request body and returns the created MenuCategoryResponse wrapped in an ApiResponse.
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> createMenu(
            @RequestBody MenuCategoryRequest request
            ) {
        ApiResponse<MenuCategoryResponse> response = menuService.createMenuCategory(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Developed by: Jaseem
     * Updated by:
     * Tested by: Jaseem
     * stage: completed
     * Time verified by: 2026-05-16
     * Description:
     * Endpoint to retrieve all menu categories for a specific restaurant. Expects a restaurantId as a path variable and returns a list of MenuCategoryResponse wrapped in an ApiResponse.
     * @return
     */
    @GetMapping("/categories/restaurantId")
    public ResponseEntity<ApiResponse<List<MenuCategoryResponse>>> getMenuCategoriesByRestaurantId() {
        ApiResponse<List<MenuCategoryResponse>> response = menuService.getMenuCategoriesByRestaurantId();
        return new ResponseEntity<>(response, response.getStatus());
    }

        /**
        * Developed by: Jaseem
        * Updated by:
        * Tested by: Jaseem
        * stage: completed
        * Time verified by: 2026-05-16
        * Description:
        * Endpoint to retrieve a menu category by its ID. Expects a categoryId as a path variable and returns the corresponding MenuCategoryResponse wrapped in an ApiResponse.
        * @param categoryId
        * @return
        */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> getMenuCategoryById(Long categoryId) {
        ApiResponse<MenuCategoryResponse> response = menuService.getMenuCategoryById(categoryId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /*
    * Developed by: Jaseem
    * Updated by:
    * Tested by: Jaseem
    * stage: completed
    * Time verified by: 2026-05-16
    * Description:
    * Endpoint to update an existing menu category. Expects a MenuCategoryUpdateRequest in the request body and returns the updated MenuCategoryResponse wrapped in an ApiResponse.
     * @param request
     */
    @PutMapping(path = "/category/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> updateMenuCategory(@RequestBody  MenuCategoryUpdateRequest request) {
        ApiResponse<MenuCategoryResponse> response = menuService.updateMenuCategory(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Developed by: Jaseem
     * Updated by:
     * Tested by: Jaseem
     * stage: completed
     * Time verified by: 2026-05-16
     * Description:
     * Endpoint to delete a menu category by its ID. Expects a categoryId as a path variable and returns an ApiResponse indicating the success or failure of the deletion operation.
     * @param
     * @return
     */
    @DeleteMapping("/category/delete/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuCategory(@PathVariable("categoryId") Long id) {
       ApiResponse<Void> response = menuService.deleteMenuCategory(id);
         return new ResponseEntity<>(response, response.getStatus());
    }
}
