package com.restaurant.user_service.service.restaurant.menu;

import com.restaurant.user_service.client.MenuClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuitems.request.MenuItemRequest;
import com.restaurant.user_service.dto.menuitems.request.MenuItemUpdateRequest;
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
    public ApiResponse<List<MenuItemsResponse>> getMenuItemsByRestaurant() {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        try {
            return menuClient.getMenuItemsByRestaurant(userCredential.getRestaurantId());
        }catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item not found for restaurant id: " + userCredential.getRestaurantId(),
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

    @Override
    public ApiResponse<MenuItemsResponse> getMenuItemsById(Long id) {


        try {
            return menuClient.getMenuItemsById(id);
        } catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item not found this id: " + id,
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

    @Override
    public ApiResponse<MenuItemsResponse> update(MenuItemUpdateRequest request) {

        try {
            return menuClient.updateMenuItem(request.getItemId(),request);
        } catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item not found this id: " + request.getItemId(),
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

    @Override
    public ApiResponse<Void> deleteMenuItem(Long itemId) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        try {
            return menuClient.deleteMenuItem(itemId,userCredential.getRestaurantId());
        } catch (FeignException.NotFound ex) {
            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item not found this id: " + itemId + " for restaurant id: " + userCredential.getRestaurantId(),
                    HttpStatus.NOT_FOUND
            );

        } catch (FeignException ex) {
            throw new RuntimeException("Menu service error");
        } catch (Exception ex) {
            log.error("Error deleting menu item: {}", ex.getMessage());
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to delete menu item: " + ex.getMessage(),
                    null
            );
        }
    }


}
