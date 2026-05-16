package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuCategoryDto;
import com.restaurant.menu_service.entity.menu.MenuCategory;
import com.restaurant.menu_service.security.SecurityCheckApisClass;
import com.restaurant.menu_service.service.menu.MenuCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/menu/categories")
@RequiredArgsConstructor
/**
 * MenuCategoryController
 *
 * Developed by: Jaseem
 * Updated by:
 * Tested by:
 * stage: in progress
 * Time verified by: 2026-05-16
 *
 * Description:
 * REST controller that exposes CRUD operations for menu categories.
 * Accepts and returns DTOs wrapped in ApiResponse to avoid exposing
 * internal entity structures directly.
 */
@Slf4j
public class MenuCategoryController {
    private final MenuCategoryService service;
    private final SecurityCheckApisClass securityCheckApis;

    /**
     * Developed by: Jaseem
     * Updated by:
     * Tested by: Jaseem
     * stage: completed
     * Time verified by: 2026-05-16
     * Description:
     * Endpoint to create a new menu category. Expects a MenuCategoryDto in the request
     * @param authorizationHeader
     * @param dto
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MenuCategoryDto>> create(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody MenuCategoryDto dto) {
        boolean isApiVerified =securityCheckApis.checkApi(authorizationHeader);
        if (!isApiVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuCategory category = fromDto(dto);
        MenuCategory created = service.create(category);
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuCategoryDto>> getById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isApiVerified =securityCheckApis.checkApi(authorizationHeader);
        if (!isApiVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuCategory category = service.getById(id);
        if (category == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(category), true, "OK", HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuCategoryDto>>> listByRestaurant(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) Long restaurantId) {
        boolean isApiVerified =securityCheckApis.checkApi(authorizationHeader);
        if (!isApiVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        List<MenuCategory> list = service.listByRestaurant(restaurantId);
        List<MenuCategoryDto> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuCategoryDto>> update(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id, @RequestBody MenuCategoryDto dto) {
        boolean isApiVerified =securityCheckApis.checkApi(authorizationHeader);
        if (!isApiVerified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuCategory category = fromDto(dto);
        MenuCategory updated = service.update(id, category);
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

    private MenuCategoryDto toDto(MenuCategory c) {
        if (c == null) return null;
        MenuCategoryDto dto = new MenuCategoryDto();
        dto.setCategoryId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());
        dto.setImage(c.getImage());
        dto.setDisplayOrder(c.getDisplayOrder());
        dto.setActive(c.getActive());
        dto.setRestaurantId(c.getRestaurantId());
        dto.setCreatedBy(c.getCreatedBy());
        
        dto.setUpdatedBy(c.getUpdatedBy());
        return dto;
    }

    private MenuCategory fromDto(MenuCategoryDto dto) {
        if (dto == null) return null;
        MenuCategory c = new MenuCategory();
        c.setId(dto.getCategoryId());
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setImage(dto.getImage());
        c.setDisplayOrder(dto.getDisplayOrder());
        c.setActive(dto.getActive());
        c.setRestaurantId(dto.getRestaurantId());
        c.setCreatedAt(Instant.now());
        c.setCreatedBy(dto.getCreatedBy());
        c.setUpdatedBy(dto.getUpdatedBy());
        return c;
    }
}

