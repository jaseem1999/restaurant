package com.restaurant.user_service.dto.table.request;


import com.restaurant.user_service.dto.table.enums.TableStatus;
import com.restaurant.user_service.dto.table.enums.TableType;
import lombok.Data;

import java.time.Instant;

@Data
public class TableRequest {
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
