package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuItemImageDto;
import com.restaurant.menu_service.entity.menu.MenuItemImage;
import com.restaurant.menu_service.service.menu.MenuItemImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu/images")
@RequiredArgsConstructor
public class MenuItemImageController {
    private final MenuItemImageService service;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemImageDto>> create(@RequestBody MenuItemImageDto dto) {
        MenuItemImage created = service.create(fromDto(dto));
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemImageDto>> getById(@PathVariable Long id) {
        MenuItemImage image = service.getById(id);
        if (image == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(image), true, "OK", HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemImageDto>>> listByMenuItem(@RequestParam Long menuItemId) {
        List<MenuItemImage> list = service.listByMenuItem(menuItemId);
        List<MenuItemImageDto> dtos = list.stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemImageDto>> update(@PathVariable Long id, @RequestBody MenuItemImageDto dto) {
        MenuItemImage updated = service.update(id, fromDto(dto));
        if (updated == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(updated), true, "Updated", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, true, "Deleted", HttpStatus.NO_CONTENT));
    }

    private MenuItemImageDto toDto(MenuItemImage i) {
        if (i == null) return null;
        MenuItemImageDto dto = new MenuItemImageDto();
        dto.setImageId(i.getId());
        dto.setImageUrl(i.getImageUrl());
        dto.setAltText(i.getAltText());
        dto.setDisplayOrder(i.getDisplayOrder());
        dto.setActive(i.getActive());
        if (i.getMenuItem() != null) dto.setMenuItemId(i.getMenuItem().getId());
        return dto;
    }

    private MenuItemImage fromDto(MenuItemImageDto dto) {
        if (dto == null) return null;
        MenuItemImage i = new MenuItemImage();
        i.setId(dto.getImageId());
        i.setImageUrl(dto.getImageUrl());
        i.setAltText(dto.getAltText());
        i.setDisplayOrder(dto.getDisplayOrder());
        i.setActive(dto.getActive());
        return i;
    }
}

