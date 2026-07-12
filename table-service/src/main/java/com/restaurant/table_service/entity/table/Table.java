package com.restaurant.table_service.entity.table;

import com.restaurant.table_service.entity.BaseEntity;
import com.restaurant.table_service.entity.table.enums.TableStatus;
import com.restaurant.table_service.entity.table.enums.TableType;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@ToString
@jakarta.persistence.Table(name = "restaurant_table")
public class Table extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String tableNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status = TableStatus.AVAILABLE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableType tableType = TableType.REGULAR;

    @Column(nullable = false)
    private Long restaurantId;

    private String location;

    private String floor;

    private String section;

    private Boolean active = true;

    private String notes;
}
