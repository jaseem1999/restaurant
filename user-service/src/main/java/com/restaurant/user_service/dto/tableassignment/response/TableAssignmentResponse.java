package com.restaurant.user_service.dto.tableassignment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
