package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.client.MenuClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuimage.request.MenuItemImageRequest;
import com.restaurant.user_service.dto.menuimage.request.MenuItemImageUpdateRequest;
import com.restaurant.user_service.dto.menuimage.response.MenuItemImageResponse;
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
@Slf4j
@RequiredArgsConstructor
public class MenuImageService implements IMenuImageService{
    private final MenuClient menuClient;
    private final UserCredentialRepository userCredentialRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public ApiResponse<MenuItemImageResponse> createItemImage(MenuItemImageRequest menuItemImageRequest) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        menuItemImageRequest.setCreatedBy(userCredential.getId());

        try {
            return menuClient.createMenuItemImage(menuItemImageRequest);
        }catch (FeignException.NotFound e) {
            log.error("Menu item not found for ID: {}", menuItemImageRequest.getMenuItemId());
            return new ApiResponse<>(null,
                    false,
                    "Menu item not found for ID: " + menuItemImageRequest.getMenuItemId(),
                    HttpStatus.NOT_FOUND
            );
        }catch (FeignException e){
            log.error("Error from Menu Service");
            log.error("Error while creating menu item image", e);
            throw new RuntimeException("Error while creating menu item image: " + e.getMessage(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ApiResponse<List<MenuItemImageResponse>> listByMenuItem(Long menuItemId) {
        try {
            return menuClient.listMenuItemImages(menuItemId);
        }catch (FeignException.NotFound e) {
            log.error("Menu item not found for ID: {}", menuItemId);
            return new ApiResponse<>(null, false, "Menu item images not found for ID: " + menuItemId, HttpStatus.NOT_FOUND);
        }catch (FeignException e){
            log.error("Error from Menu Service");
            log.error("Error while listing menu item images", e);
            throw new RuntimeException("Error while listing menu item images: " + e.getMessage(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponse<MenuItemImageResponse> updateItemImage(MenuItemImageUpdateRequest request) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        request.setUpdatedBy(userCredential.getId());

        try {
            return menuClient.updateMenuItemImage(request.getImageId(), request);
        }catch (FeignException.NotFound e) {
            log.error("Menu item image not found for ID: {}", request.getImageId());
            return new ApiResponse<>(null, false, "Menu item image not found for ID: " + request.getImageId(), HttpStatus.NOT_FOUND);
        }catch (FeignException e){
            log.error("Error from Menu Service");
            log.error("Error while updating menu item image", e);
            throw new RuntimeException("Error while updating menu item image: " + e.getMessage(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponse<MenuItemImageResponse> getItemImageById(Long id) {
        try {
            return menuClient.getMenuItemImageById(id);
        }catch (FeignException.NotFound e) {
            log.error("Menu item image not found for ID: {}", id);
            return new ApiResponse<>(null, false, "Menu item image not found for ID: " + id, HttpStatus.NOT_FOUND);
        }catch (FeignException e){
            log.error("Error from Menu Service");
            log.error("Error while retrieving menu item image", e);
            throw new RuntimeException("Error while retrieving menu item image: " + e.getMessage(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponse<String> deleteItemImage(Long id) {
        try {
            com.restaurant.user_service.dto.ApiResponse response =menuClient.deleteMenuItemImage(id);
            if (response.getMessage().equalsIgnoreCase("NOT_FOUND"))
                return new ApiResponse<>(null, false, "Menu item image not found for ID: " + id, HttpStatus.NOT_FOUND);
            return new ApiResponse<>(null, true, "Menu item image deleted successfully", HttpStatus.OK);
        }catch (FeignException.NotFound e) {
            log.error("Menu item image not found for ID: {}", id);
            return new ApiResponse<>(null, false, "Menu item image not found for ID: " + id, HttpStatus.NOT_FOUND);
        }catch (FeignException e){
            log.error("Error from Menu Service");
            log.error("Error while deleting menu item image", e);
            throw new RuntimeException("Error while deleting menu item image: " + e.getMessage(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
