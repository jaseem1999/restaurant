package com.restaurant.menu_service.entity.menu;

import com.restaurant.menu_service.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "menu_category")
public class MenuCategory extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;

    private Integer displayOrder = 0;


    private Boolean active = true;


    @Column(nullable = false)
    private Long restaurantId;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;
}
