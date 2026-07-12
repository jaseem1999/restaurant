package com.restaurant.user_service.dto.menuvariant.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemVariantUpdateRequest {
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
