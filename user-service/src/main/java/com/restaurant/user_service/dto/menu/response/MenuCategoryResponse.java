package com.restaurant.user_service.dto.menu.response;

import lombok.Data;

@Data
public class MenuCategoryResponse {
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
