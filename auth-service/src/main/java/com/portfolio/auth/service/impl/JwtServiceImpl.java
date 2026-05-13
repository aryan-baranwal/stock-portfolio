package com.portfolio.auth.service.impl;

import com.portfolio.auth.entity.User;
import com.portfolio.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiry}")
    private long accessTokenExpiry;

    @Value("${jwt.refresh-token-expiry}")
    private long refreshTokenExpiry;

    // ================= KEY =================
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ================= ACCESS TOKEN =================
    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles",
                        user.getRoles().stream()
                                .map(r -> r.getName().name())
                                .toList()
                )
                .issuer("portfolio-auth-service")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
                .signWith(getSigningKey())
                .compact();
    }

    // ================= REFRESH TOKEN =================
    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer("portfolio-auth-service")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiry))
                .signWith(getSigningKey())
                .compact();
    }

    // ================= CLAIM EXTRACTION =================
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String extractEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    @Override
    public Long extractUserId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    @Override
    public String extractJti(String token) {
        return getClaims(token).getId();
    }

    // ================= ROLE EXTRACTION (CRITICAL FIX) =================
    @Override
    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token);

        List<?> roles = claims.get("roles", List.class);

        if (roles == null) return List.of();

        return roles.stream()
                .map(Object::toString)
                .toList();
    }

    // ================= VALIDATION =================
    @Override
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            log.warn("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public long getAccessTokenExpiry() {
        return accessTokenExpiry;
    }
}