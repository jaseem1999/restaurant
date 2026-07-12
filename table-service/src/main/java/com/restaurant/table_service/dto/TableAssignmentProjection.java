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
public class TableAssignmentProjection {
    private Long id;
    private Long orderId;
    private Long customerId;
    private Long tableId;
    private String tableNumber;
    private Instant assignedAt;
    private Instant vacatedAt;
    private Boolean active;
}
