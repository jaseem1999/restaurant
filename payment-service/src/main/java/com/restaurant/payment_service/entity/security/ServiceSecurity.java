package com.restaurant.payment_service.entity.security;

import com.restaurant.payment_service.entity.BaseEntity;
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
    private String serviceName = "payment_service";
    private String serviceUsername;
    private String servicePassword;
}