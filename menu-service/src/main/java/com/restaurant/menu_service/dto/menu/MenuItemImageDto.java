package com.restaurant.menu_service.dto.menu;

import lombok.Data;

@Data
public class MenuItemImageDto {
    private Long imageId;
    private String imageUrl;
    private String altText;
    private Integer displayOrder;
    private Boolean active;
    private Long menuItemId;
    private Long createdBy;
    private Long updatedBy;
}

