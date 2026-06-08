package com.restaurant.user_service.dto.menuitems.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemRequest {
    private String itemName;
    private String description;
    private String itemType;
    private String foodType;
    private BigDecimal basePrice;
    private Integer preparationTime;
    private Long calories;
    private String image;
    private Boolean available;
    private Boolean featured;
    private BigDecimal taxPercentage;
    private Long restaurantId;
    private Long categoryId;
    private Long createdBy;
    private Long updatedBy;
}
