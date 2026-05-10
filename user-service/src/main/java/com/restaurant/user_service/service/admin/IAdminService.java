package com.restaurant.user_service.service.admin;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.login.request.LoginRequest;
import com.restaurant.user_service.dto.login.response.LoginResponse;
import com.restaurant.user_service.dto.organisation.registration.request.OrganisationRegistrationRequest;
import jakarta.validation.Valid;

public interface IAdminService {
    ApiResponse<LoginResponse> login(@Valid LoginRequest request);

    ApiResponse registerRestaurantAdmin(@Valid OrganisationRegistrationRequest request);
}
