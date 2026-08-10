package com.restaurant.user_service.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterStaffRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password should be between 8 and 100 characters")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9\\-+()\\s]{10,}$", message = "Phone number should be valid")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotEmpty
    private List<String> roles;
}

