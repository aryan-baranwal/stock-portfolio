package com.portfolio.auth.mapper;

import com.portfolio.auth.dto.response.AuthResponseDto;
import com.portfolio.auth.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public AuthResponseDto toAuthResponse(User user, String accessToken, String refreshToken, long expiresIn) {
        return AuthResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .roles(user.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toSet()))
                .build();
    }
}
