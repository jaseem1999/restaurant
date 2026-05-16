package com.restaurant.menu_service.dto.menu;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemDto {
    private Long itemId;
    private String itemName;
    private String description;
    private String itemType;
    private String foodType;
    private BigDecimal basePrice;
    private Integer preparationTime;
    private Integer calories;
    private String image;
    private Boolean available;
    private Boolean featured;
    private BigDecimal taxPercentage;
    private Long restaurantId;
    private Long categoryId;
    private Long createdBy;
    private Long updatedBy;
}

