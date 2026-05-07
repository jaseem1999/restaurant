package com.restaurant.user_service.entity.organization;

import com.restaurant.user_service.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Organization extends BaseEntity {
    private String name;
    private String address;
    private String contactEmail;
    private String contactPhone;
    private String website;
    private OrganizationType type;
    public enum OrganizationType {
        RESTAURANT,
        HOTEL,
        CATERING_SERVICE,
        FOOD_DELIVERY_SERVICE,
        ADMINISTRATIVE_OFFICE,
        OTHER
    }
}
