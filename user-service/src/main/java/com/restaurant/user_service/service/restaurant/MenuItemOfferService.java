package com.restaurant.user_service.service.restaurant;

import com.restaurant.user_service.client.MenuClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuoffer.request.MenuItemOfferRequest;
import com.restaurant.user_service.dto.menuoffer.request.MenuItemOfferUpdateRequest;
import com.restaurant.user_service.dto.menuoffer.response.MenuItemOfferResponse;
import com.restaurant.user_service.projection.use_credential.UserCredentialProjection;
import com.restaurant.user_service.repository.user.UserCredentialRepository;
import com.restaurant.user_service.security.JwtAuthenticationFilter;
import feign.FeignException;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuItemOfferService implements IMenuItemOfferService {
    private final MenuClient menuClient;
    private final UserCredentialRepository userCredentialRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public ApiResponse<MenuItemOfferResponse> create(MenuItemOfferRequest request) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        request.setCreatedBy(userCredential.getId());
        try {
            return menuClient.createMenuItemOffer(request);
        }catch (FeignException.NotFound _){
            log.warn("Menu item not found for menu item id: {}", request.getMenuItemId());
            return new ApiResponse<>(null,false,"Menu item not found for menu item id: "+ request.getMenuItemId(), HttpStatus.NOT_FOUND);
        }catch (FeignException e){
            log.error("Error while creating menu item offer", e);
            throw new InternalServerErrorException("Menu service error");
        }catch (Exception e){
            log.error("Error while creating menu item offer", e);
            throw new InternalServerErrorException("Error while creating menu item offer");
        }


    }

    @Override
    public ApiResponse<MenuItemOfferResponse> update(MenuItemOfferUpdateRequest request) {
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));
        request.setUpdatedBy(userCredential.getId());
        try {
            return menuClient.updateMenuItemOffer(request.getOfferId(), request);
        }catch (FeignException.NotFound _){
            log.warn("Menu item offer not found for menu item offer id: {}", request.getOfferId());
            return new ApiResponse<>(null,false,"Menu item offer not found for menu item offer id: "+ request.getOfferId(), HttpStatus.NOT_FOUND);
        }catch (FeignException e){
            log.error("Error while updating menu item offer", e);
            throw new InternalServerErrorException("Menu service error");
        }catch (Exception e){
            log.error("Error while updating menu item offer", e);
            throw new InternalServerErrorException("Error while updating menu item offer");
        }
    }

    @Override
    public ApiResponse<MenuItemOfferResponse> getOfferById(Long offerId) {
        try {
            return menuClient.getMenuItemOfferById(offerId);
        }catch (FeignException.NotFound _){
            log.warn("Menu item offer not found for menu item offer id: {}", offerId);
            return new ApiResponse<>(null,false,"Menu item offer not found for menu item offer id: "+ offerId, HttpStatus.NOT_FOUND);
        }catch (FeignException e){
            log.error("Error while retrieving menu item offer", e);
            throw new InternalServerErrorException("Menu service error");
        }catch (Exception e){
            log.error("Error while retrieving menu item offer", e);
            throw new InternalServerErrorException("Error while retrieving menu item offer");
        }
    }

    @Override
    public ApiResponse<List<MenuItemOfferResponse>> getAllOffers(long menuItemId, Boolean active) {
        try {
            return menuClient.getAllMenuItemOffers(menuItemId, active);
        }catch (FeignException.NotFound _){
            log.warn("Menu item not found for menu item id: {}", menuItemId);
            return new ApiResponse<>(null,false,"Menu item not found for menu item id: "+ menuItemId, HttpStatus.NOT_FOUND);
        }catch (FeignException e){
            log.error("Error while retrieving menu item offers", e);
            throw new InternalServerErrorException("Menu service error");
        }catch (Exception e){
            log.error("Error while retrieving menu item offers", e);
            throw new InternalServerErrorException("Error while retrieving menu item offers");
        }
    }

    @Override
    public ApiResponse<String> deleteOffer(Long offerId) {
        try {
            com.restaurant.user_service.dto.ApiResponse<String> response =menuClient.deleteMenuItemOffer(offerId);
            if (response.getMessage().equalsIgnoreCase("NOT_FOUND")) {
                log.warn("Menu item offer not found for menu item offer id: {}", offerId);
                return new ApiResponse<>(null, false, "Menu item offer not found for menu item offer id: " + offerId, HttpStatus.NOT_FOUND);
            }
            return new ApiResponse<>(null, true, "Menu item offer deleted successfully", HttpStatus.OK);
        } catch (FeignException.NotFound _) {
            log.warn("Menu item offer not found for menu item offer id: {}", offerId);
            return new ApiResponse<>(null, false, "Menu item offer not found for menu item offer id: " + offerId, HttpStatus.NOT_FOUND);
        } catch (FeignException e) {
            log.error("Error while deleting menu item offer", e);
            throw new InternalServerErrorException("Menu service error");
        } catch (Exception e) {
            log.error("Error while deleting menu item offer", e);
            throw new InternalServerErrorException("Error while deleting menu item offer");
        }
    }
}
