package com.restaurant.table_service.dto.table_assignment.request;

import lombok.Data;

import java.time.Instant;

@Data
public class TableAssignmentRequest {
    private Long id;
    private Instant createdAt;
    private Long createdBy;
    private Long updatedBy;
    private Instant updatedAt;
    private Instant assignedAt;
    private Instant vacatedAt;
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Long tableId;
    private String notes;
}
