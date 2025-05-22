package com.example.demo.service;

public interface RateLimiterService {
    boolean isAllowed(String key, int maxRequests, int timeWindowInSeconds);

    void resetLimit(String key);
}