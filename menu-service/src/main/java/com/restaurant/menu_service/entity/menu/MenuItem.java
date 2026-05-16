package com.restaurant.menu_service.entity.menu;


import com.restaurant.menu_service.entity.BaseEntity;
import com.restaurant.menu_service.entity.menu.enums.FoodType;
import com.restaurant.menu_service.entity.menu.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "menu_item")
public class MenuItem extends BaseEntity {


    @Column(nullable = false, length = 150)
    private String itemName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @Column(nullable = false)
    private BigDecimal basePrice;

    private Integer preparationTime;

    private Integer calories;

    private String image;


    private Boolean available = true;


    private Boolean featured = false;


    private BigDecimal taxPercentage = BigDecimal.ZERO;


    @Column(nullable = false)
    private Long restaurantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private MenuCategory category;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<MenuItemVariant> variants;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<MenuItemAddon> addons;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<MenuItemImage> images;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<MenuOffer> offers;


}