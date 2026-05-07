package com.restaurant.user_service.entity.user;

import com.restaurant.user_service.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Roles extends BaseEntity {
    private String role;
    private String description;
    private Boolean isActive;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserCredential userCredential;
}
