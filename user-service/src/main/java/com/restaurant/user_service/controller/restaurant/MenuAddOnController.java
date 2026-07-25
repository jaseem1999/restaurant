package com.restaurant.user_service.controller.restaurant;

import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.menuaddon.request.MenuItemAddonRequest;
import com.restaurant.user_service.dto.menuaddon.request.MenuItemAddonUpdateRequest;
import com.restaurant.user_service.dto.menuaddon.response.MenuItemAddonResponse;
import com.restaurant.user_service.service.restaurant.menu.IMenuAddonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/restaurant/menu/addon")
@Validated
@RestController
@RequiredArgsConstructor
public class MenuAddOnController {
    private final IMenuAddonService iMenuAddonService;

    @PostMapping(path = "add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<MenuItemAddonResponse>> addAddon(
            @RequestBody MenuItemAddonRequest request
            ){
        ApiResponse<MenuItemAddonResponse> response = iMenuAddonService.add(request);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping(path = "item/{id}")
    public ResponseEntity<ApiResponse<List<MenuItemAddonResponse>>> getByItemId(
            @PathVariable("id") @Min(1) @Valid Long itemId
    ){
        ApiResponse<List<MenuItemAddonResponse>> response = iMenuAddonService.getByItemId(itemId);
        return new ResponseEntity<>(response,response.getStatus());
    }
    @GetMapping(path = "{id}")
    public ResponseEntity<ApiResponse<MenuItemAddonResponse>> getById(
            @PathVariable("id") @Min(1) @Valid Long id
    ){
        ApiResponse<MenuItemAddonResponse> response = iMenuAddonService.getById(id);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PutMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<MenuItemAddonResponse>> update(@Valid
                                                                         @RequestBody MenuItemAddonUpdateRequest request){
        ApiResponse<MenuItemAddonResponse> response= iMenuAddonService.update(request);
        return new ResponseEntity<>(response,response.getStatus());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ){
        ApiResponse<Void> response = iMenuAddonService.delete(id);
        return new ResponseEntity<>(response,response.getStatus());
    }

}
