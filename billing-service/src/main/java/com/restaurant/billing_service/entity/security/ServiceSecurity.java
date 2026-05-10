package com.restaurant.billing_service.entity.security;

import com.restaurant.billing_service.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ServiceSecurity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String serviceName = "billing_service";
    private String serviceUsername;
    private String servicePassword;
}
