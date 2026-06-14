package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuItemDto;
import com.restaurant.menu_service.entity.menu.MenuCategory;
import com.restaurant.menu_service.entity.menu.MenuItem;
import com.restaurant.menu_service.entity.menu.enums.FoodType;
import com.restaurant.menu_service.entity.menu.enums.ItemType;
import com.restaurant.menu_service.projection.menu.response.MenuItemProjection;
import com.restaurant.menu_service.security.SecurityCheckApisClass;
import com.restaurant.menu_service.service.menu.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu/items")
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService service;
    private final SecurityCheckApisClass securityCheckApis;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemDto>> create(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody MenuItemDto dto) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuItem item = fromDto(dto);
        item.setCreatedBy(dto.getCreatedBy());
        MenuItem created = service.create(item);
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemDto>> getById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuItemProjection item = service.getById(id);
        if (item == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(item), true, "OK", HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemDto>>> list(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) Long categoryId) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        List<MenuItemProjection> items;
        if (categoryId != null) {
            items = service.listByCategory(categoryId);
        } else {
            items = service.listByRestaurant(restaurantId);
        }
        List<MenuItemDto> dtos = items.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemDto>> update(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id, @RequestBody MenuItemDto dto) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuItem item = fromDto(dto);
        MenuItem updated = service.update(id, item);
        if (updated == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(updated), true, "Updated", HttpStatus.OK));
    }

    @DeleteMapping("/{id}/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id,
            @PathVariable Long restaurantId
    ) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        String value=service.delete(id, restaurantId);
        if (!"TRUE".equals(value)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, value, HttpStatus.NOT_FOUND));
        }
        return ResponseEntity.ok(new ApiResponse<>(null, true, "Deleted", HttpStatus.NO_CONTENT));
    }

    private MenuItemDto toDto(MenuItem m) {
        if (m == null) return null;
        MenuItemDto dto = new MenuItemDto();
        dto.setItemId(m.getId());
        dto.setItemName(m.getItemName());
        dto.setDescription(m.getDescription());
        dto.setItemType(m.getItemType() != null ? m.getItemType().name() : null);
        dto.setFoodType(m.getFoodType() != null ? m.getFoodType().name() : null);
        dto.setBasePrice(m.getBasePrice());
        dto.setPreparationTime(m.getPreparationTime());
        dto.setCalories(m.getCalories());
        dto.setImage(m.getImage());
        dto.setAvailable(m.getAvailable());
        dto.setFeatured(m.getFeatured());
        dto.setTaxPercentage(m.getTaxPercentage());
        dto.setRestaurantId(m.getRestaurantId());
        if (m.getCategory() != null) dto.setCategoryId(m.getCategory().getId());
        return dto;
    }

    private MenuItemDto toDto(MenuItemProjection p) {
        if (p == null) return null;
        MenuItemDto dto = new MenuItemDto();
        dto.setItemId(p.getId());
        dto.setItemName(p.getItemName());
        dto.setDescription(p.getDescription());
        dto.setItemType(p.getItemType() != null ? p.getItemType().name() : null);
        dto.setFoodType(p.getFoodType() != null ? p.getFoodType().name() : null);
        dto.setBasePrice(p.getBasePrice());
        dto.setPreparationTime(p.getPreparationTime());
        dto.setCalories(p.getCalories());
        dto.setCategoryId(p.getCategoryId());
        dto.setImage(p.getImage());
        dto.setAvailable(p.getAvailable());
        dto.setFeatured(p.getFeatured());
        dto.setTaxPercentage(p.getTaxPercentage());
        dto.setRestaurantId(p.getRestaurantId());
        dto.setCreatedBy(p.getCreatedBy());
        dto.setUpdatedBy(p.getUpdatedBy());
        return dto;
    }

    private MenuItem fromDto(MenuItemDto dto) {
        if (dto == null) return null;
        MenuItem m = new MenuItem();
        m.setId(dto.getItemId());
        m.setItemName(dto.getItemName());
        m.setDescription(dto.getDescription());
        // enums mapping omitted for brevity; assume service handles string values or set null
        m.setBasePrice(dto.getBasePrice());
        m.setItemType(dto.getItemType() != null ? Enum.valueOf(ItemType.class, dto.getItemType()) : null);
        m.setFoodType(dto.getFoodType() != null ? Enum.valueOf(FoodType.class, dto.getFoodType()) : null);
        m.setPreparationTime(dto.getPreparationTime());
        m.setCalories(dto.getCalories());
        m.setImage(dto.getImage());
        m.setAvailable(dto.getAvailable());
        m.setFeatured(dto.getFeatured());
        m.setTaxPercentage(dto.getTaxPercentage());
        m.setRestaurantId(dto.getRestaurantId());
        
        // Set category with ID from DTO; service will look up and validate the category exists
        if (dto.getCategoryId() != null) {
            MenuCategory category = new MenuCategory();
            category.setId(dto.getCategoryId());
            m.setCategory(category);
        }
        
        return m;
    }
}

