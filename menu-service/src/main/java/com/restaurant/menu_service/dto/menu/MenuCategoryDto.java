package com.restaurant.menu_service.dto.menu;

import lombok.Data;

@Data
public class MenuCategoryDto {
    private Long categoryId;
    private String name;
    private String description;
    private String image;
    private Integer displayOrder;
    private Boolean active;
    private Long restaurantId;
    private Long createdBy;
    private Long updatedBy;
}

