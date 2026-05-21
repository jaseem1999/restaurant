package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuItemDto;
import com.restaurant.menu_service.entity.menu.MenuItem;
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

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemDto>> create(@RequestBody MenuItemDto dto) {
        MenuItem item = fromDto(dto);
        MenuItem created = service.create(item);
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemDto>> getById(@PathVariable Long id) {
        MenuItem item = service.getById(id);
        if (item == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(item), true, "OK", HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemDto>>> list(@RequestParam(required = false) Long restaurantId,
                                                               @RequestParam(required = false) Long categoryId) {
        List<MenuItem> items;
        if (categoryId != null) {
            items = service.listByCategory(categoryId);
        } else {
            items = service.listByRestaurant(restaurantId);
        }
        List<MenuItemDto> dtos = items.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemDto>> update(@PathVariable Long id, @RequestBody MenuItemDto dto) {
        MenuItem item = fromDto(dto);
        MenuItem updated = service.update(id, item);
        if (updated == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(updated), true, "Updated", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
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

    private MenuItem fromDto(MenuItemDto dto) {
        if (dto == null) return null;
        MenuItem m = new MenuItem();
        m.setId(dto.getItemId());
        m.setItemName(dto.getItemName());
        m.setDescription(dto.getDescription());
        // enums mapping omitted for brevity; assume service handles string values or set null
        m.setBasePrice(dto.getBasePrice());
        m.setPreparationTime(dto.getPreparationTime());
        m.setCalories(dto.getCalories());
        m.setImage(dto.getImage());
        m.setAvailable(dto.getAvailable());
        m.setFeatured(dto.getFeatured());
        m.setTaxPercentage(dto.getTaxPercentage());
        m.setRestaurantId(dto.getRestaurantId());
        return m;
    }
}

