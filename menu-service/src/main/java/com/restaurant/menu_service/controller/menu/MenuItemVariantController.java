package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuItemVariantDto;
import com.restaurant.menu_service.entity.menu.MenuItemVariant;
import com.restaurant.menu_service.service.menu.MenuItemVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu/variants")
@RequiredArgsConstructor
public class MenuItemVariantController {
    private final MenuItemVariantService service;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemVariantDto>> create(@RequestBody MenuItemVariantDto dto) {
        MenuItemVariant created = service.create(fromDto(dto));
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemVariantDto>> getById(@PathVariable Long id) {
        MenuItemVariant v = service.getById(id);
        if (v == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(v), true, "OK", HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemVariantDto>>> listByMenuItem(@RequestParam Long menuItemId) {
        List<MenuItemVariant> list = service.listByMenuItem(menuItemId);
        List<MenuItemVariantDto> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemVariantDto>> update(@PathVariable Long id, @RequestBody MenuItemVariantDto dto) {
        MenuItemVariant updated = service.update(id, fromDto(dto));
        if (updated == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(updated), true, "Updated", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, true, "Deleted", HttpStatus.NO_CONTENT));
    }

    private MenuItemVariantDto toDto(MenuItemVariant v) {
        if (v == null) return null;
        MenuItemVariantDto dto = new MenuItemVariantDto();
        dto.setVariantId(v.getId());
        dto.setVariantName(v.getVariantName());
        dto.setPriceAdjustment(v.getPriceAdjustment());
        dto.setAdditionalPreparationTime(v.getAdditionalPreparationTime());
        dto.setAdditionalCalories(v.getAdditionalCalories());
        dto.setAvailable(v.getAvailable());
        if (v.getMenuItem() != null) dto.setMenuItemId(v.getMenuItem().getId());
        return dto;
    }

    private MenuItemVariant fromDto(MenuItemVariantDto dto) {
        if (dto == null) return null;
        MenuItemVariant v = new MenuItemVariant();
        v.setId(dto.getVariantId());
        v.setVariantName(dto.getVariantName());
        v.setPriceAdjustment(dto.getPriceAdjustment());
        v.setAdditionalPreparationTime(dto.getAdditionalPreparationTime());
        v.setAdditionalCalories(dto.getAdditionalCalories());
        v.setAvailable(dto.getAvailable());
        return v;
    }
}

