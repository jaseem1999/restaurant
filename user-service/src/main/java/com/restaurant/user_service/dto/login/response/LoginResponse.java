package com.restaurant.user_service.dto.login.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class LoginResponse {
    private String token;
    private List<String> roles;
}
