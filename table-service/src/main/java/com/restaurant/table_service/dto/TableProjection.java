package com.restaurant.table_service.dto;

import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.entity.table.enums.TableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableProjection {
    private Long id;
    private String tableNumber;
    private Integer capacity;
    private TableStatus status;
    private TableType tableType;
    private String location;
    private String floor;
    private String section;
    private Boolean active;
    private Long createdBy;
    private Instant createdAt;
    private Long updatedBy;
    private Instant updatedAt;
    private Long restaurantId;
    private String notes;
}
