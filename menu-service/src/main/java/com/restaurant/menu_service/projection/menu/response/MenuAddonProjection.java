package com.restaurant.menu_service.projection.menu.response;

import java.math.BigDecimal;

public interface MenuAddonProjection {
    Long getAddonId();
    String getAddonName();
    BigDecimal getPrice();
    Integer getAdditionalPreparationTime();
    Integer getAdditionalCalories();
    Boolean getAvailable();
    Long getMenuItemId();
    Long getCreatedBy();
    Long getUpdatedBy();
}
