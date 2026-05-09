package com.restaurant.user_service.service.admin;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.login.request.LoginRequest;
import com.restaurant.user_service.dto.login.response.LoginResponse;
import com.restaurant.user_service.entity.user.Roles;
import com.restaurant.user_service.entity.user.UserCredential;
import com.restaurant.user_service.projection.use_credential.UserCredentialProjection;
import com.restaurant.user_service.repository.organization.OrganizationRepository;
import com.restaurant.user_service.repository.user.RoleRepository;
import com.restaurant.user_service.repository.user.UserCredentialRepository;
import com.restaurant.user_service.repository.user.UserDetailsRepository;
import com.restaurant.user_service.security.JwtTokenProvider;
import com.restaurant.user_service.security.UserPrincipal;
import com.restaurant.user_service.service.admin.cache.AdminCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService implements IAdminService {
    private final AdminCache adminCache;
    private final UserCredentialRepository userCredentialRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationRepository organizationRepository;

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());
        
        // Retrieve user credentials by email
        UserCredentialProjection userCredential = userCredentialRepository.findByEmailProjection(request.getEmail());
        
        if (userCredential == null) {
            log.warn("User not found with email: {}", request.getEmail());
            return new ApiResponse<>(null, false, "Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
        
        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), userCredential.getPassword())) {
            log.warn("Invalid password for email: {}", request.getEmail());
            return new ApiResponse<>(null, false, "Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
        
        // Extract roles from user credential
        List<String> roles = userCredential.getRoles();
        
        // Create UserPrincipal for token generation
        UserPrincipal userPrincipal = new UserPrincipal(
            userCredential.getId() != null ? userCredential.getId() : 0,
            userCredential.getEmail(),
            userCredential.getPassword(),
            roles
        );
        
        // Generate JWT token
        String token = jwtTokenProvider.generateToken(userPrincipal);
        
        // Build login response
        LoginResponse loginResponse = LoginResponse.builder()
            .token(token)
            .build();
        
        log.info("User logged in successfully: {}", request.getEmail());
        return new ApiResponse<>(loginResponse, true, "Login successful", HttpStatus.OK);
    }
}
