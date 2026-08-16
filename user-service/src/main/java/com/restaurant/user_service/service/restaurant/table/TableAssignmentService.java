package com.restaurant.user_service.service.restaurant.table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restaurant.user_service.client.TableClient;
import com.restaurant.user_service.dto.ApiResponse;
import com.restaurant.user_service.dto.tableassignment.request.TableAssignmentRequest;
import com.restaurant.user_service.dto.tableassignment.response.TableAssignmentResponse;
import com.restaurant.user_service.repository.user.UserCredentialRepository;
import com.restaurant.user_service.security.JwtAuthenticationFilter;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class TableAssignmentService implements ITableAssignmentService {
    private final UserCredentialRepository userCredentialRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TableClient tableClient;
    @Override
    public ApiResponse<TableAssignmentResponse> createTableAssignment(TableAssignmentRequest request) {
        log.info("Creating table assignment with request: {}", request);
        try {
            Long userId = userCredentialRepository.findUserCredentialByEmail(jwtAuthenticationFilter.getCurrentUserEmail())
                    .orElseThrow(() -> new RuntimeException("User credentials not found for email: " + jwtAuthenticationFilter.getCurrentUserEmail()))
                    .getId();
            request.setCreatedBy(userId);
            request.setUpdatedBy(null);
            ApiResponse<TableAssignmentResponse> response= tableClient.createTableAssignment(request);
            return response;
        }catch (FeignException.NotFound e) {


            String message = "Failed to create table assignment";

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(e.contentUTF8());

            if (jsonNode.has("message")) {
                message = jsonNode.get("message").asText();
            }

            return new ApiResponse<>(
                    null,
                    false,
                    message,
                    org.springframework.http.HttpStatus.NOT_FOUND
            );
        }
        catch (FeignException.NotAcceptable e) {

            String message = "Failed to create table assignment";

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(e.contentUTF8());

            if (jsonNode.has("message")) {
                message = jsonNode.get("message").asText();
            }

            return new ApiResponse<>(
                    null,
                    false,
                    message,
                    org.springframework.http.HttpStatus.NOT_ACCEPTABLE
            );
        }
        catch (Exception e) {
            log.error("Exception while creating table assignment with request: {}", request, e);
            return new ApiResponse<>(
                    null,
                    false,
                    "Exception while creating table assignment : " + e.getMessage(),
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
