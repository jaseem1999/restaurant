package com.restaurant.user_service.security;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


/**
 * Developed by: Jaseem
 * Updated by:
 * Tested by: Jaseem
 * stage: completed
 * Time verified by: 2026-05-16
 * Description:
 * Configuration class for Feign client to add Basic Authentication header when communicating with the Menu Service.
 * It reads the username and password from application properties, encodes them in Base64, and adds the Authorization header to each outgoing request made by the Feign client.
 */
@Configuration
@Slf4j
public class MenuFeignConfigSecurity {

    @Value("${service.security.menu.username}")
    private String username;

    @Value("${service.security.menu.password}")
    private String password;

    @Bean
    public RequestInterceptor emailAuthInterceptor() {

        return requestTemplate -> {

            // username:password
            String auth = username + ":" + password;

            // Base64 encode
            String encodedAuth = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            // Create Basic Auth header
            String authHeader = "Basic " + encodedAuth;

            // Add Authorization header
            requestTemplate.header("Authorization", authHeader);

            log.info("Authorization header added for Menu Service");
        };
    }
}