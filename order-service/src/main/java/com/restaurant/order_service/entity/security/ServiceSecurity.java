package com.restaurant.order_service.entity.security;

import com.restaurant.order_service.entity.BaseEntity;
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
    private String serviceName = "order_service";
    private String serviceUsername;
    private String servicePassword;
}
