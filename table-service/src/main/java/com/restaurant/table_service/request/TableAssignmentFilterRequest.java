package com.restaurant.table_service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableAssignmentFilterRequest {
    private Long restaurantId;
    private Long customerId;
    private Boolean active;
    private LocalDateTime assignedFromDate;
    private LocalDateTime assignedToDate;
    private Integer pageNumber = 0;
    private Integer pageSize = 20;
}
