package com.restaurant.user_service.dto.tableassignment.request;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;


@Data
public class TableAssignmentRequest {
    private Long id;
    private Instant createdAt;
    private Long createdBy;
    private Long updatedBy;
    private Instant updatedAt;
    @NonNull
    private Instant assignedAt;
    private Instant vacatedAt;
    @Min(1L)
    private Long orderId;
    private Long customerId;
    @Min(1L)
    private Long restaurantId;
    @Min(1L)
    private Long tableId;
    private String notes;
}
