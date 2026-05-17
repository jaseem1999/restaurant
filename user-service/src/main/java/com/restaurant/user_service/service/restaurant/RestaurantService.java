package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.user.RegisterStaffRequest;
import com.restaurant.user_service.dto.user.RemoveStaffRequest;
import com.restaurant.user_service.entity.user.Roles;
import com.restaurant.user_service.entity.user.UserCredential;
import com.restaurant.user_service.entity.user.UserDetails;
import com.restaurant.user_service.repository.user.RoleRepository;
import com.restaurant.user_service.repository.user.UserCredentialRepository;
import com.restaurant.user_service.repository.user.UserDetailsRepository;
import com.restaurant.user_service.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService implements IRestaurantService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final RoleRepository roleRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<String> registerRestaurantStaff(RegisterStaffRequest request) {
        log.info("Attempting to register restaurant staff with email: {}", request.getEmail());

        // Get current user's organization (restaurant owner)
        Long restaurantOwnerId = jwtAuthenticationFilter.getCurrentUserID();
        UserCredential owner = userCredentialRepository.findById(restaurantOwnerId).orElse(null);

        if (owner == null) {
            log.warn("Current user not found with ID: {}", restaurantOwnerId);
            return new ApiResponse<>(null, false, "Current user not found", HttpStatus.UNAUTHORIZED);
        }

        // Check if staff email already exists
        if (userCredentialRepository.existsByEmail(request.getEmail())) {
            log.warn("Staff member already exists with email: {}", request.getEmail());
            return new ApiResponse<>(null, false, "Email already registered", HttpStatus.CONFLICT);
        }

        try {
            // Create user credential for staff
            UserCredential staffCredential = UserCredential.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .organizationId(owner.getOrganizationId())
                    .build();
            staffCredential.setCreatedAt(Instant.now());
            staffCredential.setCreatedBy(restaurantOwnerId);
            staffCredential = userCredentialRepository.save(staffCredential);

            log.info("Staff credential created with ID: {}", staffCredential.getId());

            // Assign role to staff
            Roles role = Roles.builder()
                    .role(request.getRole())
                    .isActive(true)
                    .description("Staff role with permissions for " + request.getRole())
                    .userCredential(staffCredential)
                    .build();
            role.setCreatedBy(restaurantOwnerId);
            role.setCreatedAt(Instant.now());
            roleRepository.save(role);

            log.info("Role assigned to staff: {}", staffCredential.getId());

            // Create staff details
            UserDetails staffDetails = UserDetails.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .designation(request.getDesignation())
                    .phoneNumber(request.getPhoneNumber())
                    .address(request.getAddress())
                    .userCredential(staffCredential)
                    .build();
            staffDetails.setCreatedAt(Instant.now());
            staffDetails.setCreatedBy(restaurantOwnerId);
            userDetailsRepository.save(staffDetails);

            log.info("Staff details created successfully for staff member: {}", staffCredential.getId());
            log.info("Restaurant staff registered successfully: {}", request.getEmail());

            return new ApiResponse<>("Restaurant staff registered successfully", true, "Staff registration successful", HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Error registering restaurant staff: {}", e.getMessage(), e);
            return new ApiResponse<>(null, false, "Staff registration failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ApiResponse<String> removeRestaurantStaff(RemoveStaffRequest request) {
        log.info("Attempting to remove restaurant staff with email: {}", request.getEmail());

        // Get current user's organization (restaurant owner)
        Long restaurantOwnerId = jwtAuthenticationFilter.getCurrentUserID();
        UserCredential owner = userCredentialRepository.findById(restaurantOwnerId).orElse(null);

        if (owner == null) {
            log.warn("Current user not found with ID: {}", restaurantOwnerId);
            return new ApiResponse<>(null, false, "Current user not found", HttpStatus.UNAUTHORIZED);
        }

        // Find the staff member to remove
        UserCredential staffToRemove = userCredentialRepository.findUserCredentialByEmail(request.getEmail())
                .map(projection -> userCredentialRepository.findById(projection.getId()).orElse(null))
                .orElse(null);

        if (staffToRemove == null) {
            log.warn("Staff member not found with email: {}", request.getEmail());
            return new ApiResponse<>(null, false, "Staff member not found", HttpStatus.NOT_FOUND);
        }

        // Check if staff belongs to the same organization
        if (!staffToRemove.getOrganizationId().equals(owner.getOrganizationId())) {
            log.warn("Staff member {} does not belong to the same organization as owner {}", request.getEmail(), owner.getEmail());
            return new ApiResponse<>(null, false, "Staff member does not belong to your restaurant", HttpStatus.FORBIDDEN);
        }

        // Prevent owner from removing themselves
        if (staffToRemove.getId().equals(restaurantOwnerId)) {
            log.warn("Owner {} attempted to remove themselves", owner.getEmail());
            return new ApiResponse<>(null, false, "You cannot remove yourself", HttpStatus.BAD_REQUEST);
        }

        try {
            // Get user details for the staff member
            UserDetails staffDetails = userDetailsRepository.findById(staffToRemove.getId()).orElse(null);

            // Delete in proper order due to foreign key constraints
            if (staffDetails != null) {
                userDetailsRepository.delete(staffDetails);
                log.info("User details deleted for staff member: {}", staffToRemove.getId());
            }

            // Delete roles associated with the user
            List<Roles> roles = roleRepository.findAll().stream()
                    .filter(role -> role.getUserCredential() != null && role.getUserCredential().getId().equals(staffToRemove.getId()))
                    .toList();

            for (Roles role : roles) {
                roleRepository.delete(role);
                log.info("Role deleted for staff member: {}", staffToRemove.getId());
            }

            // Finally delete the user credential
            userCredentialRepository.delete(staffToRemove);
            log.info("User credential deleted for staff member: {}", staffToRemove.getId());

            log.info("Restaurant staff removed successfully: {}", request.getEmail());
            return new ApiResponse<>("Restaurant staff removed successfully", true, "Staff removal successful", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error removing restaurant staff: {}", e.getMessage(), e);
            return new ApiResponse<>(null, false, "Staff removal failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
