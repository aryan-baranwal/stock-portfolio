package com.portfolio.auth.service;

import com.portfolio.auth.entity.User;

import java.util.List;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String extractEmail(String token);

    Long extractUserId(String token);

    String extractJti(String token);

    boolean isTokenValid(String token);

    long getAccessTokenExpiry();


    List<String> extractRoles(String token);
}