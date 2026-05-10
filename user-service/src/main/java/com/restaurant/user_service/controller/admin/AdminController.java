package com.restaurant.user_service.controller.admin;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.login.request.LoginRequest;
import com.restaurant.user_service.dto.login.response.LoginResponse;
import com.restaurant.user_service.service.admin.IAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Validated
@RequiredArgsConstructor
public class AdminController {

    private final IAdminService adminService;

    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request){
        ApiResponse<LoginResponse> response = adminService.login(request);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
