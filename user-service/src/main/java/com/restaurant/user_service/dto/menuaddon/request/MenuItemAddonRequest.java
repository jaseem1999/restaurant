package com.restaurant.user_service.dto.menuaddon.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemAddonRequest {
    private String addonName;
    private BigDecimal price;
    private Integer additionalPreparationTime;
    private Integer additionalCalories;
    private Boolean available;
    private Long menuItemId;
    private Long createdBy;
}
