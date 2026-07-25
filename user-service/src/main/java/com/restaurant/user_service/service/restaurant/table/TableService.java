package com.restaurant.user_service.service.restaurant.table;

import com.restaurant.user_service.client.TableClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.table.request.TableRequest;
import com.restaurant.user_service.dto.table.response.TableResponse;
import com.restaurant.user_service.projection.use_credential.UserCredentialProjection;
import com.restaurant.user_service.repository.user.UserCredentialRepository;
import com.restaurant.user_service.security.JwtAuthenticationFilter;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableService implements ITableService {
    private final UserCredentialRepository userCredentialRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TableClient tableClient;

    @Override
    public ApiResponse<TableResponse> createTable(TableRequest tableRequest) {
        log.info("Creating table with request: {}", tableRequest);
        UserCredentialProjection userCredential = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()
                ));

        try {
            tableRequest.setCreatedBy(userCredential.getId());
            tableRequest.setUpdatedBy(null);
            tableRequest.setRestaurantId(userCredential.getRestaurantId());
            return tableClient.createTable(tableRequest);
        }catch (FeignException.FeignClientException e){
            log.error("Feign client exception while creating table with request: {}", tableRequest, e);
            return new ApiResponse<>(
                    null,
                    false,
                    "Failed to create table: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        catch (Exception e){
            log.error("Exception while creating table with request: {}", tableRequest, e);
            return new ApiResponse<>(
                    null,
                    false,
                    "Exception while creating table : " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
