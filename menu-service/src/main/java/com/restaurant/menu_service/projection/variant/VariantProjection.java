package com.restaurant.menu_service.projection.variant;

import java.math.BigDecimal;

public interface VariantProjection {
    Long getVariantId();
    String getVariantName();
    BigDecimal getPriceAdjustment();
    Integer getAdditionalPreparationTime();
    Integer getAdditionalCalories();
    Boolean getAvailable();
    Long getMenuItemId();
    Long getCreatedBy();
    Long getUpdatedBy();
}
