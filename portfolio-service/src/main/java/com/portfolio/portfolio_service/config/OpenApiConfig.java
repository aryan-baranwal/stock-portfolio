package com.portfolio.portfolio_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title(
                                        "Portfolio Service API"
                                )
                                .version("1.0")
                                .description(
                                        "Microservice for portfolio and holdings management"
                                )
                                .contact(
                                        new Contact()
                                                .name("Aman Raj")
                                                .email("aman@example.com")
                                )
                );
    }
}