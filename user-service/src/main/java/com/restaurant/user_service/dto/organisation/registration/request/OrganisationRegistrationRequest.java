package com.restaurant.user_service.dto.organisation.registration.request;

import com.restaurant.user_service.entity.organization.Organization;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganisationRegistrationRequest {
    
    @NotBlank(message = "Organisation name is required")
    private String organisationName;
    
    @NotBlank(message = "Organization address is required")
    private String organizationAddress;
    
    @NotBlank(message = "Organization contact email is required")
    @Email(message = "Organization contact email should be valid")
    private String organizationContactEmail;
    
    @NotBlank(message = "Organization contact phone is required")
    @Pattern(regexp = "^[0-9\\-+()\\s]{10,}$", message = "Organization contact phone should be valid")
    private String organizationContactPhone;
    
    @NotBlank(message = "Organization website is required")
    @Pattern(regexp = "^(https?://).*", message = "Organization website should start with http:// or https://")
    private String organizationWebsite;
    
    @NotNull(message = "Organization type is required")
    private Organization.OrganizationType type;

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
    
    public enum OrganizationType {
        RESTAURANT,
        HOTEL,
        CATERING_SERVICE,
        FOOD_DELIVERY_SERVICE,
        ADMINISTRATIVE_OFFICE,
        OTHER
    }

}
