package com.restaurant.menu_service.projection.items;

import com.restaurant.menu_service.entity.menu.enums.FoodType;
import com.restaurant.menu_service.entity.menu.enums.ItemType;

import java.math.BigDecimal;

/**
 * Projection for MenuItem used by repository queries.
 *
 * The underlying entity uses 'id' as the primary key (inherited from BaseEntity).
 * Spring Data projections map getter names to entity properties, so we expose
 * getId() here. To keep existing code that expects getItemId(), we provide
 * a default method that delegates to getId().
 */
public interface MenuItemProjection {
    // Map directly to the entity property 'id'
    Long getId();

    // Backwards-compatible accessor used throughout the codebase
    default Long getItemId() {
        return getId();
    }

    String getItemName();
    String getDescription();
    ItemType getItemType();
    FoodType getFoodType();
    BigDecimal getBasePrice();
    Integer getPreparationTime();
    Integer getCalories();
    Long getCategoryId();
    String getImage();
    Boolean getAvailable();
    Boolean getFeatured();
    BigDecimal getTaxPercentage();
    Long getRestaurantId();
    Long getCreatedBy();
    Long getUpdatedBy();
}

