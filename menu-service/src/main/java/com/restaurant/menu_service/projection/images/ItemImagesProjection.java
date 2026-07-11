package com.restaurant.menu_service.projection.images;

public interface ItemImagesProjection {

    Long getMenuItemId();
    Long getId();
    String getImageUrl();
    String getAltText();
    Integer getDisplayOrder();
    Boolean getActive();

}

