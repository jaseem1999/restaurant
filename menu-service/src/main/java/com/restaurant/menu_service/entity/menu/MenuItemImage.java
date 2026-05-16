package com.restaurant.menu_service.entity.menu;

import com.restaurant.menu_service.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "menu_item_image")
public class MenuItemImage extends BaseEntity {

    @Column(nullable = false)
    private String imageUrl;

    private String altText;

    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private MenuItem menuItem;
}
