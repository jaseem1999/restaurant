package com.restaurant.menu_service.security;

import com.restaurant.menu_service.entity.security.ServiceSecurity;
import com.restaurant.menu_service.repository.ServiceSecurityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * SecurityCheckApisClass is responsible for validating API credentials for external services.
 *
 * Developed by: Jaseem
 * Updated by:
 * Tested by:
 * Time verified by: 2026-05-16
 *
 * Description:
 * This class provides a method to check if the provided username and password match the stored credentials
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityCheckApisClass {

    private final ServiceSecurityRepository securityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public boolean checkApi(String authorizationHeader) {
        log.info("Checking API credentials for menu service");
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return false;
        }

        // Expecting header in form: "Basic base64(username:password)"
        if (!authorizationHeader.startsWith("Basic ")) {
            return false;
        }

        try {
            String base64 = authorizationHeader.substring(6).trim();
            byte[] decoded = java.util.Base64.getDecoder().decode(base64);
            String credentials = new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);
            if (parts.length != 2) {
                return false;
            }
            String username = parts[0];
            String password = parts[1];

            // load stored credentials for this service (menu_service)
            ServiceSecurity security = securityRepository.findByServiceName("menu_service").orElse(null);
            if (security == null) return false;

            String storedUsername = security.getServiceUsername();
            String storedPassword = security.getServicePassword();

            if (storedUsername == null || storedPassword == null) return false;

            if (!storedUsername.equals(username)) return false;

            return passwordEncoder.matches(password, storedPassword);
        } catch (IllegalArgumentException ex) {
            // base64 decode error
            return false;
        }
    }
}
