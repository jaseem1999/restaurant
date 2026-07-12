package com.restaurant.table_service.request;

import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.entity.table.enums.TableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableFilterRequest {
    private Long restaurantId;
    private TableStatus status;
    private TableType tableType;
    private Integer capacity;
    private String floor;
    private String section;
    private Boolean active;
    private Integer pageNumber = 0;
    private Integer pageSize = 20;
}
