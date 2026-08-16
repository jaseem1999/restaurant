package com.restaurant.table_service.security;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class TableFeignConfigSecurity {
    @Value("${service.security.table.username}")
    private String username;

    @Value("${service.security.table.password}")
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
