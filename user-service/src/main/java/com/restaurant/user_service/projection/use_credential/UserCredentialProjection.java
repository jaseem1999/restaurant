package com.restaurant.user_service.projection.use_credential;

import java.util.List;

public interface UserCredentialProjection {
    Long getId();
    String getEmail();
    String getPassword();
    List<String> getRoles();
}
