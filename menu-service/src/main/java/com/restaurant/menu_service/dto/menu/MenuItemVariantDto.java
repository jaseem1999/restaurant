package com.restaurant.menu_service.dto.menu;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemVariantDto {
    private Long variantId;
    private String variantName;
    private BigDecimal priceAdjustment;
    private Integer additionalPreparationTime;
    private Integer additionalCalories;
    private Boolean available;
    private Long menuItemId;
    private Long createdBy;
    private Long updatedBy;
}

