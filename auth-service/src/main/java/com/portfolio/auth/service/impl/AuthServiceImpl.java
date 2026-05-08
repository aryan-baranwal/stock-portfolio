package com.portfolio.auth.service.impl;

import com.portfolio.auth.dto.RoleAssignmentDto;
import com.portfolio.auth.dto.request.LoginRequestDto;
import com.portfolio.auth.dto.request.RefreshTokenRequestDto;
import com.portfolio.auth.dto.request.RegisterRequestDto;
import com.portfolio.auth.dto.response.ApiResponse;
import com.portfolio.auth.dto.response.AuthResponseDto;
import com.portfolio.auth.entity.RefreshToken;
import com.portfolio.auth.entity.Role;
import com.portfolio.auth.entity.User;
import com.portfolio.auth.enums.AuthProvider;
import com.portfolio.auth.enums.RoleEnum;
import com.portfolio.auth.exception.AuthException;
import com.portfolio.auth.messaging.UserEventPublisher;
import com.portfolio.auth.repository.RefreshTokenRepository;
import com.portfolio.auth.repository.RoleRepository;
import com.portfolio.auth.repository.UserRepository;
import com.portfolio.auth.service.AuthService;
import com.portfolio.auth.service.JwtService;
import com.portfolio.auth.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher userEventPublisher;

    @Override
    @Transactional
    public ApiResponse<AuthResponseDto> register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email already registered");
        }

        // Fetch USER role from DB (inserted by data.sql)
        Role userRole = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new AuthException("Default role not found. Check data.sql"));

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .provider(AuthProvider.LOCAL)
                .build();

        // Assign role before saving
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);
        log.info("User registered: {}", savedUser.getEmail());

        userEventPublisher.publishUserRegistered(savedUser);

        String accessToken = jwtService.generateAccessToken(savedUser);
        String refreshToken = saveRefreshToken(savedUser);

        return ApiResponse.created(buildAuthResponse(savedUser, accessToken, refreshToken));
    }

    @Override
    public ApiResponse<AuthResponseDto> login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Invalid credentials");
        }

        if (!user.getIsActive()) {
            throw new AuthException("Account is deactivated");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = saveRefreshToken(user);

        log.info("User logged in: {}", user.getEmail());

        return ApiResponse.success(buildAuthResponse(user, accessToken, refreshToken));
    }

    @Override
    @Transactional
    public ApiResponse<AuthResponseDto> refreshToken(RefreshTokenRequestDto request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new AuthException("Invalid refresh token"));

        if (storedToken.getIsRevoked() || storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AuthException("Refresh token expired or revoked");
        }

        storedToken.setIsRevoked(true);
        refreshTokenRepository.save(storedToken);

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new AuthException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = saveRefreshToken(user);

        return ApiResponse.success(buildAuthResponse(user, newAccessToken, newRefreshToken));
    }

    @Override
    @Transactional
    public ApiResponse<Void> logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException("Invalid authorization header");
        }

        String token = authHeader.substring(7);
        String jti = jwtService.extractJti(token);
        Long userId = jwtService.extractUserId(token);

        tokenBlacklistService.blacklistToken(jti, jwtService.getAccessTokenExpiry());
        refreshTokenRepository.deleteAllByUserId(userId);

        log.info("User logged out, token blacklisted jti: {}", jti);

        return ApiResponse.success(null);
    }

    @Override
    @Transactional
    public ApiResponse<Void> assignRole(RoleAssignmentDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AuthException("User not found"));

        // Fetch role from DB instead of creating a new one
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new AuthException("Role not found: " + request.getRole()));

        user.getRoles().add(role);
        userRepository.save(user);

        log.info("Role {} assigned to user {}", request.getRole(), request.getUserId());

        return ApiResponse.success(null);
    }

    private String saveRefreshToken(User user) {
        String token = jwtService.generateRefreshToken(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(token)
                .expiryDate(LocalDateTime.now().plusSeconds(604800))
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    private AuthResponseDto buildAuthResponse(User user, String accessToken, String refreshToken) {
        Set<String> roleNames = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpiry())
                .userId(user.getId())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }
}