package com.restaurant.user_service.service.restaurant.menu;

import com.restaurant.user_service.client.MenuClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuaddon.request.MenuItemAddonRequest;
import com.restaurant.user_service.dto.menuaddon.request.MenuItemAddonUpdateRequest;
import com.restaurant.user_service.dto.menuaddon.response.MenuItemAddonResponse;
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
public class MenuAddonService implements IMenuAddonService{
    private final MenuClient menuClient;
    private final UserCredentialRepository userCredentialRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public ApiResponse<MenuItemAddonResponse> add(MenuItemAddonRequest request) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));

        request.setCreatedBy(userCredential.getId());
        try {
            return menuClient.addAddon(request);
        }catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu item not found  id" ,
                    HttpStatus.NOT_FOUND
            );

        }
        catch (FeignException ex) {
            log.error("Feign error while saving menu add on : {}", ex.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex) {
            log.error("Error saving menu add on: {}", ex.getMessage());
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to save addon: " + ex.getMessage(),
                    null
            );
        }

    }

    @Override
    public ApiResponse<List<MenuItemAddonResponse>> getByItemId(Long itemId) {
        try {
            return menuClient.addOnGetByItemId(itemId);
        }catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu add on not found this item id" ,
                    HttpStatus.NOT_FOUND
            );

        }
        catch (FeignException ex) {
            log.error("Feign error while saving menu item: {}", ex.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex) {
            log.error("Error menu add on: {}", ex.getMessage());
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to save addon: " + ex.getMessage(),
                    null
            );
        }
    }

    @Override
    public ApiResponse<MenuItemAddonResponse> getById(Long id) {
        try {
            return menuClient.addOnGetById(id);
        }catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Menu add on not found this id" ,
                    HttpStatus.NOT_FOUND
            );

        }
        catch (FeignException ex) {
            log.error("Feign error while saving menu add on: {}", ex.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex) {
            log.error("Error menu add on: {}", ex.getMessage());
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to save addon: " + ex.getMessage(),
                    null
            );
        }
    }

    @Override
    public ApiResponse<MenuItemAddonResponse> update(MenuItemAddonUpdateRequest request) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));

        request.setUpdatedBy(userCredential.getId());
        try {
            return menuClient.addOnUpdate(request.getAddonId(), request);
        }catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Add on not found this id" ,
                    HttpStatus.NOT_FOUND
            );

        }
        catch (FeignException ex) {
            log.error("Feign error while saving menu add on : {}", ex.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex) {
            log.error("Error saving menu add on: {}", ex.getMessage());
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to update addon: " + ex.getMessage(),
                    null
            );
        }
    }

    @Override
    public ApiResponse<Void> delete(Long id) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        try {
            ApiResponse<Void> response=menuClient.addOnDelete(userCredential.getRestaurantId(), id);
            if ("NOT_FOUND".equals(response.getMessage())){
                return new ApiResponse<>(
                        null,
                        false,
                        "Add on not found this id" ,
                        HttpStatus.NOT_FOUND
                );
            }
            if ("RID".equals(response.getMessage())){
                return new ApiResponse<>(
                        null,
                        false,
                        "Restaurant id mismatch" ,
                        HttpStatus.FORBIDDEN
                );
            }
            return new ApiResponse<>(
                    null,
                    true,
                    "Deleted successful" ,
                    HttpStatus.OK
            );
        }catch (FeignException.NotFound ex) {

            return new ApiResponse<>(
                    null,
                    false,
                    "Add on not found this id" ,
                    HttpStatus.NOT_FOUND
            );

        }catch (FeignException.NotAcceptable ex){
            return new ApiResponse<>(
                    null,
                    false,
                    "Deletion not acceptable plz check menu server logs" ,
                    HttpStatus.NOT_FOUND
            );
        }
        catch (FeignException ex) {
            log.error("Feign error while saving menu add on : {}", ex.getMessage());
            throw new RuntimeException("Menu service error");
        }
        catch (Exception ex) {
            log.error("Error delete menu add on: {}", ex.getMessage());
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to delete addon: " + ex.getMessage(),
                    null
            );
        }
    }
}
