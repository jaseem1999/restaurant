package com.restaurant.menu_service.projection.offer;

import com.restaurant.menu_service.entity.menu.MenuItem;
import com.restaurant.menu_service.entity.menu.enums.OfferType;


import java.math.BigDecimal;
import java.time.Instant;

public interface MenuOfferProjection {
    Long getId();
    String getOfferName();
    String getDescription();
    OfferType getOfferType();
    BigDecimal getDiscountValue();
    BigDecimal getMinimumOrderValue();
    Instant getStartDate();
    Instant getEndDate();
    Boolean getActive();
    Long getMenuItemId();
}
