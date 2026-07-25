package com.restaurant.user_service.dto.table.response;

import com.restaurant.user_service.dto.table.enums.TableStatus;
import com.restaurant.user_service.dto.table.enums.TableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private String tableNumber;
    private Integer capacity;
    private TableStatus tableStatus;
    private TableType tableType;
    private Long restaurantId;
    private String location;
    private String floor;
    private String section;
    private Boolean active;
    private String notes;
}
