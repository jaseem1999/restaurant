package com.restaurant.user_service.controller.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.user.RegisterStaffRequest;
import com.restaurant.user_service.dto.user.RemoveStaffRequest;
import com.restaurant.user_service.service.admin.IAdminService;
import com.restaurant.user_service.service.restaurant.IRestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant")
@Validated
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {

    private final IAdminService adminService;
    private final IRestaurantService restaurantService;

    @PostMapping(path = "/register-admin", consumes = "application/json")
    public ResponseEntity<ApiResponse<String>> registerRestaurantAdmin() {
        log.info("Endpoint called: register-admin");
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(null, false, "Use /api/auth/register-restaurant for restaurant registration", null));
    }

    @PostMapping(path = "/register-staff", consumes = "application/json")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<ApiResponse<String>> registerRestaurantStaff(
            @Valid @RequestBody RegisterStaffRequest request) {
        log.info("Registering restaurant staff with email: {}", request.getEmail());
        ApiResponse<String> response = restaurantService.registerRestaurantStaff(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping(path = "/remove-staff", consumes = "application/json")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<ApiResponse<String>> removeRestaurantStaff(
            @Valid @RequestBody RemoveStaffRequest request) {
        log.info("Removing restaurant staff with email: {}", request.getEmail());
        ApiResponse<String> response = restaurantService.removeRestaurantStaff(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
