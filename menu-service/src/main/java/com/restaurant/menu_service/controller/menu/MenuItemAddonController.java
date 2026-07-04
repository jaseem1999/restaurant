package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuItemAddonDto;
import com.restaurant.menu_service.entity.menu.MenuItemAddon;
import com.restaurant.menu_service.projection.addon.MenuAddonProjection;
import com.restaurant.menu_service.security.SecurityCheckApisClass;
import com.restaurant.menu_service.service.menu.MenuItemAddonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu/addons")
@RequiredArgsConstructor
public class MenuItemAddonController {
    private final MenuItemAddonService service;
    private final SecurityCheckApisClass securityCheckApis;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemAddonDto>> create(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody MenuItemAddonDto dto) {
        boolean isApiVerified =securityCheckApis.checkApi(authorizationHeader);
        if (!isApiVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuItemAddon created = service.create(fromDto(dto));
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemAddonDto>> getById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isApiVerified =securityCheckApis.checkApi(authorizationHeader);
        if (!isApiVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuAddonProjection addon = service.getById(id);
        if (addon == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(projectionToDTO(addon), true, "OK", HttpStatus.OK));
    }

    private MenuItemAddonDto projectionToDTO(MenuAddonProjection addon) {
        if (addon == null) return null;
        MenuItemAddonDto dto = new MenuItemAddonDto();
        dto.setAddonId(addon.getAddonId());
        dto.setAddonName(addon.getAddonName());
        dto.setPrice(addon.getPrice());
        dto.setAdditionalPreparationTime(addon.getAdditionalPreparationTime());
        dto.setAdditionalCalories(addon.getAdditionalCalories());
        dto.setAvailable(addon.getAvailable());
        if (addon.getMenuItemId() != null) dto.setMenuItemId(addon.getMenuItemId());
        return dto;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemAddonDto>>> list(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long menuItemId) {
        boolean isApiVerified =securityCheckApis.checkApi(authorizationHeader);
        if (!isApiVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        List<MenuAddonProjection> list = service.listByMenuItem(menuItemId);
        List<MenuItemAddonDto> dtos = list.stream().map(this::projectionToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemAddonDto>> update(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id, @RequestBody MenuItemAddonDto dto) {
        boolean isApiVerified =securityCheckApis.checkApi(authorizationHeader);
        if (!isApiVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuItemAddon updated = service.update(id, fromDto(dto));
        if (updated == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(updated), true, "Updated", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isApiVerified =securityCheckApis.checkApi(authorizationHeader);
        if (!isApiVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, true, "Deleted", HttpStatus.NO_CONTENT));
    }

    private MenuItemAddonDto toDto(MenuItemAddon a) {
        if (a == null) return null;
        MenuItemAddonDto dto = new MenuItemAddonDto();
        dto.setAddonId(a.getId());
        dto.setAddonName(a.getAddonName());
        dto.setPrice(a.getPrice());
        dto.setAdditionalPreparationTime(a.getAdditionalPreparationTime());
        dto.setAdditionalCalories(a.getAdditionalCalories());
        dto.setAvailable(a.getAvailable());
        if (a.getMenuItem() != null) dto.setMenuItemId(a.getMenuItem().getId());
        return dto;
    }

    private MenuItemAddon fromDto(MenuItemAddonDto dto) {
        if (dto == null) return null;
        MenuItemAddon a = new MenuItemAddon();
        a.setId(dto.getAddonId());
        a.setAddonName(dto.getAddonName());
        a.setPrice(dto.getPrice());
        a.setAdditionalPreparationTime(dto.getAdditionalPreparationTime());
        a.setAdditionalCalories(dto.getAdditionalCalories());
        a.setAvailable(dto.getAvailable());
        a.setCreatedAt(Instant.now());
        a.setCreatedBy(dto.getCreatedBy());
        return a;
    }
}

