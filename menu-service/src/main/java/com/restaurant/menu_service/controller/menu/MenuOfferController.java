package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuOfferDto;
import com.restaurant.menu_service.entity.menu.MenuOffer;
import com.restaurant.menu_service.projection.offer.MenuOfferProjection;
import com.restaurant.menu_service.security.SecurityCheckApisClass;
import com.restaurant.menu_service.service.menu.MenuOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/menu/offers")
@RequiredArgsConstructor
public class MenuOfferController {
    private final MenuOfferService service;
    private final SecurityCheckApisClass securityCheckApis;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuOfferDto>> create(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody MenuOfferDto dto) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuOffer created = service.create(fromDto(dto));
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuOfferDto>> getById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuOfferProjection offer = service.getById(id);
        if (offer == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(fromProjectionToDto(offer), true, "OK", HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<java.util.List<MenuOfferDto>>> listByMenuItem(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) Long menuItemId,
            @RequestParam(required = false) Boolean active) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        java.util.List<MenuOfferProjection> offers;
        if (menuItemId != null && Boolean.TRUE.equals(active)) {
            offers = service.listActiveByMenuItem(menuItemId);
        } else if (menuItemId != null) {
            offers = service.listByMenuItem(menuItemId);
        } else {
            offers = service.listActiveBetween(Instant.now().minusSeconds(10 * 365 * 24 * 60 * 60), Instant.now().plusSeconds(10 * 365 * 24 * 60 * 60));
        }
        java.util.List<MenuOfferDto> dtos = offers.stream().map(this::fromProjectionToDto).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    private MenuOfferDto fromProjectionToDto(MenuOfferProjection o) {
        if (o == null) return null;
        MenuOfferDto dto = new MenuOfferDto();
        dto.setOfferId(o.getId());
        dto.setOfferName(o.getOfferName());
        dto.setDescription(o.getDescription());
        dto.setOfferType(o.getOfferType() != null ? o.getOfferType().name() : null);
        dto.setDiscountValue(o.getDiscountValue());
        dto.setMinimumOrderValue(o.getMinimumOrderValue());
        dto.setStartDate(o.getStartDate());
        dto.setEndDate(o.getEndDate());
        dto.setActive(o.getActive());
        dto.setMenuItemId(o.getMenuItemId());
        return dto;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuOfferDto>> update(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id, @RequestBody MenuOfferDto dto) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        MenuOffer updated = service.update(id, fromDto(dto));
        if (updated == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(updated), true, "Updated", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long id) {
        boolean isAuthorized=securityCheckApis.checkApi(authorizationHeader);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, false, "Unauthorized", HttpStatus.UNAUTHORIZED));
        }
        java.lang.String result =service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, true, result, HttpStatus.NO_CONTENT));
    }

    private MenuOfferDto toDto(MenuOffer o) {
        if (o == null) return null;
        MenuOfferDto dto = new MenuOfferDto();
        dto.setOfferId(o.getId());
        dto.setOfferName(o.getOfferName());
        dto.setDescription(o.getDescription());
        dto.setOfferType(o.getOfferType() != null ? o.getOfferType().name() : null);
        dto.setDiscountValue(o.getDiscountValue());
        dto.setMinimumOrderValue(o.getMinimumOrderValue());
        dto.setStartDate(o.getStartDate());
        dto.setEndDate(o.getEndDate());
        dto.setActive(o.getActive());
        if (o.getMenuItem() != null) dto.setMenuItemId(o.getMenuItem().getId());
        return dto;
    }

    private MenuOffer fromDto(MenuOfferDto dto) {
        if (dto == null) return null;
        MenuOffer o = new MenuOffer();
        if (dto.getMenuItemId() != null) {
            com.restaurant.menu_service.entity.menu.MenuItem menuItem = new com.restaurant.menu_service.entity.menu.MenuItem();
            menuItem.setId(dto.getMenuItemId());
            o.setMenuItem(menuItem);
        }
        if(dto.getUpdatedBy() != null) {
            o.setUpdatedBy(dto.getUpdatedBy());
            o.setUpdatedAt(Instant.now());
        }
        if(dto.getCreatedBy() != null) {
            o.setCreatedBy(dto.getCreatedBy());
        }
        o.setId(dto.getOfferId());
        o.setOfferName(dto.getOfferName());
        o.setDescription(dto.getDescription());
        o.setDiscountValue(dto.getDiscountValue());
        o.setMinimumOrderValue(dto.getMinimumOrderValue());
        o.setStartDate(dto.getStartDate());
        o.setEndDate(dto.getEndDate());
        o.setActive(dto.getActive());
        return o;
    }
}

