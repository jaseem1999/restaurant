package com.restaurant.user_service.service.admin;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.login.request.LoginRequest;
import com.restaurant.user_service.dto.login.response.LoginResponse;
import com.restaurant.user_service.dto.organisation.registration.request.OrganisationRegistrationRequest;
import com.restaurant.user_service.entity.organization.Organization;
import com.restaurant.user_service.entity.user.Roles;
import com.restaurant.user_service.entity.user.UserCredential;
import com.restaurant.user_service.entity.user.UserDetails;
import com.restaurant.user_service.projection.use_credential.UserCredentialProjection;
import com.restaurant.user_service.repository.organization.OrganizationRepository;
import com.restaurant.user_service.repository.user.RoleRepository;
import com.restaurant.user_service.repository.user.UserCredentialRepository;
import com.restaurant.user_service.repository.user.UserDetailsRepository;
import com.restaurant.user_service.security.JwtAuthenticationFilter;
import com.restaurant.user_service.security.JwtTokenProvider;
import com.restaurant.user_service.security.UserPrincipal;
import com.restaurant.user_service.service.admin.cache.AdminCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService implements IAdminService {
    private final AdminCache adminCache;
    private final UserCredentialRepository userCredentialRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationRepository organizationRepository;

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());

        // Retrieve user credentials by email
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(request.getEmail()).orElse(null);

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
        log.error("User roles: {}", userCredential.getRoles());
        List<String> roles = Arrays.asList(userCredential.getRoles().split(","));
        // Create UserPrincipal for token generation
        UserPrincipal userPrincipal = new UserPrincipal(
                userCredential.getId(),
                userCredential.getEmail(),
                userCredential.getPassword(),
                roles
        );



        // Generate JWT token
        String token = jwtTokenProvider.generateToken(userPrincipal);

        // Build login response
        LoginResponse loginResponse = LoginResponse.builder()
                .token(token)
                .roles(roles)
                .build();

        log.info("User logged in successfully: {}", request.getEmail());
        return new ApiResponse<>(loginResponse, true, "Login successful", HttpStatus.OK);
    }

    @Override
    public ApiResponse<String> registerRestaurantAdmin(OrganisationRegistrationRequest request) {
        log.info("Attempting to register restaurant admin with email: {}", request.getEmail());

        // Check if user already exists
        if (userCredentialRepository.existsByEmail(request.getEmail())) {
            log.warn("User already exists with email: {}", request.getEmail());
            return new ApiResponse<>(null, false, "Email already registered", HttpStatus.CONFLICT);
        }

        // Check if organization already exists
        if (organizationRepository.existsByName(request.getOrganisationName())) {
            log.warn("Organization already exists with name: {}", request.getOrganisationName());
            return new ApiResponse<>(null, false, "Organization name already exists", HttpStatus.CONFLICT);
        }

        try {
            // Create organization
            Organization organization = Organization.builder()
                    .name(request.getOrganisationName())
                    .address(request.getOrganizationAddress())
                    .contactEmail(request.getOrganizationContactEmail())
                    .contactPhone(request.getOrganizationContactPhone())
                    .website(request.getOrganizationWebsite())
                    .type(request.getType())
                    .build();
            organization.setCreatedAt(Instant.now());
            organization.setCreatedBy(jwtAuthenticationFilter.getCurrentUserID()); // System user
            organization = organizationRepository.save(organization);

            log.info("Organization created with ID: {}", organization.getId());

            // Create user credential
            UserCredential userCredential = UserCredential.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .organizationId(organization.getId())
                    .build();
            userCredential.setCreatedAt(Instant.now());
            userCredential.setCreatedBy(jwtAuthenticationFilter.getCurrentUserID()); // System user
            userCredential = userCredentialRepository.save(userCredential);

            log.info("User credential created with ID: {}", userCredential.getId());

            // Assign RESTAURANT_OWNER role
            Roles role = Roles.builder()
                    .role("RESTAURANT_OWNER")
                    .isActive(true)
                    .description("Restaurant Owner role with permissions to manage their restaurant")
                    .userCredential(userCredential)
                    .build();
            role.setCreatedBy(jwtAuthenticationFilter.getCurrentUserID()); // System user
            role.setCreatedAt(Instant.now());
            roleRepository.save(role);

            log.info("Role assigned to user: {}", userCredential.getId());

            // Create user details
            UserDetails userDetails = UserDetails.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .designation(request.getDesignation())
                    .phoneNumber(request.getPhoneNumber())
                    .address(request.getAddress())
                    .userCredential(userCredential)
                    .build();
            userDetails.setCreatedAt(Instant.now());
            userDetails.setCreatedBy(jwtAuthenticationFilter.getCurrentUserID()); // System user
            userDetailsRepository.save(userDetails);

            log.info("User details created successfully for user: {}", userCredential.getId());
            log.info("Restaurant admin registered successfully: {}", request.getEmail());

            return new ApiResponse<>("Restaurant admin registered successfully", true, "Registration successful", HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Error registering restaurant admin: {}", e.getMessage(), e);
            return new ApiResponse<>(null, false, "Registration failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}