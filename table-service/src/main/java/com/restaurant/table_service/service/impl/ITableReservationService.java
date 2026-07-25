package com.restaurant.table_service.service.impl;

import java.time.Instant;

public interface ITableReservationService {
    default boolean isTableReserved(Long tableId, Instant assignedAt, Instant vacatedAt) {
        return false;
    }
}
