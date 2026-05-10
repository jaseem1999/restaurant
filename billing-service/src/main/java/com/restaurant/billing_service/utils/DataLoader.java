package com.restaurant.billing_service.utils;

import com.restaurant.billing_service.entity.security.ServiceSecurity;
import com.restaurant.billing_service.repository.ServiceSecurityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ServiceSecurityRepository billingServiceSecurityRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${service.security.username}")
    private String username;

    @Value("${service.security.password}")
    private String password;

    @Override
    public void run(String... args) throws Exception {

        if (username == null || password == null) {
            throw new RuntimeException("User service credentials are not set in the application properties.");
        }

        ServiceSecurity serviceSecurity = billingServiceSecurityRepository.findByServiceName("billing_service").orElse(null);
        if (serviceSecurity == null) {
            serviceSecurity = ServiceSecurity.builder().serviceName("billing_service").
                    serviceUsername(username)
                    .servicePassword(passwordEncoder.encode(password)).build();
            serviceSecurity.setCreatedBy(0l);
            serviceSecurity.setCreatedAt(Instant.now());
            billingServiceSecurityRepository.save(serviceSecurity);
            log.info("Initial login credentials for menu-service have been created.");
        } else {
            log.info("Login credentials for menu-service already exist. Skipping initialization.");
        }
    }
}