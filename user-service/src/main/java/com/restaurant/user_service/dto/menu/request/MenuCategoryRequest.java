package com.restaurant.user_service.dto.menu.request;

import lombok.Data;

@Data
public class MenuCategoryRequest {
    private String name;
    private String description;
    private String image;
    private Integer displayOrder;
    private Boolean active;
    private Long restaurantId;
    private Long createdBy;
    private Long updatedBy;
}
