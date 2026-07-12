package com.restaurant.table_service.entity.table;

import com.restaurant.table_service.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@ToString
@jakarta.persistence.Table(name = "table_assignment")
public class TableAssignment extends BaseEntity {

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long restaurantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @Column(nullable = false, updatable = false)
    private Instant assignedAt;

    private Instant vacatedAt;

    private Boolean active = true;

    private String notes;
}
