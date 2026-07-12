package com.restaurant.user_service.dto.menuimage.request;

import lombok.Data;

@Data
public class MenuItemImageUpdateRequest {
    private Long imageId;
    private String imageUrl;
    private String altText;
    private Integer displayOrder;
    private Boolean active;
    private Long menuItemId;
    private Long createdBy;
    private Long updatedBy;
}
