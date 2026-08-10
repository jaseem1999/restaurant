package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.user.RegisterStaffRequest;
import com.restaurant.user_service.dto.user.RemoveStaffRequest;
import jakarta.validation.Valid;

public interface IRestaurantService {
    ApiResponse<String> registerRestaurantStaff(@Valid RegisterStaffRequest request);

    ApiResponse<String> removeRestaurantStaff(@Valid RemoveStaffRequest request);
}
