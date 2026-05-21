package com.restaurant.user_service.projection.use_credential;

public interface UserCredentialProjection {
    Long getId();

    String getEmail();

    String getPassword();

    String getRoles();

    Long getRestaurantId();
}
