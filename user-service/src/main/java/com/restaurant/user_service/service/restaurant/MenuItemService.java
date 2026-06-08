package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.client.MenuClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuitems.request.MenuItemRequest;
import com.restaurant.user_service.dto.menuitems.response.MenuItemsResponse;
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
public class MenuItemService implements IMenuItemService {

    private final MenuClient menuClient;
    private final UserCredentialRepository userCredentialRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public ApiResponse<MenuItemsResponse> saveMenuItem(MenuItemRequest request) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        try {
            request.setCreatedBy(userCredential.getId());
            request.setCategoryId(request.getCategoryId());
            request.setRestaurantId(userCredential.getRestaurantId());
            return menuClient.saveMenuItem(request);
        }catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item not found for category id: " ,
                    HttpStatus.NOT_FOUND
            );

        } catch (FeignException ex) {
            log.error("Feign error while saving menu item: {}", ex.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex) {
            log.error("Error saving menu item: {}", ex.getMessage());
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to save menu item: " + ex.getMessage(),
                    null
            );
        }
    }

    @Override
    public ApiResponse<List<MenuItemsResponse>> getMenuItemsByCategory(Long categoryId) {
        try {
            return menuClient.getMenuItemsByCategory(categoryId);
        }catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item not found for category id: " + categoryId,
                    HttpStatus.NOT_FOUND
            );

        } catch (FeignException ex) {
            throw new RuntimeException("Menu service error");
        } catch (Exception ex) {
            log.error("Error fetching menu items by category: {}", ex.getMessage());
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to fetch menu items: " + ex.getMessage(),
                    null
            );
        }
    }

    @Override
    public ApiResponse<List<MenuItemsResponse>> getMenuItemsByRestaurant(Long restaurantId) {
        try {
            return menuClient.getMenuItemsByRestaurant(restaurantId);
        }catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item not found for restaurant id: " + restaurantId,
                    HttpStatus.NOT_FOUND
            );

        } catch (FeignException ex) {
            throw new RuntimeException("Menu service error");
        } catch (Exception ex) {
            log.error("Error fetching menu items by Restaurant: {}", ex.getMessage());
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to fetch menu items: " + ex.getMessage(),
                    null
            );
        }

    }
}
