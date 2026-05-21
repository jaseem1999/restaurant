package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.client.MenuClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menu.request.MenuCategoryRequest;
import com.restaurant.user_service.dto.menu.request.MenuCategoryUpdateRequest;
import com.restaurant.user_service.dto.menu.response.MenuCategoryResponse;
import com.restaurant.user_service.exception.ResourceNotFoundException;
import com.restaurant.user_service.projection.use_credential.UserCredentialProjection;
import com.restaurant.user_service.repository.user.UserCredentialRepository;
import com.restaurant.user_service.security.JwtAuthenticationFilter;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService implements IMenuCategoryService {

    private final MenuClient menuClient;
    private final UserCredentialRepository userCredentialRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public ApiResponse<MenuCategoryResponse> createMenuCategory(MenuCategoryRequest request) {

        try {
            return menuClient.createMenuCategory(request);

        } catch (FeignException ex) {
            throw new RuntimeException("Failed to create menu category");
        }
    }

    @Override
    public ApiResponse<List<MenuCategoryResponse>> getMenuCategoriesByRestaurantId() {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        try {

            return menuClient.getMenuCategoriesByRestaurantId(userCredential.getRestaurantId());

        } catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu categories not found for restaurant id: " + userCredential.getRestaurantId(),
                    HttpStatus.NOT_FOUND
            );

        } catch (FeignException ex) {
            throw new RuntimeException("Menu service error");
        }
    }

    @Override
    public ApiResponse<MenuCategoryResponse> getMenuCategoryById(Long categoryId) {

        try {
            return menuClient.getMenuCategoryById(categoryId);

        } catch (FeignException.NotFound ex) {
            return new ApiResponse<>(
                    null,
                    false,
                    "Menu category not found with id: " + categoryId,
                    HttpStatus.NOT_FOUND
            );

        } catch (FeignException ex) {
            throw new RuntimeException("Menu service error");
        }
    }

    @Override
    public ApiResponse<MenuCategoryResponse> updateMenuCategory(
            MenuCategoryUpdateRequest request
    ) {

        UserCredentialProjection credentialProjection =
                userCredentialRepository
                        .findUserCredentialByEmail(
                                jwtAuthenticationFilter.getCurrentUserEmail()
                        )
                        .orElseThrow(
                                () -> new AuthenticationCredentialsNotFoundException(
                                        "User credentials not found for email: "
                                                + jwtAuthenticationFilter.getCurrentUserEmail()
                                )
                        );

        request.setUpdatedBy(credentialProjection.getId());

        try {

            return menuClient.updateMenuCategory(
                    request.getCategoryId(),
                    request
            );

        } catch (FeignException.NotFound ex) {
            log.warn("Menu category not found with id: {}", request.getCategoryId());
            return new ApiResponse<>(
                    null,
                    false,
                    "Menu category not found with id: " + request.getCategoryId(),
                    HttpStatus.NOT_FOUND
            );
        } catch (FeignException ex) {

            throw new RuntimeException("Failed to update menu category");
        }
    }

    @Override
    public ApiResponse<Void> deleteMenuCategory(Long categoryId) {

        try {

            return menuClient.deleteMenuCategory(categoryId);

        } catch (FeignException.NotFound ex) {
            log.warn("Menu category not found with id: {}", categoryId);
            return new ApiResponse<>(
                    null,
                    false,
                    "Menu category not found with id: " + categoryId,
                    HttpStatus.NOT_FOUND
            );

        } catch (FeignException ex) {

            throw new RuntimeException("Failed to delete menu category");
        }
    }
}