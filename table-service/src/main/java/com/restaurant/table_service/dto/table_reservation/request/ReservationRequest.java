package com.restaurant.table_service.dto.table_reservation.request;

import com.restaurant.table_service.entity.table.enums.ReservationStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class ReservationRequest {
    private Instant reservationDateTime;
    private Integer guestCount;
    private Integer customerCount;
    private Long customerId; // null acceptable
    private String guestName;
    private String guestPhone;
    private String guestEmail;
    private ReservationStatus status;
    private Long restaurantId;
    private Long tableId;
    private String specialRequests;
    private Instant checkInDateTime;
    private Instant checkOutDateTime;
    private Boolean noShow;
    private Instant createdAt;
    private Instant updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private Long reservationId; // for update, null acceptable
}
