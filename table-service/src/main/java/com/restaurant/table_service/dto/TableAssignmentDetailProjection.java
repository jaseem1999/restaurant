package com.restaurant.table_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableAssignmentDetailProjection {
    private Long id;
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Long tableId;
    private String tableNumber;
    private Instant assignedAt;
    private Instant vacatedAt;
    private Boolean active;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
}
