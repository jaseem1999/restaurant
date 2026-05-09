package com.restaurant.user_service.dto.login.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginRequest {
    @Email
    private String email;
    private String password;
}
