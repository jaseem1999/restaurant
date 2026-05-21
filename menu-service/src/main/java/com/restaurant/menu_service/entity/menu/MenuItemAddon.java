package com.restaurant.menu_service.entity.menu;

import com.restaurant.menu_service.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "menu_item_addon")
public class MenuItemAddon extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String addonName;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    private Integer additionalPreparationTime;

    private Integer additionalCalories;

    @Column(nullable = false)
    private Boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private MenuItem menuItem;
}
