package com.restaurant.table_service.dto;

import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.entity.table.enums.TableType;
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
public class TableDetailProjection {
    private Long id;
    private String tableNumber;
    private Integer capacity;
    private TableStatus status;
    private TableType tableType;
    private Long restaurantId;
    private String location;
    private String floor;
    private String section;
    private Boolean active;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
}
