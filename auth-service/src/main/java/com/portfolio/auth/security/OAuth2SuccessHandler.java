package com.portfolio.auth.security;

import com.portfolio.auth.entity.RefreshToken;
import com.portfolio.auth.entity.User;
import com.portfolio.auth.enums.AuthProvider;
import com.portfolio.auth.messaging.UserEventPublisher;
import com.portfolio.auth.repository.RefreshTokenRepository;
import com.portfolio.auth.repository.UserRepository;
import com.portfolio.auth.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserEventPublisher userEventPublisher;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getName();

        AuthProvider provider = request.getRequestURI().contains("google")
                ? AuthProvider.GOOGLE : AuthProvider.GITHUB;

        boolean isNew = !userRepository.existsByEmail(email);

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .fullName(name != null ? name : email)
                    .provider(provider)
                    .providerId(providerId)
                    .roles(new HashSet<>())
                    .build();
            return userRepository.save(newUser);
        });

        if (isNew) {
            userEventPublisher.publishUserRegistered(user);
        }

        String accessToken = jwtService.generateAccessToken(user);

        // Save refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(jwtService.generateRefreshToken(user))
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshToken);

        response.setContentType("application/json");
        response.getWriter().write("{\"accessToken\":\"" + accessToken + "\"}");

        log.info("OAuth2 login successful for: {}", email);
    }
}