package com.restaurant.user_service.service.restaurant.menu;

import com.restaurant.user_service.client.MenuClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuvariant.request.MenuItemVariantRequest;
import com.restaurant.user_service.dto.menuvariant.request.MenuItemVariantUpdateRequest;
import com.restaurant.user_service.dto.menuvariant.response.MenuItemVariantResponse;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuItemVariantService implements IMenuItemVariantServices {
    private final MenuClient menuClient;
    private final UserCredentialRepository userCredentialRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public ApiResponse<MenuItemVariantResponse> createMenuItemVariant(MenuItemVariantRequest request) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        request.setCreatedBy(userCredential.getId());
        try {
            return menuClient.createMenuItemVariant(request);
        } catch (FeignException.NotFound _) {
            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item not found for menu item id: " + request.getMenuItemId(),
                    null
            );
        } catch (FeignException e) {
            log.error("Feign error while creating menu item variant: {}", e.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex){
            log.error("Unexpected error while creating menu item variant: {}", ex.getMessage());
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public ApiResponse<MenuItemVariantResponse> getMenuItemVariantById(Long id) {
        try {
            return menuClient.getMenuItemVariantById(id);
        } catch (FeignException.NotFound e) {
            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item variant not found for id: " + id,
                    null
            );
        } catch (FeignException e) {
            log.error("Feign error while retrieving menu item variant: {}", e.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex){
            log.error("Unexpected error while retrieving menu item variant: {}", ex.getMessage());
            throw new RuntimeException("Unexpected error");
        }
    }

    public ApiResponse<MenuItemVariantResponse> updateMenuItemVariant(Long id, MenuItemVariantUpdateRequest request) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        request.setUpdatedBy(userCredential.getId());
        try {
            return menuClient.updateMenuItemVariant(id, request);
        } catch (FeignException.NotFound e) {
            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item variant not found for id: " + id,
                    null
            );
        } catch (FeignException e) {
            log.error("Feign error while updating menu item variant: {}", e.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex){
            log.error("Unexpected error while updating menu item variant: {}", ex.getMessage());
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public ApiResponse<List<MenuItemVariantResponse>> getMenuItemVariantByItemId(Long menuItemId) {
        try {
            return menuClient.getMenuItemVariantsByMenuItemId(menuItemId);
        } catch (FeignException.NotFound e) {
            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item variant not found for menu item id: " + menuItemId,
                    null
            );
        } catch (FeignException e) {
            log.error("Feign error while retrieving menu item variants by menu item id: {}", e.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex){
            log.error("Unexpected error while retrieving menu item variants by menu item id: {}", ex.getMessage());
            throw new RuntimeException("Unexpected error");
        }
    }

    @Override
    public ApiResponse<String> deleteMenuItemVariantById(Long id) {
        try {
            ApiResponse<String> response=menuClient.deleteMenuItemVariantById(id);
            if (response.getMessage().equalsIgnoreCase("NOT_FOUND"))
                return new ApiResponse<>(
                        null,
                        false,
                        "Menu item variant not found for id: " + id,
                        HttpStatus.NOT_FOUND
                );
            return response;
        } catch (FeignException.NotFound e) {
            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item variant not found for id: " + id,
                    null
            );
        } catch (FeignException e) {
            log.error("Feign error while deleting menu item variant: {}", e.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex){
            log.error("Unexpected error while deleting menu item variant: {}", ex.getMessage());
            throw new RuntimeException("Unexpected error");
        }
    }


}
