package com.restaurant.user_service.dto.login.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {
    private String token;
}
