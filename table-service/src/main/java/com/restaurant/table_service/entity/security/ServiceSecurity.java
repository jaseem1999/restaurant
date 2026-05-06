package com.restaurant.table_service.entity.security;

import com.restaurant.table_service.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ServiceSecurity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String serviceName = "table_service";
    private String serviceUsername;
    private String servicePassword;
}
