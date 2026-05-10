package com.restaurant.payment_service.repository;


import com.restaurant.payment_service.entity.security.ServiceSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentServiceSecurityRepository extends JpaRepository<ServiceSecurity,Long> {
    Optional<ServiceSecurity> findByServiceName(String s);
}