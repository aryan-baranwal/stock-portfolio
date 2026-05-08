package com.portfolio.auth.controller;

import com.portfolio.auth.dto.*;
import com.portfolio.auth.dto.request.LoginRequestDto;
import com.portfolio.auth.dto.request.RefreshTokenRequestDto;
import com.portfolio.auth.dto.request.RegisterRequestDto;
import com.portfolio.auth.dto.response.ApiResponse;
import com.portfolio.auth.dto.response.AuthResponseDto;
import com.portfolio.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@Valid @RequestBody RegisterRequestDto request) {
        log.info("Register request for email: {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @Operation(summary = "Login with email and password")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        log.info("Login request for email: {}", request.getEmail());
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto>> refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @Operation(summary = "Logout - invalidates token")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.logout(authHeader));
    }

    @Operation(summary = "Get current user info")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> me(
            org.springframework.security.core.Authentication authentication
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(email));
    }

    @Operation(summary = "Assign role to user (ADMIN only)")
    @PostMapping("/admin/assign-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignRole(@Valid @RequestBody RoleAssignmentDto request) {
        return ResponseEntity.ok(authService.assignRole(request));
    }
}