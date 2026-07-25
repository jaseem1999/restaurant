package com.restaurant.table_service.entity.table;

import com.restaurant.table_service.entity.BaseEntity;
import com.restaurant.table_service.entity.table.enums.ReservationStatus;
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
@jakarta.persistence.Table(name = "table_reservation")
public class TableReservation extends BaseEntity {

    @Column(nullable = false)
    private Instant reservationDateTime;

    @Column(nullable = false)
    private Integer guestCount;

    @Column(nullable = false)
    private Long customerId;

    private String guestName;

    private String guestPhone;

    private String guestEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(nullable = false)
    private Long restaurantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @Column(columnDefinition = "TEXT")
    private String specialRequests;

    private Instant checkInDateTime;

    private Instant checkOutDateTime;

    private Boolean noShow = false;
}
