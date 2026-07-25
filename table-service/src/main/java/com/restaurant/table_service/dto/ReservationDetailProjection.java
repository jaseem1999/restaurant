package com.restaurant.table_service.dto;

import com.restaurant.table_service.entity.table.enums.ReservationStatus;
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
public class ReservationDetailProjection {
    private Long id;
    private Instant reservationDateTime;
    private Integer guestCount;
    private Long customerId;
    private String guestName;
    private String guestPhone;
    private String guestEmail;
    private ReservationStatus status;
    private Long restaurantId;
    private Long tableId;
    private String tableNumber;
    private String specialRequests;
    private Instant checkInDateTime;
    private Instant checkOutDateTime;
    private Boolean noShow;
    private Instant createdAt;
    private Instant updatedAt;
}
