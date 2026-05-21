package com.restaurant.menu_service.controller.menu;

import com.restaurant.menu_service.dto.ApiResponse;
import com.restaurant.menu_service.dto.menu.MenuOfferDto;
import com.restaurant.menu_service.entity.menu.MenuOffer;
import com.restaurant.menu_service.service.menu.MenuOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/menu/offers")
@RequiredArgsConstructor
public class MenuOfferController {
    private final MenuOfferService service;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuOfferDto>> create(@RequestBody MenuOfferDto dto) {
        MenuOffer created = service.create(fromDto(dto));
        return new ResponseEntity<>(new ApiResponse<>(toDto(created), true, "Created", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuOfferDto>> getById(@PathVariable Long id) {
        MenuOffer offer = service.getById(id);
        if (offer == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(offer), true, "OK", HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<java.util.List<MenuOfferDto>>> listByMenuItem(@RequestParam(required = false) Long menuItemId,
                                                                                  @RequestParam(required = false) Boolean active) {
        java.util.List<MenuOffer> offers;
        if (menuItemId != null && Boolean.TRUE.equals(active)) {
            offers = service.listActiveByMenuItem(menuItemId);
        } else if (menuItemId != null) {
            offers = service.listByMenuItem(menuItemId);
        } else {
            offers = service.listActiveBetween(LocalDateTime.now().minusYears(10), LocalDateTime.now().plusYears(10));
        }
        java.util.List<MenuOfferDto> dtos = offers.stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(dtos, true, "OK", HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuOfferDto>> update(@PathVariable Long id, @RequestBody MenuOfferDto dto) {
        MenuOffer updated = service.update(id, fromDto(dto));
        if (updated == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, false, "Not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ApiResponse<>(toDto(updated), true, "Updated", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, true, "Deleted", HttpStatus.NO_CONTENT));
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

