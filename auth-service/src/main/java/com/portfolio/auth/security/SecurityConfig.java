package com.portfolio.auth.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // 🔴 Disable CSRF for stateless APIs
                .csrf(csrf -> csrf.disable())

                // 🔴 Stateless session (JWT based system)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 🔥 Prevent HTML login redirect (important for APIs + Swagger)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, authException) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                )

                // ================= AUTH RULES =================
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // Auth APIs
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/refresh",

                                // 🔥 OAuth2 endpoints (IMPORTANT FIX)
                                "/oauth2/**",
                                "/login/oauth2/**",

                                // Swagger
                                "/swagger-ui/**",
                                "/v3/api-docs/**",

                                // Actuator
                                "/actuator/**"
                        ).permitAll()

                        // Admin APIs
                        .requestMatchers("/api/auth/admin/**").hasRole("ADMIN")

                        // Everything else secured
                        .anyRequest().authenticated()
                )

                // ================= OAUTH2 LOGIN =================
                .oauth2Login(oauth2 -> oauth2
                        // where login starts
                        .authorizationEndpoint(auth ->
                                auth.baseUri("/oauth2/authorization")
                        )

                        // callback URL handler
                        .redirectionEndpoint(redir ->
                                redir.baseUri("/login/oauth2/code/*")
                        )

                        // custom success handler (JWT generation happens here)
                        .successHandler(oAuth2SuccessHandler)
                )

                // ================= JWT FILTER =================
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}