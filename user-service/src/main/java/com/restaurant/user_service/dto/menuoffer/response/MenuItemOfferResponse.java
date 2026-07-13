package com.restaurant.user_service.dto.menuoffer.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class MenuItemOfferResponse {
    private Long offerId;
    private String offerName;
    private String description;
    private String offerType;
    private BigDecimal discountValue;
    private BigDecimal minimumOrderValue;
    private Instant startDate;
    private Instant endDate;
    private Boolean active;
    private Long menuItemId;
    private Long createdBy;
    private Long updatedBy;
}
