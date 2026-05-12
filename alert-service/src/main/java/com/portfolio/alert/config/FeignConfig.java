package com.portfolio.alert.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    // Forwards X-User-Id and X-User-Roles headers
    // that the API Gateway injects after JWT validation
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder
                            .getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                String userId = request.getHeader("X-User-Id");
                String userRoles = request.getHeader("X-User-Roles");

                if (userId != null) {
                    requestTemplate.header("X-User-Id", userId);
                }
                if (userRoles != null) {
                    requestTemplate.header("X-User-Roles", userRoles);
                }
            }
        };
    }
}