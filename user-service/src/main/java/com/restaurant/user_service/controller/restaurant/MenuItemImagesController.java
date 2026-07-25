package com.restaurant.user_service.controller.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuimage.request.MenuItemImageRequest;
import com.restaurant.user_service.dto.menuimage.request.MenuItemImageUpdateRequest;
import com.restaurant.user_service.dto.menuimage.response.MenuItemImageResponse;
import com.restaurant.user_service.service.restaurant.menu.IMenuImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/restaurant/menu/images")
@Validated
@RestController
@RequiredArgsConstructor
public class MenuItemImagesController {
    private final IMenuImageService iMenuImageService;

    @PostMapping(path = "add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<MenuItemImageResponse>> createItemImage(@Valid @RequestBody
                                                                              MenuItemImageRequest menuItemImageRequest){
        ApiResponse<MenuItemImageResponse> apiResponse = iMenuImageService.createItemImage(menuItemImageRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @GetMapping(path = "item/{menuItemId}", produces = "application/json")
    public ResponseEntity<ApiResponse<List<MenuItemImageResponse>>> listByMenuItem(
            @Valid @PathVariable("menuItemId") Long menuItemId) {
        ApiResponse<List<MenuItemImageResponse>> apiResponse = iMenuImageService.listByMenuItem(menuItemId);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<MenuItemImageResponse>> getItemImageById(
            @Valid @PathVariable("id") Long id) {
        // Assuming you have a method in your service to get an item image by ID
        ApiResponse<MenuItemImageResponse> apiResponse = iMenuImageService.getItemImageById(id);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PutMapping(path = "update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<MenuItemImageResponse>> updateItemImage(
            @Valid @RequestBody MenuItemImageUpdateRequest request
            ){
        ApiResponse<MenuItemImageResponse> response = iMenuImageService.updateItemImage(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<String>> deleteItemImage(
            @Valid @PathVariable("id") Long id){
        ApiResponse<String> response = iMenuImageService.deleteItemImage(id);
        return new ResponseEntity<>(response, response.getStatus());
    }


}
