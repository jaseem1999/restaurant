package com.restaurant.menu_service.entity.menu;

import com.restaurant.menu_service.entity.BaseEntity;
import com.restaurant.menu_service.entity.menu.enums.OfferType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "menu_offer")
public class MenuOffer extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String offerName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private OfferType offerType;

    private BigDecimal discountValue;

    private BigDecimal minimumOrderValue;

    private Instant startDate;

    private Instant endDate;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private MenuItem menuItem;
}
