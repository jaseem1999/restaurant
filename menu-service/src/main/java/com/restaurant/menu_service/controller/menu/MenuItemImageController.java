package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuItemImageDto;
import com.restaurant.menu_service.entity.menu.MenuItemImage;
import com.restaurant.menu_service.projection.images.ItemImagesProjection;
import com.restaurant.menu_service.security.SecurityCheckApisClass;
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
    private final SecurityCheckApisClass securityCheckApis;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemImageDto>> create(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody MenuItemImageDto dto) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuItemImage created = service.create(fromDto(dto));
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemImageDto>> getById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        ItemImagesProjection image = service.getById(id);
        if (image == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(imageProjectionToDto(image), true, "OK", HttpStatus.OK));
    }

    private MenuItemImageDto imageProjectionToDto(ItemImagesProjection i)
    {
        if (i == null) return null;
        MenuItemImageDto dto = new MenuItemImageDto();
        dto.setImageId(i.getId());
        dto.setImageUrl(i.getImageUrl());
        dto.setAltText(i.getAltText());
        dto.setDisplayOrder(i.getDisplayOrder());
        dto.setActive(i.getActive());
        dto.setMenuItemId(i.getMenuItemId());
        return dto;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuItemImageDto>>> listByMenuItem(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam Long menuItemId) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        List<ItemImagesProjection> list = service.listByMenuItem(menuItemId);
        List<MenuItemImageDto> dtos = list.stream().map(this::imageProjectionToDto).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemImageDto>> update(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id, @RequestBody MenuItemImageDto dto) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuItemImage updated = service.update(id, fromDto(dto));
        if (updated == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(updated), true, "Updated", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isAuthorised=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorised) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        String result=service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, true, result, HttpStatus.NO_CONTENT));
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
        if (dto.getMenuItemId() != null) {
            i.setMenuItem(new com.restaurant.menu_service.entity.menu.MenuItem());
            i.getMenuItem().setId(dto.getMenuItemId());
        }
        if(dto.getUpdatedBy()!=null)
            i.setUpdatedBy(dto.getUpdatedBy());
        i.setId(dto.getImageId());
        i.setCreatedBy(dto.getCreatedBy());
        i.setImageUrl(dto.getImageUrl());
        i.setAltText(dto.getAltText());
        i.setDisplayOrder(dto.getDisplayOrder());
        i.setActive(dto.getActive());
        return i;
    }
}

