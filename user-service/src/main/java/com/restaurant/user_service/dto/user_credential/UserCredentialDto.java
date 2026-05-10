package com.restaurant.user_service.dto.user_credential;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredentialDto {
    private Long id;
    private String email;
    private String password;
    private List<String> roles;
}

