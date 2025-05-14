package com.example.demo.service.imp;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.example.demo.service.RateLimiterService;
import com.example.demo.service.RedisService;

@Service
public class RateLimiterServiceImpl implements RateLimiterService {

    private final RedisService redisService;

    public RateLimiterServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean isAllowed(String key, int maxRequests, int timeWindowInSeconds) {
        String rateLimitKey = "rate_limit:" + key;
        Object currentCountObj = redisService.get(rateLimitKey);
        Long currentCount = currentCountObj != null ? ((Number) currentCountObj).longValue() : null;

        if (currentCount == null) {
            // First request
            redisService.setWithExpireTime(rateLimitKey, 1L, timeWindowInSeconds, TimeUnit.SECONDS);
            return true;
        }

        if (currentCount >= maxRequests) {
            return false;
        }

        // Increment counter
        redisService.increment(rateLimitKey);
        return true;
    }

    @Override
    public void resetLimit(String key) {
        String rateLimitKey = "rate_limit:" + key;
        redisService.delete(rateLimitKey);
    }
}