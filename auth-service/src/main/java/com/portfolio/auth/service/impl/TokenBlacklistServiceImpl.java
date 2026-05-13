package com.portfolio.auth.service.impl;

import com.portfolio.auth.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String BLACKLIST_PREFIX = "blacklist:";

    @Override
    public void blacklistToken(String jti, long expiryMillis) {
        String key = BLACKLIST_PREFIX + jti;
        redisTemplate.opsForValue().set(key, "revoked", expiryMillis, TimeUnit.MILLISECONDS);
        log.debug("Token blacklisted with jti: {}", jti);
    }

    @Override
    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }
}