package com.portfolio.auth.service;

import com.portfolio.auth.dto.*;
import com.portfolio.auth.dto.request.LoginRequestDto;
import com.portfolio.auth.dto.request.RefreshTokenRequestDto;
import com.portfolio.auth.dto.request.RegisterRequestDto;
import com.portfolio.auth.dto.response.ApiResponse;
import com.portfolio.auth.dto.response.AuthResponseDto;

public interface AuthService {
    ApiResponse<AuthResponseDto> register(RegisterRequestDto request);
    ApiResponse<AuthResponseDto> login(LoginRequestDto request);
    ApiResponse<AuthResponseDto> refreshToken(RefreshTokenRequestDto request);
    ApiResponse<Void> logout(String authHeader);
    ApiResponse<Void> assignRole(RoleAssignmentDto request);
}