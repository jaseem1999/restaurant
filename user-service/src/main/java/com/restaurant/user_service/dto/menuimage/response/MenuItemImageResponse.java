package com.restaurant.user_service.dto.menuimage.response;

import lombok.Data;

@Data
public class MenuItemImageResponse {
    private Long imageId;
    private String imageUrl;
    private String altText;
    private Integer displayOrder;
    private Boolean active;
    private Long menuItemId;
    private Long createdBy;
    private Long updatedBy;
}
