package com.restaurant.menu_service.projection.menu.response;

/**
 * Projection for MenuCategory used by repository queries.
 *
 * The underlying entity uses 'id' as the primary key (inherited from BaseEntity).
 * Spring Data projections map getter names to entity properties, so we expose
 * getId() here. To keep existing code that expects getCategoryId(), we provide
 * a default method that delegates to getId().
 */
public interface MenuCategoryProjection {
    // Map directly to the entity property 'id'
    Long getId();

    // Backwards-compatible accessor used throughout the codebase
    default Long getCategoryId() {
        return getId();
    }

    String getName();
    String getDescription();
    String getImage();
    Integer getDisplayOrder();
    Boolean getActive();
    Long getRestaurantId();
    Long getCreatedBy();
    Long getUpdatedBy();
}
