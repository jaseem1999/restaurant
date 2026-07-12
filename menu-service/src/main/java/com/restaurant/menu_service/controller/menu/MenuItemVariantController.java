package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuItemVariantDto;
import com.restaurant.menu_service.entity.menu.MenuItemVariant;
import com.restaurant.menu_service.projection.variant.VariantProjection;
import com.restaurant.menu_service.security.SecurityCheckApisClass;
import com.restaurant.menu_service.service.menu.MenuItemVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu/variants")
@RequiredArgsConstructor
public class MenuItemVariantController {
    private final MenuItemVariantService service;
    private final SecurityCheckApisClass securityCheckApis;
    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemVariantDto>> create(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody MenuItemVariantDto dto) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuItemVariant created = service.create(fromDto(dto));
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemVariantDto>> getById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        VariantProjection v = service.getById(id);
        if (v == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(formProjectionToDto(v), true, "OK", HttpStatus.OK));
    }

    private MenuItemVariantDto formProjectionToDto(VariantProjection v) {
        MenuItemVariantDto dto = new MenuItemVariantDto();
        dto.setVariantId(v.getVariantId());
        dto.setVariantName(v.getVariantName());
        dto.setPriceAdjustment(v.getPriceAdjustment());
        dto.setAdditionalPreparationTime(v.getAdditionalPreparationTime());
        dto.setAdditionalCalories(v.getAdditionalCalories());
        dto.setAvailable(v.getAvailable());
        dto.setMenuItemId(v.getMenuItemId());
        dto.setCreatedBy(v.getCreatedBy());
        dto.setUpdatedBy(v.getUpdatedBy());
        return dto;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemVariantDto>>> listByMenuItem(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long menuItemId) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        List<VariantProjection> list = service.listByMenuItem(menuItemId);
        List<MenuItemVariantDto> dtos = list.stream().map(this::formProjectionToDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemVariantDto>> update(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id,
            @RequestBody MenuItemVariantDto dto) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuItemVariant updated = service.update(id, fromDto(dto));
        if (updated == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(updated), true, "Updated", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        String result = service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, true, result, HttpStatus.OK));
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
        dto.setCreatedBy(v.getCreatedBy());
        dto.setUpdatedBy(v.getUpdatedBy());
        if (v.getMenuItem() != null) dto.setMenuItemId(v.getMenuItem().getId());
        return dto;
    }

    private MenuItemVariant fromDto(MenuItemVariantDto dto) {
        if (dto == null) return null;
        MenuItemVariant v = new MenuItemVariant();
        if (dto.getCreatedBy() != null)
            v.setCreatedBy(dto.getCreatedBy());
        if (dto.getMenuItemId() != null) {
            v.setMenuItem(new com.restaurant.menu_service.entity.menu.MenuItem());
            v.getMenuItem().setId(dto.getMenuItemId());
        }
        if (dto.getUpdatedBy() != null) {
            v.setUpdatedBy(dto.getUpdatedBy());
            v.setUpdatedAt(Instant.now());
        }
        v.setId(dto.getVariantId());
        v.setVariantName(dto.getVariantName());
        v.setPriceAdjustment(dto.getPriceAdjustment());
        v.setAdditionalPreparationTime(dto.getAdditionalPreparationTime());
        v.setAdditionalCalories(dto.getAdditionalCalories());
        v.setAvailable(dto.getAvailable());
        return v;
    }
}

