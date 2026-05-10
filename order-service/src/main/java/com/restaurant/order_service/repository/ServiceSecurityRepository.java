package com.restaurant.order_service.repository;

import com.restaurant.order_service.entity.security.ServiceSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceSecurityRepository extends JpaRepository<ServiceSecurity,Long> {
    Optional<ServiceSecurity> findByServiceName(String s);
}