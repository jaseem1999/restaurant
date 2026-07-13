package com.restaurant.user_service.controller.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuoffer.request.MenuItemOfferRequest;
import com.restaurant.user_service.dto.menuoffer.request.MenuItemOfferUpdateRequest;
import com.restaurant.user_service.dto.menuoffer.response.MenuItemOfferResponse;
import com.restaurant.user_service.service.restaurant.IMenuItemOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/menu/menu-item-offers")
@Validated
@RequiredArgsConstructor
@Slf4j
public class MenuItemOfferController {
    private final IMenuItemOfferService iMenuItemOfferService;

    @PostMapping(path = "create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<MenuItemOfferResponse>> createOffer(@Valid @RequestBody MenuItemOfferRequest request, BindingResult bindingResult){
        ApiResponse<MenuItemOfferResponse> response = iMenuItemOfferService.create(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping(path = "update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<MenuItemOfferResponse>> update(@Valid @RequestBody MenuItemOfferUpdateRequest request, BindingResult bindingResult){
        ApiResponse<MenuItemOfferResponse> response = iMenuItemOfferService.update(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(path = "get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<MenuItemOfferResponse>> getOfferById(@PathVariable("id") Long offerId){
        // Assuming you have a method in your service to get an offer by ID
        ApiResponse<MenuItemOfferResponse> response = iMenuItemOfferService.getOfferById(offerId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping(path = "get-all/{menuItemId}/{active}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<MenuItemOfferResponse>>> getAllOffers(
            @PathVariable long menuItemId,
            @PathVariable Boolean active
    ){
        ApiResponse<List<MenuItemOfferResponse>> response = iMenuItemOfferService.getAllOffers(menuItemId, active);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping(path = "delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> deleteOffer(@PathVariable("id") Long offerId) {
        // Assuming you have a method in your service to delete an offer by ID
        ApiResponse<String> response = iMenuItemOfferService.deleteOffer(offerId);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
