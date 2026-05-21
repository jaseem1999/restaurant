package com.restaurant.menu_service.dto.menu;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemAddonDto {
    private Long addonId;
    private String addonName;
    private BigDecimal price;
    private Integer additionalPreparationTime;
    private Integer additionalCalories;
    private Boolean available;
    private Long menuItemId;
    private Long createdBy;
    private Long updatedBy;
}

