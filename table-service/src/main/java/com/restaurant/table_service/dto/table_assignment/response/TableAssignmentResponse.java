package com.restaurant.table_service.dto.table_assignment.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class TableAssignmentResponse {
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
