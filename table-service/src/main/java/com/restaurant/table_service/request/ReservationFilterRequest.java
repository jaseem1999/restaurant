package com.restaurant.table_service.request;

import com.restaurant.table_service.entity.table.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationFilterRequest {
    private Long restaurantId;
    private Long customerId;
    private ReservationStatus status;
    private LocalDateTime reservationDateFrom;
    private LocalDateTime reservationDateTo;
    private Boolean noShow;
    private Integer pageNumber = 0;
    private Integer pageSize = 20;
}
