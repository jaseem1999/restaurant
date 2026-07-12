package com.restaurant.user_service.dto.menuitems.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemUpdateRequest {
    @Min(1)
    private Long itemId;
    private String itemName;
    private String description;
    @Pattern(regexp = "FOOD|BEVERAGE|DESSERT|APPETIZER", message = "Invalid item type. Must be one of: FOOD, BEVERAGE, DESSERT, APPETIZER")
    private String itemType;
    @Pattern(regexp = "VEGETARIAN|NON_VEGETARIAN|VEGAN|GLUTEN_FREE", message = "Invalid food type. Must be one of: VEGETARIAN, NON_VEGETARIAN, VEGAN, GLUTEN_FREE")
    private String foodType;
    private BigDecimal basePrice;
    private Integer preparationTime;
    private Long calories;
    private String image;
    private Boolean available;
    private Boolean featured;
    private BigDecimal taxPercentage;
}
